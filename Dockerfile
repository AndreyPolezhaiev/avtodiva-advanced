FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /application

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests
RUN java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:21-jre
WORKDIR /application

COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

EXPOSE 8080
EXPOSE 5005