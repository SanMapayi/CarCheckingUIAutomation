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
    public final CarCheckingHomePage carCheckingHomePage = new CarCheckingHomePage();
    public final CarOutputComparisonPage carOutputComparisonPage = new CarOutputComparisonPage();

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

        // check if output file contains reg number from the input file
        carOutputComparisonPage.checkIfMapContainsKeys(regNumber, mapOfExpectedResultOutput);

        // Enter reg number & search
        carOutputComparisonPage.enterRegAndSearchPage(
                regNumber, carCheckingHomePage, carOutputComparisonPage
        );

        // Fetch expected values
        List<String> expectedDetails = mapOfExpectedResultOutput.get(regNumber);
        String expectedMake = expectedDetails.get(0);
        String expectedModel = expectedDetails.get(1);
        String expectedYear = expectedDetails.get(2);

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