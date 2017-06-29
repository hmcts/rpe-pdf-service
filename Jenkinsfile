#!groovy

properties(
  [[$class: 'GithubProjectProperty', projectUrlStr: 'http://git.reform.hmcts.net/cmc/pdf-service/'],
   pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)

@Library('Reform')
import uk.gov.hmcts.Ansible
import uk.gov.hmcts.Packager
import uk.gov.hmcts.Versioner
import uk.gov.hmcts.RPMTagger

def ansible = new Ansible(this, 'cmc')
def packager = new Packager(this, 'cmc')
def versioner = new Versioner(this)

milestone()
lock(resource: "pdf-service-${env.BRANCH_NAME}", inversePrecedence: true) {
  node {
    try {
      def version
      def pdfServiceVersion
      String pdfServiceRPMVersion

      stage('Checkout') {
        deleteDir()
        checkout scm
      }

      stage('Build') {
        versioner.addJavaVersionInfo()
        sh "./gradlew build -x test"
      }

      stage('OWASP dependency check') {
        try {
          sh "./gradlew -DdependencyCheck.failBuild=true dependencyCheck"
        } catch (err) {
          archiveArtifacts 'build/reports/dependency-check-report.html'
          notifyBuildResult channel: '#cmc-tech-notification', color: 'warning',
            message: 'OWASP dependency check failed see the report for the errors'
        }
      }

      stage('Test (Unit)') {
        sh "./gradlew test"
      }

      stage('Test (API)') {
        sh "./gradlew apiTest"
      }

      stage('Package (RPM)') {
        pdfServiceRPMVersion = packager.javaRPM('pdf-service', 'build/libs/pdf-service-$(./gradlew -q printVersion)-all.jar',
          'springboot', 'src/main/resources/application.yml')
        version = "{pdf_service_buildnumber: ${pdfServiceRPMVersion} }"

        if ("master" == "${env.BRANCH_NAME}") {
          packager.publishJavaRPM('pdf-service')
        }
      }

      stage('Package (Docker)') {
        sh "./gradlew clean installDist"
        pdfServiceVersion = dockerImage imageName: 'cmc/pdf-service-api'
      }

      RPMTagger rpmTagger = new RPMTagger(this,
        'pdf-service',
        packager.rpmName('pdf-service', pdfServiceRPMVersion),
        'cmc-local'
      )
      onMaster {
        milestone()
        lock(resource: "CMC-deploy-dev", inversePrecedence: true) {
          stage('Deploy (Dev)') {
            ansibleCommitId = ansible.runDeployPlaybook(version, 'dev')
            rpmTagger.tagDeploymentSuccessfulOn('dev')
            rpmTagger.tagAnsibleCommit(ansibleCommitId)
          }
        }

        milestone()
      }
    } catch (err) {
      archiveArtifacts 'build/reports/**/*.html'
      archiveArtifacts 'build/pdf-service/reports/**/*.html'
      notifyBuildFailure channel: '#cmc-tech-notification'
      throw err
    }
  }
  milestone()
}
