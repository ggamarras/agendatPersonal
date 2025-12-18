# Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# Run
FROM eclipse-temurin:17-jre
WORKDIR /app
#COPY --from=build /app/target/vault-web-app-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/target/vault-web-app-1.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar"]