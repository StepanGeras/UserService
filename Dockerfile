FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/UserService-0.0.1-SNAPSHOT.jar UserService-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "UserService-0.0.1-SNAPSHOT.jar"]
