FROM amazoncorretto:11
LABEL maintainer="yuya okada"

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT [ \
    "java", \
    "-jar", \
    "/app.jar"]
