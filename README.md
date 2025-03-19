Project Overview
This project automates the process of checking car valuations using the car-checking website. The tests include validating the car registration numbers extracted from a text file, performing vehicle detail searches on the website, and comparing the results against expected outputs. The framework is built using TestNG and Selenium.

Key Features:
Extracts car registration numbers from an input file (car_input.txt).
Searches for car details on the car-checking website.
Validates car make, model, and year with data from car_output.txt.
Configurable browser support (Chrome, Firefox, Edge) and headless mode for CI/CD environments.
Getting Started
Prerequisites
Before running the tests, make sure you have the following installed:

Java Development Kit (JDK)
Ensure that Java 8 or higher is installed. You can verify by running:

bash
Copy
Edit
java -version
Maven
Apache Maven is used for project dependencies and build management. You can check if Maven is installed by running:

bash
Copy
Edit
mvn -v
IDE (Integrated Development Environment)
You can use any IDE (like IntelliJ IDEA, Eclipse, etc.) that supports Java and Maven.

Browser Drivers
The project uses WebDriverManager to automatically manage and download the appropriate browser drivers (Chrome, Firefox, Edge).

Running Tests
1. Clone the Repository
bash
Copy
Edit
git clone <repository_url>
cd <project_directory>
2. Install Project Dependencies
Once the repository is cloned, navigate to the project directory and install all required dependencies using Maven:

bash
Copy
Edit
mvn clean install
3. Run the Tests Using TestNG
To run the tests, you can use the following command:

Running All Tests
bash
Copy
Edit
mvn test
This will execute all the TestNG tests defined in the project.

Running Specific Test Class
To run a specific test class, use the following command:

bash
Copy
Edit
mvn -Dtest=CarValuationTest test
Running Tests in a Specific Suite
If you have configured TestNG XML suites (e.g., testng.xml), you can run the suite using:

bash
Copy
Edit
mvn test -DsuiteXmlFile=testng.xml
Running in Headless Mode
Headless mode is enabled by default for browsers. If you want to ensure headless execution, check the config file config.properties and set headless=true.

Test Configuration
The project uses the following configurations to control test execution:

Browser: Chrome, Firefox, or Edge (configured in config.properties).
Window Size: maximize or custom (e.g., 1920x1080).
Implicit Wait: Configurable timeout for waiting for elements to appear.
Headless Mode: Set to true by default for CI/CD environments.
The configuration file is located in:

bash
Copy
Edit
src/test/resources/config/config.properties
Sample config.properties:
properties
Copy
Edit
browser=chrome
headless=true
windowSize=1920x1080
implicitWait=10
pageLoadTimeout=30
url=https://car-checking.com
Test Reports
Once the tests have run, TestNG generates reports in the target directory. You can find detailed execution reports in the following locations:

TestNG HTML Report: target/surefire-reports/index.html
TestNG XML Report: target/test-classes/testng-results.xml
Adding New Test Cases
To add a new test case:

Create a new test class in src/test/java/com/carchecking/testcases/.
Use the @Test annotation for your test method.
Optionally, use a DataProvider to run the test with multiple inputs (for plate numbers, etc.).
Add the test to the appropriate TestNG suite if necessary (configured via XML).
Collaborator Guidelines
Branching Strategy: Use feature branches for new work and merge requests for review.
Logging: Ensure that each test has proper logging for easier debugging.
Test Data: Test data is managed in car_input.txt and car_output.txt files. Always validate that new data is in the correct format before adding.
Contributing
We welcome contributions to improve this project. Here's how you can contribute:

Fork the repository.
Create a new branch.
Make your changes and commit them.
Push the changes to your forked repository.
Create a pull request with a description of your changes.
Troubleshooting
If you face any issues while running the tests, here are some common solutions:

Issue with WebDriver: Ensure the browser is properly set in the config file and that the correct version of the browser is installed.
Test Failures: Check the logs in the target/surefire-reports/ folder for more detailed error messages.
TestNG Configuration: Verify your testng.xml file if running tests in a suite.
