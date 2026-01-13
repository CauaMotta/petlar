# --- BACKEND BUILD ---
FROM maven:3.9-eclipse-temurin-24 AS backend-builder
WORKDIR /app
COPY ./PetLar_BackEnd/pom.xml .
COPY ./PetLar_BackEnd/src ./src
RUN mvn clean package -DskipTests

# --- ESTÁGIO 2: Runtime ---
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]