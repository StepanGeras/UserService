FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*head.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
