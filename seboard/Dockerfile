FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY /var/jenkins_home/workspace/seboard-pipeline/seboard-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources resources
EXPOSE 8888
ENTRYPOINT ["java","-jar","app.jar"]