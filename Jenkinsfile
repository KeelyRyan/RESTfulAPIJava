pipeline {
    agent any

    environment {
        JAVA_HOME = "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
        PATH = "/usr/local/bin:/opt/homebrew/bin:${JAVA_HOME}/bin:${env.PATH}"
         SPRING_PROFILES_ACTIVE = 'docker'
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
                    sh 'echo Running with profile: $SPRING_PROFILES_ACTIVE'
                    sh 'mvn clean package'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                dir('orders') {
                    sh 'mvn spring-boot:build-image -Dspring-boot.build-image.imageName=orders-app'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('orders') {
                    withSonarQubeEnv('SonarQube') {
                        withCredentials([string(credentialsId: 'sonar-token2', variable: 'SONAR_TOKEN')]) {
                            sh '''
                                mvn clean verify sonar:sonar \
                                  -Dsonar.projectKey=orders \
                                  -Dsonar.host.url=http://localhost:9000 \
                                  -Dsonar.login=$SONAR_TOKEN \
                                  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
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
        }

        stage('Debug Directory') {
            steps {
                sh 'pwd && ls -l'
            }
        }

        stage('Run Ansible Deployment') {
            steps {
                script {
                    dir("${env.WORKSPACE}") {
                        sh 'ansible-playbook deploy.yml'
                    }
                }
            }
        }
    } 
}
