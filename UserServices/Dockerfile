# Use the official OpenJDK 21 base image
FROM --platform=linux/amd64 openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host to the container
COPY build/libs/UserServices-1.0-SNAPSHOT.jar /app/userserviceApp.jar

# Expose the port that your Java service listens on
EXPOSE 9810

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/userserviceApp.jar"]