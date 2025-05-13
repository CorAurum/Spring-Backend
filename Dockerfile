# Use Eclipse Temurin JDK 21 as the base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the container
COPY ../../AppData/Local/Temp/Rar$DRa18716.37835.rartemp/Spring-Backend-main .

# Grant execute permissions to the Maven wrapper and build the project
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Run the application
CMD ["sh", "-c", "java -jar target/*.jar"]
