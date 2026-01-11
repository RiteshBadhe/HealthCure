FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# Make mvnw executable for Linux
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/*.jar"]
