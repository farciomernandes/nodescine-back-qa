FROM openjdk:24-jdk-slim AS build

WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Copiar apenas o pom.xml primeiro (para cache de dependências)
COPY pom.xml ./

# Baixar dependências sem código-fonte (isso é cacheado pelo Docker)
RUN mvn dependency:go-offline -B

# Agora copiar o código fonte
COPY src ./src

# Compilar a aplicação
RUN mvn clean package -DskipTests

# --- Fase final, só com o JAR ---
FROM openjdk:24-jdk-slim

WORKDIR /app

# Copiar o JAR da fase de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
