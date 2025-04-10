package com.carchecking.testsCases;

import com.carchecking.base.TestBase;
import core.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.CarInputAndOutputReader;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CarRegValidationTest extends TestBase {


    List<String> listOfExtractedRegNumbers;


    @Test(description = "Checks if the car input file exists, readable, and size.", testName = "TC001 - testFileExistsSizeAndReadable")
    public void testFileExistsSizeAndReadable() throws IOException {

        //Validating that the file exits.
        Assert.assertTrue(Files.exists(Constants.CARINPUTPATH), "Car input file does not exist.");
        //Validating that the file exits.
        Assert.assertTrue(Files.size(Constants.CARINPUTPATH) > 0, "Car input file is empty.");
        //Validating that the file is readable.
        Assert.assertTrue(Files.isReadable(Constants.CARINPUTPATH), "The file is not readable");
    }


    @Test(description = "Reads the car input file and extracts the vehicle registration numbers.",
            testName = "TC002 - testValidateFileContentFormat",
            dependsOnMethods = {"testFileExistsSizeAndReadable"})
    public void testValidateFileContentFormat() {


        listOfExtractedRegNumbers = CarInputAndOutputReader.getInstance.getListOfExtractedPlateNumber();
        // Validating that test of registration number is not empty or null
        Assert.assertFalse(listOfExtractedRegNumbers.isEmpty());
        Assert.assertTrue(Optional.ofNullable(listOfExtractedRegNumbers).isPresent());

        // Validating each plate number format (e.g., AD58 VNF, KT17DLX)
        String plateNumberPattern = "[A-Z]{2}\\d{1,2}\\s?[A-Z]{3}";
        for (String plate : listOfExtractedRegNumbers) {
            Assert.assertTrue(plate.matches(plateNumberPattern), "Invalid plate number format: " + plate);
        }

    }


    @Test(description = "Checks for duplicates in the car input file.",
            testName = "TC003 - testCheckForDuplicates",
            dependsOnMethods = {"testValidateFileContentFormat"})
    public void testCheckForDuplicates() {

        // Assert no duplicates in the list
        Set<String> uniquePlateNumbers = new HashSet<>(listOfExtractedRegNumbers);
        Assert.assertEquals(listOfExtractedRegNumbers.size(), uniquePlateNumbers.size(), "There are duplicates in the car input file.");
    }


    @Test(description = "Validates if the extracted registration numbers match the expected values.",
            testName = "TC004 - testExtractedRegistrationNumbers",
            dependsOnMethods = {"testValidateFileContentFormat"})
    public void testExtractedRegistrationNumbers() {


        Assert.assertTrue(listOfExtractedRegNumbers.contains("AD58 VNF"));
        Assert.assertTrue(listOfExtractedRegNumbers.contains("BW57 BOW"));
        Assert.assertTrue(listOfExtractedRegNumbers.contains("KT17DLX"));
        Assert.assertTrue(listOfExtractedRegNumbers.contains("SG18 HTN"));

    }
}