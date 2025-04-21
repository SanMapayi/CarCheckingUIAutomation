# Use an official OpenJDK base image
FROM eclipse-temurin:23-jdk

# Install netcat (nc)
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*
# Create app directory
WORKDIR /app

# Copy the built JARs and resources
COPY target/CarCheckingTest.jar .
COPY target/CarCheckingTest-tests.jar .
COPY target/libs ./libs
COPY testng.xml .
COPY src/test/resources ./src/test/resources

# Copy wait script
COPY wait-for-grid.sh /usr/bin/wait-for-grid.sh
RUN chmod +x /usr/bin/wait-for-grid.sh

# Set default command to wait then run tests
ENTRYPOINT ["/usr/bin/wait-for-grid.sh"]
CMD ["java", "-cp", "CarCheckingTest.jar:CarCheckingTest-tests.jar:libs/*", "org.testng.TestNG", "-verbose", "10", "testng.xml"]
