FROM maven:3-amazoncorretto:21-alpine AS BUILD

LABEL org.opencontainers.image.source="https://github.com/yurinogueira/FFUT"

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM maven:3-amazoncorretto:21-alpine

LABEL maintainer="personal@yurinogueira.dev.br"
LABEL vendor="TUFF"

ARG JAR_FILE=/app/target/ffut-*.jar

COPY --from=build ${JAR_FILE} app.jar

CMD ["java", "-jar", "/app.jar", "--spring.config.location=file:/application.properties"]
