package com.carchecking.pages;

import com.carchecking.base.TestBase;
import com.carchecking.behaviour.ActionMethods;
import com.carchecking.behaviour.GetMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CarOutputComparisonPage extends TestBase {

    GetMethods getMethods = GetMethods.getInstance;
    ActionMethods actionMethods = ActionMethods.getInstance;


    public CarOutputComparisonPage() {
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Make')]]//td[@class='td-right']")
    private WebElement make;

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Model')]]//td[@class='td-right']")
    private WebElement model;

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Year of manufacture')]]//td[@class='td-right']")
    private WebElement year;

    @FindBy(xpath = "//div[@Class ='alert alert-danger'][contains(text(), recognised)]")
    private WebElement alertDanger;

    public String getMake() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(make));
        return getMethods.getDetailValueByLabel("Make", make);
    }

    public String getModel() {
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        getWait().until(ExpectedConditions.visibilityOf(model));
        return getMethods.getDetailValueByLabel("Model", model);
    }

    public String getYear() {
        getWait().until(ExpectedConditions.visibilityOf(year));
        return getMethods.getDetailValueByLabel("Year", year);
    }

    public void enterRegNumberAndSearch(String regNumber, CarCheckingHomePage carCheckingHomePage) throws IOException {
        carCheckingHomePage.enterRegNumber(regNumber);
        carCheckingHomePage.clickCheckNowButton();
        logger.info("Clicked 'Check Now' for: " + regNumber);
    }

    public void checkIfResultIsNotDisplayed(String regNumber, CarCheckingHomePage carCheckingHomePage) throws IOException {

       boolean isSearchResultDisplayed = carCheckingHomePage.isSearchResultDisplayed();
        if (!isSearchResultDisplayed) {
            logger.error("Search result not displayed for: " + regNumber);
            boolean resultPageIsLoaded = actionMethods.isDisplayed(alertDanger);
            if(resultPageIsLoaded) {
                logger.info("Licence plate number is not recognised");
                Assert.fail("Licence plate number is not recognised" + regNumber);
            }
            //captureScreen("testName");
            Assert.fail("Search result not displayed for: a possible server(5000 error) " + regNumber);
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


    public List<String> getAndCheckIfRegNumberIsPresent(Map<String, List<String>> mapOfExpectedResultOutput, String regNumber) {
        List<String> expectedDetails = mapOfExpectedResultOutput.get(regNumber);
        if (expectedDetails == null) {
            logger.info("The list [{}] for registration [{}] is null", expectedDetails, regNumber);
            logger.info("reg number [{}] is not present in the list", regNumber);
            Assert.fail("The reg number %s is not present in the output file".formatted(regNumber));
        }

        return expectedDetails;
    }



}
