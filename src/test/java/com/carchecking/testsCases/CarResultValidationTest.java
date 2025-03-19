package com.carchecking.testsCases;

import com.carchecking.base.TestBase;
import com.carchecking.pages.CarCheckingHomePage;
import data.RegNumberDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.CarInputAndOutputReader;

import java.io.IOException;
import java.util.List;

public class CarResultValidationTest extends TestBase {

    List<String> listOfExtractedRegNumbers;

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
        CarCheckingHomePage carCheckingHomePage = new CarCheckingHomePage();

        carCheckingHomePage.enterRegNumber(regNumber);
        carCheckingHomePage.clickCheckNowButton();

        if (carCheckingHomePage.isSearchResultDisplayed()) {
            Assert.assertTrue(true, "Search result displayed for: " + regNumber);
            carCheckingHomePage.clickHomePageLink();
        } else {
            logger.info("Search result is NOT displayed for: " + regNumber);
            if (carCheckingHomePage.isAlertMessageDisplayed()) {
                logger.info("You must wait a bit longer before generating a new report for " + regNumber);
                Assert.fail("No result for registration number: " + regNumber);
            }
        }
    }
}

