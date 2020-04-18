FROM amazoncorretto:11
LABEL maintainer="yuya okada"

ARG S3_BUCKET
ENV S3_BUCKET_NAME ${S3_BUCKET}

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT [ \
    "java", \
    "-jar", \
    "/app.jar"]
