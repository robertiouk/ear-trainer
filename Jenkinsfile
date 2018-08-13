pipeline {
  agent {
    docker {
      image 'gradle:4.9.0-jdk8'
    }

  }
  stages {
    stage('Init') {
      steps {
        sh '''echo PATH=${PATH}
echo GRADLE_HOME=${GRADLE_HOME}
gradle clean'''
      }
    }
    stage('Build') {
      steps {
        sh 'gradle build'
      }
    }
    stage('Report') {
      steps {
        junit 'build\\reports\\tests\\test\\index.html'
      }
    }
    stage('Archive') {
      steps {
        archiveArtifacts 'output\\*'
      }
    }
  }
}