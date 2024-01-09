FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY . .
RUN mvn package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

COPY --from=builder /app/target/nabd-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "nabd-0.0.1-SNAPSHOT.jar"]
