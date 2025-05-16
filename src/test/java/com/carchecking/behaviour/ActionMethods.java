package com.carchecking.behaviour;


import com.carchecking.base.TestBase;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;





public class ActionMethods extends TestBase {


    public static final ActionMethods getInstance = new ActionMethods();

    private ActionMethods() {};
    public void enterText(WebElement element, String value, String featureName)
    {
        try {
            getWait().until(ExpectedConditions.visibilityOf(element));
            element.sendKeys(value);
            logger.info(value +" "+ "is Entered");
        }

        catch (Exception e){
            logger.error(e.getMessage() + "  "  + featureName);
        }

    }

    public void click(WebElement element, String featureName) throws IOException {
        if (isDisplayed(element) == true) {
            try {
                getWait().until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                logger.info("A click function is performed on " + element);
            }
            catch (Exception e) {System.out.println(e.getMessage());
                captureScreen(featureName);}
        }

    }

    public boolean isDisplayed(WebElement element)
    {
        try {
            getWait().until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();

        }
        catch (Exception e) {
            logger.error("[{}] is not displayed and error [{}] thrown", element, e.getMessage());
            return false;
        }

    }
    public void SelectElement(WebElement element, String Value)
    {
        new Select(element).selectByValue(Value);
    }

    public ExpectedCondition<Boolean> visibilityOfElement(final WebElement element){
        return  new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    logger.info("The element is displayed");
                    return element.isDisplayed();
                } catch (NoSuchElementException e) {
                    logger.error("No Such element here! and error " + e.getMessage() );
                    return false;}
                catch (StaleElementReferenceException el) {
                    return false;
                }
            }
        };

    }
    public static ExpectedCondition<Boolean> wait(final boolean condition){
        return  new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return condition;
            }

        };

    }
}
