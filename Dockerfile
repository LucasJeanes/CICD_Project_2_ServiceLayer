FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/CICD_Project_2_ServiceLayer-0.0.1-SNAPSHOT.jar /app
EXPOSE 8082
CMD ["java", "-jar", "CICD_Project_2_ServiceLayer-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]