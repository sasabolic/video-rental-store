FROM openjdk:8-jdk-alpine

VOLUME /tmp
ADD *.jar app.jar
ENV JAVA_OPTS=""
ENV VRS_MYSQL mysql

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
