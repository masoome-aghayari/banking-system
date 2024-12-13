FROM openjdk:17-jdk-slim
WORKDIR /app
COPY mvnw* ./
COPY .mvn/ .mvn/
COPY pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src/ /app/src/
RUN ./mvnw package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/banking-system-1.0.jar"]
