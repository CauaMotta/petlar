# --- FRONTEND BUILD ---
FROM node:20-alpine AS frontend-builder
WORKDIR /app
COPY ./PetLar_FrontEnd/package.json ./
COPY ./PetLar_FrontEnd/package-lock.json ./
RUN npm install
COPY ./PetLar_FrontEnd/ .
RUN npm run test
RUN npm run build

# --- BACKEND BUILD ---
FROM maven:3.9-eclipse-temurin-24 AS backend-builder
WORKDIR /app
COPY ./PetLar_BackEnd/pom.xml .
COPY ./PetLar_BackEnd/src ./src
COPY --from=frontend-builder /app/dist ./src/main/resources/static
RUN mvn clean package

# --- ESTÁGIO 2: Runtime ---
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]