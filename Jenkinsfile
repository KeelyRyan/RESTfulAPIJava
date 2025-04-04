pipeline {
    agent any
    tools {
        maven 'Default Macven'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/KeelyRyan/RESTfulAPIJava.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn clean verify sonar:sonar'
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build('restfulapijava:latest')
                }
            }
        }
    }
}
