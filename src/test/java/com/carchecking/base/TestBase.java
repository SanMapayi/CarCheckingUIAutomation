package com.carchecking.base;

import configuration.LogDirectorySetup;
import configuration.ReadConfig;
import core.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import utilities.LoggerUtil;
import utilities.ScreenshotUtil;

import java.io.IOException;
import java.time.Duration;

public class TestBase {
    protected static WebDriver driver;
    public static WebDriverWait wait;
    protected static final Logger logger = LoggerUtil.getLogger();
    public static ScreenshotUtil screenshotUtil;


    protected static ReadConfig config = ReadConfig.getInstance(); // Singleton config loader


    @BeforeSuite
    public void beforeSuite() throws IOException {
        screenshotUtil = new ScreenshotUtil(driver, logger);
        screenshotUtil.deleteFailedScreenshotsInFolder(Constants.SCREENSHOTSPATH);
    }

    @BeforeClass
    public void setup() throws IOException {
        // Loging the setup process
        logger.info("Starting setup for class: " + this.getClass().getSimpleName());
        // Initialize WebDriver from TestBase class
        TestBase.initialize();


    }


    // Initialize WebDriver
    public static WebDriver initialize() throws IOException {
        if (driver != null) {
            logger.info("WebDriver already initialized.");
            return driver;
        }

        LogDirectorySetup.createLogDirectory();  // Ensure log directory exist

        String browser = config.getBrowser();
        boolean headless = config.isHeadless();
        String windowSize = config.getWindowSize();

        logger.info("Initializing WebDriver for browser: {}", browser);

        // WebDriver setup with headless mode support for CI/CD
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) chromeOptions.addArguments("--headless");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless");
                driver = new EdgeDriver(edgeOptions);
                break;
            default:
                logger.error("Unsupported browser: " + browser);
                throw new RuntimeException("Unsupported browser: " + browser);
        }

        // Handing window size and the default is maximum
        if (windowSize.equalsIgnoreCase("maximize")) {
            driver.manage().window().maximize();
        } else if (windowSize.matches("\\d+x\\d+")) { // Ensures format like "1920x1080"
            try {
                String[] dimensions = windowSize.split("x");
                int width = Integer.parseInt(dimensions[0]);
                int height = Integer.parseInt(dimensions[1]);
                driver.manage().window().setSize(new Dimension(width, height));
            } catch (Exception e) {
                throw new RuntimeException("Invalid window size format in config. Use 'maximize' or 'WIDTHxHEIGHT'.");
            }
        } else {
            logger.error("Invalid window size: " + windowSize);
            throw new RuntimeException("Invalid window size: " + windowSize);
        }

        // timeouts from config
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));

        wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));

        screenshotUtil = new ScreenshotUtil(driver, logger);

        // Open application URL
        driver.get(config.getUrl());
        return driver;
    }

    // Getter for WebDriverWait to be used in test classes
    public static WebDriverWait getWait() {
        return wait;
    }

    // Quit driver
    @AfterClass
    public void tearDown() {
        logger.info("Ending teardown for class: " + this.getClass().getSimpleName());
        if (driver != null) {
            driver.quit();
        }
    }
    // Capture screenshot method
    public static void captureScreen(String testName) throws IOException {
        screenshotUtil.captureScreenshot(testName);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        // Capture screenshot on test failure
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("result status");
            try {
                if (TestBase.screenshotUtil == null) {
                   logger.info("screenshot is null");
                }
                if (TestBase.driver == null) {
                    logger.info("driver is null");
                }
                TestBase.captureScreen(result.getMethod().getMethodName());
                TestBase.logger.info("Screenshot captured for test: " + result.getMethod().getMethodName());
            } catch (IOException e) {
                TestBase.logger.error("Failed to capture screenshot", e);
            }
        }
        // Quit WebDriver after each test method
        if (TestBase.driver != null) {
            TestBase.driver.quit();
            TestBase.driver = null; // Reset the driver to null
        }
    }

}