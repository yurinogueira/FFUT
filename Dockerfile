FROM maven:3 AS BUILD

LABEL org.opencontainers.image.source="https://github.com/yurinogueira/FFUT"

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/target/ffut-*.jar .

CMD ["java", "-jar", "ffut-*.jar", "--spring.config.location=file:/home/ubuntu/config/application.properties"]
