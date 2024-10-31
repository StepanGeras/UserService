pipeline {
    agent any
    environment {
        IMAGE_NAME = "shifer/userservice:latest"
        CONSUL_HOST = "consul"
        CONSUL_PORT = "8500" 
        CONFIG_URI = "http://configservice:8888"
        DATA_MONGODB_URI = "mongodb://mongo:27017"
        DATASOURCE_URL_POSTGRES = "jdbc:postgresql://postgres:5432/postgres" 
    }
    stages {
        stage('Gradle Build') {
            steps {
                sh './gradlew clean build'
                sh 'ls -la build/libs'
            }
        }
        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                    sh "docker push ${IMAGE_NAME}"
                }
            }
        }
        stage('Deploy to Minikube') {
    steps {
        script {
            def deploymentExists = sh(
                script: "kubectl get deployment userservice --ignore-not-found",
                returnStatus: true
            ) == 0

            if (!deploymentExists) {
                sh """
                    kubectl create deployment userservice --image=${IMAGE_NAME} 
                    kubectl expose deployment userservice --type=ClusterIP --port=8081
                 """
            } else {
                sh "kubectl set image deployment/userservice userservice=${IMAGE_NAME}"
            }
            sh """
                withCredentials([usernamePassword(credentialsId: 'postgres-credentials', usernameVariable: 'POSTGRES_USER', passwordVariable: 'POSTGRES_PASS')]) {
                    kubectl set env deployment/userservice SPRING_DATASOURCE_URL=${DATASOURCE_URL_POSTGRES} 
                    kubectl set env deployment/userservice SPRING_DATASOURCE_USERNAME=${POSTGRES_USERNAME} 
                    kubectl set env deployment/userservice SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASS} 
                }
                kubectl set env deployment/userservice SPRING_CLOUD_CONSUL_HOST=${CONSUL_HOST} 
                kubectl set env deployment/userservice SPRING_CLOUD_CONSUL_PORT=${CONSUL_PORT}
                kubectl set env deployment/userservice SPRING_CLOUD_CONFIG_URI=${CONFIG_URI} 
                kubectl set env deployment/userservice SPRING_DATA_MONGODB_URI=${DATA_MONGODB_URI} 
            """
                }
            }
        }
    }
}

