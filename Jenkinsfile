#!groovy

properties(
  [[$class: 'GithubProjectProperty', projectUrlStr: 'http://git.reform.hmcts.net/cmc/pdf-service/'],
   pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)

@Library('Reform@feature/ROPS-632-promotion-and-tagging')
import uk.gov.hmcts.Ansible
import uk.gov.hmcts.Packager
import uk.gov.hmcts.Versioner
import uk.gov.hmcts.RPMTagger
@Library('CMC')
import uk.gov.hmcts.cmc.integrationtests.IntegrationTests
import uk.gov.hmcts.cmc.smoketests.SmokeTests

def ansible = new Ansible(this, 'cmc')
def packager = new Packager(this, 'cmc')
def versioner = new Versioner(this)

def smokeTests = new SmokeTests(this)
def integrationTests = new IntegrationTests(env, this)

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
        sh "./gradlew clean build -x test"
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


      stage('Package (JAR)') {
        versioner.addJavaVersionInfo()
        sh "./gradlew bootRepackage installDist"
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
        pdfServiceVersion = dockerImage imageName: 'cmc/pdf-service-api'
      }

      stage('Integration Tests') {
        integrationTests.execute([
          'PDF_SERVICE_API_VERSION'     : pdfServiceVersion
        ])
      }

      RPMTagger rpmTagger = new RPMTagger(this,
        'pdf-service',
        packager.rpmName('pdf-service', pdfServiceRPMVersion),
        'cmc-local'
      )
      if ("master" == "${env.BRANCH_NAME}") {
        milestone()
        lock(resource: "CMC-deploy-dev", inversePrecedence: true) {
          stage('Deploy (Dev)') {
            ansibleCommitId = ansible.runDeployPlaybook(version, 'dev')
            rpmTagger.tagDeploymentSuccessfulOn('dev')
            rpmTagger.tagAnsibleCommit(ansibleCommitId)
          }
          stage('Smoke test (Dev)') {
            smokeTests.executeAgainst(env.CMC_DEV_APPLICATION_URL)
            rpmTagger.tagTestingPassedOn('dev')
          }
        }

        milestone()
        lock(resource: "CMC-deploy-test", inversePrecedence: true) {
          stage('Deploy (Test)') {
            ansibleCommitId = ansible.runDeployPlaybook(version, 'test', ansibleCommitId)
            rpmTagger.tagDeploymentSuccessfulOn('test')
          }
          stage('Smoke test (Test)') {
            smokeTests.executeAgainst(env.CMC_DEV_APPLICATION_URL)
            rpmTagger.tagTestingPassedOn('test')
          }
        }
      }
    } catch (err) {
      notifyBuildFailure channel: '#cmc-tech-notification'
      throw err
    }
  }
  milestone()
}
