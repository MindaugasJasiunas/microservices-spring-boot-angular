#FROM openjdk:11-jre-slim as builder
FROM eclipse-temurin:17-jre-focal as builder
WORKDIR application
ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

#FROM openjdk:11-jre-slim
FROM eclipse-temurin:17-jre-focal
LABEL PROJECT_NAME=${project.artifactId} \
      PROJECT=${project.id}

EXPOSE 9090

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]