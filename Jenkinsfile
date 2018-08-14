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
    stage('Deploy') {
      steps {
        sh 'gradle deploy'
      }
    }
    stage('Zip') {
      steps {
        sh 'gradle zipOutput'
      }
    }
    stage('Test') {
      steps {
        sh 'gradle test'
      }
    }
    stage('Report') {
      steps {
        junit 'build/reports/**/*.xml'
      }
    }
    stage('Archive') {
      steps {
        archiveArtifacts '*.zip'
      }
    }
  }
}