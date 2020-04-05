FROM openjdk:8-jre-alpine
LABEL version = "1.0"
MAINTAINER Felix Aballi <felixaballi@gmail.com>
USER root
RUN addgroup -S thorntail && adduser -S thorntail -G thorntail
USER thorntail:thorntail

VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]