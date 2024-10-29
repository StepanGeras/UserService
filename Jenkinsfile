pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "myusername/myapp:${version}"
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        MINIKUBE_CONTEXT = 'minikube'
    }

    stages {
        stage('build') {
            steps {
                script {
                    sh './gradlew build'
                }
            }
        }

        stage('docker-build') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('docker-push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        sh "docker push ${DOCKER_IMAGE}"
                    }
                }
            }
        }

        stage('deploy-minikube') {
            steps {
                script {
                    sh 'eval $(minikube docker-env)'

                    sh """
                    kubectl set image deployment/myapp-deployment myapp=${DOCKER_IMAGE} -n default
                    kubectl rollout status deployment/myapp-deployment -n default
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
