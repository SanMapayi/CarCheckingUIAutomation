package com.carchecking.testsCases;

import com.carchecking.base.ITestLifecycleBase;
import com.carchecking.base.TestBase;
import com.carchecking.pages.CarCheckingHomePage;
import data.RegNumberDataProvider;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.CarInputAndOutputReader;

import java.io.IOException;
import java.util.List;

public class CarResultValidationTest extends TestBase implements ITestLifecycleBase {

    private List<String> listOfExtractedRegNumbers;
    private CarCheckingHomePage carCheckingHomePage;


    @Test(description = "Extracts vehicle registration numbers from the input file.", testName = "TC001 - testExtractRegistrationNumbers")
    public void testExtractRegistrationNumbers() {
        listOfExtractedRegNumbers = CarInputAndOutputReader.getInstance.getListOfExtractedPlateNumber();
        Assert.assertFalse(listOfExtractedRegNumbers.isEmpty(), "No registration numbers found in input file.");
    }

    @Test(dataProviderClass = RegNumberDataProvider.class, dataProvider = "carRegNumbersProvider",
            description = "Enters each extracted registration number into the car valuation website and verifies search execution.",
            testName = "TC002 - testEnterPlateNumberInCarValuationPage",
            dependsOnMethods = {"testExtractRegistrationNumbers"})
    public void testEnterPlateNumberInCarValuationPage(String regNumber) throws IOException, InterruptedException {
        carCheckingHomePage = new CarCheckingHomePage();
        logger.info("Starting test for vehicle registration: " + regNumber);

        // Ensure we are on the home page before starting
        driver.get(config.getUrl());
        logger.info("Navigated to home page");

        // Enter registration number and click the check button
        carCheckingHomePage.enterRegNumber(regNumber);
        carCheckingHomePage.clickCheckNowButton();

        // Check if search results are displayed using findElements to avoid NoSuchElementException
        boolean isSearchable = !driver.findElements(By.xpath("//div[@id='content']//div[contains(@class, 'content-holder')]//div[contains(@class, 'report-header')]")).isEmpty();

        if (!isSearchable) {
            logger.warn("Search result is NOT displayed for: " + regNumber);

            if (carCheckingHomePage.isAlertMessageDisplayed()) {
                logger.info("Licence plate number is not recognised " + regNumber);
            } else {
                logger.error("No alert message found; possible unexpected issue, may be a 500 error.");
            }
        } else if (isSearchable){

            logger.info("Search successful, returning to home page.");
            carCheckingHomePage.clickHomePageLink(); // Navigate back to the home page
        }

        Assert.assertTrue(isSearchable, "Search result is NOT displayed for " + regNumber);
    }
}