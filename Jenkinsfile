pipeline {
    agent any
    environment {
        JAVA_HOME = "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }
   tools {
        maven 'Default Maven'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/KeelyRyan/RESTfulAPIJava.git'
            }
        }
        stage('Build') {
            steps {
                dir('orders') {
                    sh 'mvn clean package'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                dir('orders') {  // Specify the correct directory
                    withSonarQubeEnv('SonarQube') {
                        withCredentials([string(credentialsId: 'sonar-token2', variable: 'SONAR_TOKEN')]) {
                            sh '''
                                mvn clean verify sonar:sonar \
                                -Dsonar.projectKey=orders \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.login=$SONAR_TOKEN
                            '''
                        }
                    }
                }
            }
        }
        stage('Test') {
            steps {
                dir('orders') {
                    sh 'mvn test'
                }
            }
        // stage('Docker Build') {
        //     steps {
        //         dir('orders') {
        //             script {
        //                 docker.build('restfulapijava:latest')
        //             }
        //         }
        //     }
        // }
    }
}
}