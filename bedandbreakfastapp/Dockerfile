# MULTISTAGE
# FROM maven:3.9.9-eclipse-temurin-21 AS compiler
FROM maven:3.9.9-amazoncorretto-21 AS compiler

# These is just a name
ARG COMPILE_DIR=/code_folder
WORKDIR ${COMPILE_DIR}

# Copy project files and build
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY src src
COPY .mvn .mvn

#Compile and package the application
RUN mvn clean package -Dmaven.test.skip=true


# stage 2 (Runtime Stage)
# Consider using a lighter base image like  switch to a runtime-only image like eclipse-temurin:23-jre or openjdk:23-jre to reduce the image size. (use jre?)
# The maven image in the runtime stage includes unnecessary tools for running the application.
# FROM eclipse-temurin:23-jre AS runtime
# FROM maven:3.9.9-eclipse-temurin-21 AS runtime
FROM maven:3.9.9-amazoncorretto-21 AS runtime

ARG DEPLOY_DIR=/app
WORKDIR ${DEPLOY_DIR}

# Just copying the jar file and renaming it to day17l
COPY --from=compiler /code_folder/target/assessment-0.0.1-SNAPSHOT.jar assessment.jar

ENV SERVER_PORT=3000
EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "assessment.jar"]