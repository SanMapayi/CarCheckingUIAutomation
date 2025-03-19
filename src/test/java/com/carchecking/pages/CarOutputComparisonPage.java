package com.carchecking.pages;

import com.carchecking.base.TestBase;
import com.carchecking.behaviour.GetMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CarOutputComparisonPage extends TestBase {

    GetMethods getMethods = GetMethods.getInstance;

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Make')]]//td[@class='td-right']")
    private WebElement make;

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Model')]]//td[@class='td-right']")
    private WebElement model;

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Year of manufacture')]]")
    private WebElement year;

    public String getMake() {
        return getMethods.getDetailValueByLabel("Make");
    }

    public String getModel() {
        return getMethods.getDetailValueByLabel("Model");
    }

    public String getYear() {
        return getMethods.getDetailValueByLabel("Year");
    }

    public void enterRegNumberAndSearch(String regNumber, CarCheckingHomePage carCheckingHomePage) throws IOException {
        carCheckingHomePage.enterRegNumber(regNumber);
        carCheckingHomePage.clickCheckNowButton();
        logger.info("Clicked 'Check Now' for: " + regNumber);
    }

    public void checkIfResultIsNotDisplayed(String regNumber, CarCheckingHomePage carCheckingHomePage) {
        if (!carCheckingHomePage.isSearchResultDisplayed()) {
            logger.error("Search result not displayed for: " + regNumber);
            Assert.fail("Search result not displayed for: " + regNumber);
        }
    }

    public void checkIfMapContainsKeys
            (String regNumber, Map<String, List<String>> mapOfExpectedResultOutput) {
        if (!mapOfExpectedResultOutput.containsKey(regNumber)) {
            logger.warn("Registration number not found in expected results: " + regNumber);
            Assert.fail("Registration number missing in expected output: " + regNumber);
        }
    }

    public void enterRegAndSearchPage
            (String regNumber, CarCheckingHomePage carCheckingHomePage, CarOutputComparisonPage carOutputComparisonPage) throws IOException {
        carOutputComparisonPage.enterRegNumberAndSearch(regNumber, carCheckingHomePage);
        carOutputComparisonPage.checkIfResultIsNotDisplayed(regNumber, carCheckingHomePage);
        logger.info("Search result displayed for: " + regNumber);
    }

    public void fetchingExpectedValueAndValidationTest(CarOutputComparisonPage carOutputComparisonPage,
                                                       String expectedMake, String expectedModel, String expectedYear, String regNumber) {
        // Fetch actual values from website
        String actualMake = carOutputComparisonPage.getMake();
        String actualModel = carOutputComparisonPage.getModel();
        String actualYear = carOutputComparisonPage.getYear();

        logger.info("Comparing expected vs. actual results:");
        logger.info("Make: Expected [{}] vs Actual [{}]", expectedMake, actualMake);
        logger.info("Model: Expected [{}] vs Actual [{}]", expectedModel, actualModel);
        logger.info("Year: Expected [{}] vs Actual [{}]", expectedYear, actualYear);

        // Assertions
        Assert.assertEquals(actualMake, expectedMake, "Make mismatch for " + regNumber);
        Assert.assertEquals(actualModel, expectedModel, "Model mismatch for " + regNumber);
        Assert.assertEquals(actualYear, expectedYear, "Year mismatch for " + regNumber);
    }

}
