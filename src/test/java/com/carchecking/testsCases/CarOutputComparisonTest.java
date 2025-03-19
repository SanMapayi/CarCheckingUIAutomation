package com.carchecking.testsCases;

import com.carchecking.base.TestBase;
import com.carchecking.pages.CarCheckingHomePage;
import com.carchecking.pages.CarOutputComparisonPage;
import data.RegNumberDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.CarInputAndOutputReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CarOutputComparisonTest extends TestBase {

    private final Map<String, List<String>> mapOfExpectedResultOutput = CarInputAndOutputReader.getInstance.vehicleRegMakeModelYearFromOutputFile();

    @Test(dataProviderClass = RegNumberDataProvider.class, dataProvider = "carRegNumbersProvider",
            description = "TC001 - Fetch vehicle details from car valuation website and compare with expected output")
    public void testCompareCarValuationResults(String regNumber) throws IOException, InterruptedException {
        CarCheckingHomePage carCheckingHomePage = new CarCheckingHomePage();
        CarOutputComparisonPage carOutputComparisonPage = new CarOutputComparisonPage();

        logger.info("Starting test for vehicle registration: " + regNumber);

        // Ensure we are on the home page before starting
        driver.get(config.getUrl());
        logger.info("Navigated to home page");

        if (!mapOfExpectedResultOutput.containsKey(regNumber)) {
            logger.warn("Registration number not found in expected results: " + regNumber);
            Assert.fail("Registration number missing in expected output: " + regNumber);
        }

        // Enter reg number & search
        carCheckingHomePage.enterRegNumber(regNumber);
        carCheckingHomePage.clickCheckNowButton();
        logger.info("Clicked 'Check Now' for: " + regNumber);

        if (!carCheckingHomePage.isSearchResultDisplayed()) {
            logger.error("Search result not displayed for: " + regNumber);
            Assert.fail("Search result not displayed for: " + regNumber);
        }

        logger.info("Search result displayed for: " + regNumber);

        // Fetch expected values
        List<String> expectedDetails = mapOfExpectedResultOutput.get(regNumber);
        String expectedMake = expectedDetails.get(0);
        String expectedModel = expectedDetails.get(1);
        String expectedYear = expectedDetails.get(2);

        // Fetch actual values from website
        String actualMake = carOutputComparisonPage.getMake();
        System.out.printf("Actual ----- +  " + actualMake);
        String actualModel = carOutputComparisonPage.getModel();
        System.out.printf("Actual ----- +  " + actualModel);
        String actualYear = carOutputComparisonPage.getYear();
        System.out.printf("Actual ----- +  " + actualYear);

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