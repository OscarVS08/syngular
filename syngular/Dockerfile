# Construcción con Maven y JDK 23
FROM eclipse-temurin:23-jdk-jammy AS build
WORKDIR /app

# Copiar archivos de dependencias y código fuente
COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src

# Empaquetar la aplicación omitiendo pruebas temporales
RUN ./mvnw clean package -DskipTests

# Imagen ligera de ejecución con JRE 23
FROM eclipse-temurin:23-jre-jammy
WORKDIR /app

# Copiar el archivo JAR resultante
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto estándar de Spring Boot
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]