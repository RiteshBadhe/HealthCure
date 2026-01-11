FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Build project without tests
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Run the jar
CMD ["sh", "-c", "java -jar target/*.jar"]
