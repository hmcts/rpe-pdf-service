#!groovy

//noinspection GroovyAssignabilityCheck Jenkins API requires this format
properties(
  [[$class: 'GithubProjectProperty', projectUrlStr: 'https://github.com/hmcts/cmc-pdf-service/'],
   pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)

@Library('Reform')
import uk.gov.hmcts.Ansible
import uk.gov.hmcts.Packager
import uk.gov.hmcts.Versioner
import uk.gov.hmcts.RPMTagger

Ansible ansible = new Ansible(this, 'cmc')
Packager packager = new Packager(this, 'cmc')
Versioner versioner = new Versioner(this)

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

      if (env.BRANCH_NAME == 'hotfix/Strip-illegal-xml-characters') {
        stage('Build') {
          versioner.addJavaVersionInfo()
          sh "./gradlew build -x test"
        }

        stage('OWASP dependency check') {
          try {
            sh "./gradlew -DdependencyCheck.failBuild=true dependencyCheck"
          } catch (ignored) {
            archiveArtifacts 'build/reports/dependency-check-report.html'
            notifyBuildResult channel: '#cmc-tech-notification', color: 'warning',
              message: 'OWASP dependency check failed see the report for the errors'
          }
        }

        stage('Test (Unit)') {
          sh "./gradlew test"
        }
      }

      stage('Test (API)') {
        sh "./gradlew apiTest"
      }

      stage('Sonar') {
        onPR {
          withCredentials([string(credentialsId: 'jenkins-public-github-api-token-text', variable: 'GITHUB_ACCESS_TOKEN')]) {
            String prNumber = env.BRANCH_NAME.substring(3)
            sh """
               ./gradlew -Dsonar.analysis.mode=preview \
                -Dsonar.github.pullRequest=$prNumber \
                -Dsonar.github.repository=hmcts/cmc-pdf-service \
                -Dsonar.github.oauth=$GITHUB_ACCESS_TOKEN \
                -Dsonar.host.url=$SONARQUBE_URL \
                sonarqube
            """
          }
        }

        if (env.BRANCH_NAME == 'hotfix/Strip-illegal-xml-characters') {
          sh "./gradlew -Dsonar.host.url=$SONARQUBE_URL sonarqube"
        }
      }


      stage('Package (RPM)') {
        sh "./gradlew bootRepackage"
        pdfServiceRPMVersion = packager.javaRPM('', 'pdf-service', 'build/libs/pdf-service-$(./gradlew -q printVersion)-all.jar',
          'springboot', 'src/main/resources/application.yml', true)
        version = "{pdf_service_buildnumber: ${pdfServiceRPMVersion} }"

        if (env.BRANCH_NAME == 'hotfix/Strip-illegal-xml-characters') {
          packager.publishJavaRPM('pdf-service')
        }
      }

//      stage('Package (Docker)') {
//        sh "./gradlew clean installDist"
//        pdfServiceVersion = dockerImage imageName: 'cmc/pdf-service-api'
//      }

      //noinspection GroovyVariableNotAssigned it is guaranteed to be assigned
      RPMTagger rpmTagger = new RPMTagger(this,
        'pdf-service',
        packager.rpmName('pdf-service', pdfServiceRPMVersion),
        'cmc-local'
      )
      if (env.BRANCH_NAME == 'hotfix/Strip-illegal-xml-characters') {
        milestone()
        lock(resource: "CMC-deploy-test", inversePrecedence: true) {
          stage('Deploy (Test)') {
            ansibleCommitId = ansible.runDeployPlaybook(version, 'test')
            rpmTagger.tagDeploymentSuccessfulOn('test')
            rpmTagger.tagAnsibleCommit(ansibleCommitId)
          }
        }

        milestone()
//        lock(resource: "CMC-deploy-demo", inversePrecedence: true) {
//          stage('Deploy (Demo)') {
//            ansible.runDeployPlaybook(version, 'demo')
//          }
//        }
//
//        milestone()
      }

    } catch (err) {
      if (env.BRANCH_NAME == 'hotfix/Strip-illegal-xml-characters') {
        archiveArtifacts 'build/reports/**/*.html'
        archiveArtifacts 'build/pdf-service/reports/**/*.html'
      }
      notifyBuildFailure channel: '#cmc-tech-notification'
      throw err
    }
  }
  milestone()
}
