FROM openjdk:24-jdk-slim

WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml ./

# Copiar código fonte
COPY src ./src

# Instalar Maven e construir aplicação
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Expor porta
EXPOSE 8080

# Executar aplicação
CMD ["java", "-jar", "target/*.jar"]
