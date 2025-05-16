package com.carchecking.testsCases;

import com.carchecking.base.ITestLifecycleBase;
import com.carchecking.base.TestBase;
import com.carchecking.pages.CarCheckingHomePage;
import com.carchecking.pages.CarOutputComparisonPage;
import data.RegNumberDataProvider;
import org.testng.annotations.Test;
import utilities.CarInputAndOutputReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CarOutputComparisonTest extends TestBase implements ITestLifecycleBase {
    private CarCheckingHomePage carCheckingHomePage;
    private CarOutputComparisonPage carOutputComparisonPage;
    private Map<String, List<String>> mapOfExpectedResultOutput;


    @Test(dataProviderClass = RegNumberDataProvider.class, dataProvider = "carRegNumbersProvider",
            description = "TC001 - Fetch vehicle details from car valuation website and compare with expected output")
    public void testCompareCarValuationResults(String regNumber) throws IOException, InterruptedException {
        carCheckingHomePage = new CarCheckingHomePage();
        carOutputComparisonPage = new CarOutputComparisonPage();
        mapOfExpectedResultOutput = CarInputAndOutputReader.getInstance.vehicleRegMakeModelYearFromOutputMap();
        logger.info("Starting test for vehicle registration: " + regNumber);

        // Ensure we are on the home page before starting
        driver.get(config.getUrl());
        logger.info("Navigated to home page in test [{}], and testing for '{}'", Thread.currentThread().getStackTrace()[1].getMethodName(), regNumber);

        // Check if output file contains reg number from the input file
        carOutputComparisonPage.checkIfMapContainsKeys(regNumber, mapOfExpectedResultOutput);

        // And then return the list of expected values from the output file (Make, Model, year)
        List<String> expectedDetails = carOutputComparisonPage.getAndCheckIfRegNumberIsPresent(mapOfExpectedResultOutput, regNumber);

        // Enter reg number & search
        carOutputComparisonPage.enterRegAndSearchPage(regNumber, carCheckingHomePage, carOutputComparisonPage);

        // Fetching actual values from website using JS and asserting for make, model, and year and compare with expected values
        carOutputComparisonPage.fetchingExpectedValueAndValidationTest(carOutputComparisonPage, expectedDetails.get(0),
                expectedDetails.get(1), expectedDetails.get(2), regNumber);
    }
}