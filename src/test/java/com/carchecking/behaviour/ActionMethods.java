package com.carchecking.behaviour;

import com.carchecking.base.TestBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;

public class ActionMethods extends TestBase {

    public static final ActionMethods getInstance = new ActionMethods();

    private ActionMethods() {}

    public void enterText(WebElement element, String value, String featureName) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(value);
            logger.info("Entered value '{}' into '{}'", value, featureName);
        } catch (TimeoutException e) {
            logger.error("Timeout: Element '{}' not visible for entering text", featureName, e);
        } catch (Exception e) {
            logger.error("Error entering text '{}' in '{}': {}", value, featureName, e.getMessage());
        }
    }

    public void click(WebElement element, String featureName) {
        try {
            if (isDisplayed(element)) {
                getWait().until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                logger.info("Clicked on '{}'", featureName);
            } else {
                logger.warn("Click failed: Element '{}' not visible", featureName);
            }
        } catch (TimeoutException e) {
            logger.error("Timeout: '{}' not clickable", featureName, e);
        } catch (Exception e) {
            logger.error("Error clicking '{}': {}", featureName, e.getMessage());
            try {
                captureScreen(driver, featureName);
            } catch (IOException ioException) {
                logger.error("Failed to capture screenshot for '{}'", featureName, ioException);
            }
        }
    }

    public boolean isDisplayed(WebElement element) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (TimeoutException e) {
            logger.warn("Timeout: Element not visible");
            return false;
        } catch (NoSuchElementException e) {
            logger.warn("Element not found: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error checking element visibility: {}", e.getMessage());
            return false;
        }
    }

    public static void selectElement(WebElement element, String value) {
        try {
            new Select(element).selectByValue(value);
            logger.info("Selected '{}' from dropdown", value);
        } catch (Exception e) {
            logger.error("Error selecting '{}': {}", value, e.getMessage());
        }
    }

    public static ExpectedCondition<Boolean> visibilityOfElement(final WebElement element) {
        return driver -> {
            try {
                boolean displayed = element.isDisplayed();
                if (displayed) logger.info("Element is displayed");
                return displayed;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        };
    }


    public String getText(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        }
        catch(Exception e) {e.getMessage();}
        return element.getText();
    }
}
