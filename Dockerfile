#FROM openjdk:8-jdk-alpine
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM eclipse-temurin:20-jdk-alpine
WORKDIR /app
COPY out/artifacts/jwtProject_jar/jwtProject.jar jwtProject.jar
EXPOSE 8080
CMD ["java", "-jar", "jwtProject.jar"]
