FROM amazoncorretto:25-alpine-jdk@sha256:80667e38af71ac103a3ae36a0b531d54c73c4da28fc02b57f69bce8993c0e1b0

ARG CI_COMMIT_TIMESTAMP
ARG CI_COMMIT_SHA
ARG CI_COMMIT_TAG

LABEL org.opencontainers.image.authors="Group F, Infrastructure Engineering PT, Hochschule Burgenland"
LABEL org.opencontainers.image.vendor="Infrastructure-Engineering-PT-Group-F"
LABEL org.opencontainers.image.source="https://github.com/Infrastructure-Engineering-PT-Group-F/backend"
LABEL org.opencontainers.image.created="${CI_COMMIT_TIMESTAMP}"
LABEL org.opencontainers.image.title="infrastructure-engineering-pt-group-f-backend"
LABEL org.opencontainers.image.description="The backend of Group F's weather application for the Infrastructure Engineering (PT) course at the Hochschule Burgenland."
LABEL org.opencontainers.image.revision="${CI_COMMIT_SHA}"
LABEL org.opencontainers.image.version="${CI_COMMIT_TAG}"

COPY build/libs/infrastructure-engineering-pt-group-f-backend-*.jar /app.jar

EXPOSE 8080/tcp

CMD ["java", "-jar", "/app.jar"]
