pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials' // ID учетных данных Docker Hub
        DOCKER_IMAGE = "shifer/${env.JOB_NAME}" // имя образа
        VERSION = versionNumber() // получение версии из функции версии Gradle
    }

    stages {
        stage('Checkout') {
            steps {
                // Склонировать проект из Git-репозитория
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Собрать проект с помощью Gradle и создать Docker образ
                sh './gradlew clean build' // Собирает проект
                sh "docker build -t ${DOCKER_IMAGE}:${VERSION} ." // Создает Docker образ с версией
            }
        }

        stage('Push to Docker Hub') {
            steps {
                // Аутентификация и пуш образа в Docker Hub
                withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}:${VERSION}"
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                // Деплой образа в Minikube
                script {
                    sh "kubectl set image deployment/${JOB_NAME} ${JOB_NAME}=${DOCKER_IMAGE}:${VERSION} --namespace=microservices"
                }
            }
        }
    }
}


