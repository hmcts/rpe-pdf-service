#!groovy

//noinspection GroovyAssignabilityCheck Jenkins API requires this format
properties(
  [[$class: 'GithubProjectProperty', projectUrlStr: 'https://github.com/hmcts/cmc-pdf-service/'],
   pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)

@Library('Reform')
import uk.gov.hmcts.Packager
import uk.gov.hmcts.Versioner

Packager packager = new Packager(this, 'reform')
Versioner versioner = new Versioner(this)

def channel = '#platform-engineering'

milestone()
lock(resource: "pdf-service-${env.BRANCH_NAME}", inversePrecedence: true) {
  node {
    try {
      stage('Checkout') {
        deleteDir()
        checkout scm
      }

      onMaster {
        stage('Build') {
          versioner.addJavaVersionInfo()
          sh "./gradlew build -x test"
        }

        stage('OWASP dependency check') {
          try {
            sh "./gradlew -DdependencyCheck.failBuild=true dependencyCheck"
          } catch (ignored) {
            archiveArtifacts 'build/reports/dependency-check-report.html'
            notifyBuildResult channel: channel, color: 'warning',
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

        onMaster {
          sh "./gradlew -Dsonar.host.url=$SONARQUBE_URL sonarqube"
        }
      }


      stage('Package (RPM)') {
        sh "./gradlew bootRepackage"
        packager.javaRPM('', 'pdf-service', 'build/libs/pdf-service-all.jar',
          'springboot', 'src/main/resources/application.yml', true)

        onMaster {
          packager.publishJavaRPM('pdf-service')
        }
      }

      stage('Package (Docker)') {
        sh "./gradlew clean installDist"
        dockerImage imageName: 'reform/pdf-service-api'
      }
    } catch (err) {
      onMaster {
        archiveArtifacts 'build/reports/**/*.html'
        archiveArtifacts 'build/pdf-service/reports/**/*.html'
      }
      notifyBuildFailure channel: channel
      throw err
    } finally {
        step([$class: 'InfluxDbPublisher',
               customProjectName: 'PDF Service',
               target: 'Jenkins Data'])
    }
  }
  milestone()
}
