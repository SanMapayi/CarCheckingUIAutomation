package configuration;

import org.slf4j.Logger;
import utilities.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class ReadConfig {
    private static ReadConfig instance;
    private Properties properties;

    private static final Logger logger = LoggerUtil.getLogger();

    // Private constructor (Singleton)
    private ReadConfig() {
        properties = new Properties();
        loadConfig();
    }

    // Singleton instance getter
    public static ReadConfig getInstance() {
        if (instance == null) {
            synchronized (ReadConfig.class) {
                if (instance == null) {
                    instance = new ReadConfig();
                }
            }
        }
        return instance;
    }

    // Load config.properties file
    private void loadConfig() {
        try {
            String rootPath = System.getProperty("user.dir");
            Path configPath = Path.of(rootPath, "src", "test", "resources", "config", "config.properties");

            // Ensure file exists before reading
            if (!Files.exists(configPath)) {
                throw new RuntimeException("Config file not found at: " + configPath);
            }

            // Load properties file
            try (InputStream inputStream = Files.newInputStream(configPath, StandardOpenOption.READ)) {
                properties.load(inputStream);
                logger.info("Configuration file loaded successfully.");
            }
        } catch (IOException e) {
            logger.error("Failed to load file in class %s and thrown an exception (%s)".formatted(ReadConfig.class.getName(), e));
            throw new RuntimeException("Failed to load config file: " + e.getMessage());
        }
    }

    // Getter methods for each property
    public String getBrowser() {
        return properties.getProperty("browser", "chrome").toLowerCase();
    }

    public String getUrl() {
        return properties.getProperty("url", "https://car-checking.com");
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicitWait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicitWait", "15"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("pageLoadTimeout", "20"));
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public String getWindowSize() {
        return properties.getProperty("windowSize", "maximize");
    }
}
