package com.carchecking.behaviour;

import com.carchecking.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GetMethods extends TestBase {

    public static final GetMethods getInstance = new GetMethods();

    private GetMethods() {}

    /**
     * Retrieves the text of a WebElement after waiting for its visibility and interactability.
     * Handles switching to the iframe before interacting with elements inside it.
     *
     * @param element WebElement from which text is to be retrieved.
     * @return The visible text of the element, or an empty string if retrieval fails.
     */
    public String getText(WebElement element) {
        try {
            // Switch to the iframe containing the relevant elements
            switchToCarOutputIframe();

            // Wait for visibility of the element
            wait.until(ExpectedConditions.visibilityOf(element));
            String text = element.getText();

            // If getText returns empty, try getting the innerHTML
            if (text.isEmpty()) {
                text = element.getAttribute("innerHTML");
            }

            logger.info("Retrieved text: '{}'", text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {} | Exception: {}", element, e.getMessage());
            return "";
        }
    }

    /**
     * Retrieves the value for a specific detail (e.g., Make, Model, Year) based on the label using JavaScript.
     * Handles switching to the iframe before interacting with elements inside it.
     *
     * @param label The label text (e.g., "Make", "Model").
     * @return The value corresponding to the label (e.g., "BMW").
     */
    public String getDetailValueByLabel(String label, WebElement webElement) {
        try {
            // Switch to the iframe containing the relevant elements
            switchToCarOutputIframe();

            // Wait for the WebElement to be clickable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(webElement));

            // Use JavaScript to retrieve the corresponding detail value by label
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // JavaScript to get the value of the WebElement directly
            String script = "return arguments[0].textContent;";  // Directly use the WebElement argument

            // Execute JavaScript and return the result
            String value = (String) js.executeScript(script, webElement);

            if (value != null && !value.isEmpty()) {
                logger.info("Retrieved value for '{}' using JavaScript: {}", label, value);
            } else {
                logger.warn("No value found for '{}'", label);
            }

            return value != null ? value : "";
        } catch (Exception e) {
            logger.error("Failed to retrieve detail value for '{}': {}", label, e.getMessage());
            return "";
        }
    }


    /**
     * Switches to the iframe that contains the car output details.
     */
    private void switchToCarOutputIframe() {
        try {
            // Find the iframe containing the car output details by its unique selector (using an iframe with a known src or other attribute)
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"content\"]/div[1]/div[3]/div/div/div[2]")));

            // Switch to the iframe
            driver.switchTo().frame(iframe);
            logger.info("Switched to the iframe containing car output details.");
        } catch (Exception e) {
            logger.error("Failed to switch to iframe: {}", e.getMessage());
        }
    }
}
