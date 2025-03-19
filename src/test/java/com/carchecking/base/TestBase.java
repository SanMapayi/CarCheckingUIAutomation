package com.carchecking.base;

import configuration.LogDirectorySetup;
import configuration.ReadConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utilities.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

public class TestBase {
    protected static WebDriver driver;
    public static WebDriverWait wait;
    protected static final Logger logger = LoggerUtil.getLogger();
    protected static ReadConfig config = ReadConfig.getInstance(); // Singleton config loader



    @BeforeClass
    public void setup() {
        // Loging the setup process
        logger.info("Starting setup for class: " + this.getClass().getSimpleName());
            // Initialize WebDriver from TestBase class
            TestBase.initialize();

    }


    // Initialize WebDriver
    public static void initialize() {
        if (driver != null) {
            logger.info("WebDriver already initialized.");
            return;
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

        // Open application URL
        driver.get(config.getUrl());
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
    public static void captureScreen(WebDriver driver, String testName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        Path screenshotsFilePath = Path.of(System.getProperty("user.dir"), "Screenshots", testName + ".png");
        File target = new File(screenshotsFilePath.toFile().toString());
        FileUtils.copyFile(source, target);
        logger.info("Screenshot taken: {}", target.getAbsolutePath());
    }

}
