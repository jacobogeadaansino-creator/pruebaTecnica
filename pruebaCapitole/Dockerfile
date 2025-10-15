FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/pruebaCapitole-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /app/config

COPY src/main/resources/application.yaml /app/config/application.yaml

COPY docker-entrypoint.sh /app/docker-entrypoint.sh

RUN chmod +x /app/docker-entrypoint.sh

EXPOSE 6969

ENTRYPOINT ["/app/docker-entrypoint.sh"]
