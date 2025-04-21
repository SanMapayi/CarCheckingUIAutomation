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
import org.openqa.selenium.remote.RemoteWebDriver;
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
import java.net.URL;
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
        // Log the setup process
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

        LogDirectorySetup.createLogDirectory();  // Ensure log directory exists

        String browser = config.getBrowser();
        boolean headless = config.isHeadless();
        String windowSize = config.getWindowSize();
        boolean useRemote = Boolean.parseBoolean(System.getenv().getOrDefault("USE_REMOTE_DRIVER", "false"));

        logger.info("Initializing WebDriver for browser: {}", browser);
        logger.info("Using Remote WebDriver: {}", useRemote);

        if (useRemote) {
            try {
                // Get the Hub URL (environment variable or default to 'selenium-hub')
                String hubHost = System.getenv("HUB_HOST") != null ? System.getenv("HUB_HOST") : "selenium-hub";
                URL remoteUrl = new URL("http://" + hubHost + ":4444/wd/hub");
                logger.info("Using Selenium Hub URL: {}", remoteUrl.toString());

                // Initialize WebDriver with proper options based on the browser
                switch (browser.toLowerCase()) {
                    case "chrome":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        if (headless) chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("--no-sandbox"); // Disable sandbox for Docker
                        chromeOptions.addArguments("--disable-dev-shm-usage"); // Avoid resource limitations in Docker
                        driver = new RemoteWebDriver(remoteUrl, chromeOptions);
                        break;
                    case "firefox":
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        if (headless) firefoxOptions.addArguments("--headless");
                        driver = new RemoteWebDriver(remoteUrl, firefoxOptions);
                        break;
                    case "edge":
                        EdgeOptions edgeOptions = new EdgeOptions();
                        if (headless) edgeOptions.addArguments("--headless");
                        driver = new RemoteWebDriver(remoteUrl, edgeOptions);
                        break;
                    default:
                        logger.error("Unsupported browser for RemoteWebDriver: {}", browser);
                        throw new RuntimeException("Unsupported browser for RemoteWebDriver: " + browser);
                }
            } catch (Exception e) {
                logger.error("Failed to initialize RemoteWebDriver", e);
                throw new RuntimeException("RemoteWebDriver setup failed", e);
            }
        } else {
            // Local WebDriver setup (your original code)
            switch (browser.toLowerCase()) {
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
                    logger.error("Unsupported browser: {}", browser);
                    throw new RuntimeException("Unsupported browser: " + browser);
            }
        }

        // Window sizing
        if (windowSize.equalsIgnoreCase("maximize")) {
            driver.manage().window().maximize();
        } else if (windowSize.matches("\\d+x\\d+")) {
            try {
                String[] dimensions = windowSize.split("x");
                int width = Integer.parseInt(dimensions[0]);
                int height = Integer.parseInt(dimensions[1]);
                driver.manage().window().setSize(new Dimension(width, height));
            } catch (Exception e) {
                throw new RuntimeException("Invalid window size format in config. Use 'maximize' or 'WIDTHxHEIGHT'.");
            }
        }

        // Timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));

        // Screenshot Util
        screenshotUtil = new ScreenshotUtil(driver, logger);

        // Launch URL
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
            try {
                captureScreen(result.getMethod().getMethodName());
                logger.info("Screenshot captured for test: " + result.getMethod().getMethodName());
            } catch (IOException e) {
                logger.error("Failed to capture screenshot", e);
            }
        }

        // Quit WebDriver after each test method
        if (driver != null) {
            driver.quit();
            driver = null; // Reset the driver to null
        }
    }
}
