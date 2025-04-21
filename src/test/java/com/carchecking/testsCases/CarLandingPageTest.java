package com.carchecking.testsCases;

import com.carchecking.base.TestBase;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;


public class CarLandingPageTest extends TestBase {


    @Test(description = "Reads the car input file and extracts the vehicle registration numbers.",
            testName = "TC001 - testLoadWebPage")
    public void testLoadWebPage() throws MalformedURLException {
        logger.info("Setting up the WebDriver in the landing page test");
        // The URL from the config
        String expectedUrl = config.getUrl();

        // Get current URL of the loaded page
        String currentUrl = new URL(driver.getCurrentUrl()).getProtocol() + "://" + new URL(driver.getCurrentUrl()).getHost();
        if (currentUrl.endsWith("/")) {
            currentUrl = currentUrl.substring(0, currentUrl.length() -1);
        }
        System.out.println(currentUrl + "_".repeat(50));
        wait.until(ExpectedConditions.urlContains("car-checking.com"));


        // Verifying that the current URL is correct (matches the URL from config)
        logger.info("Verifying the page URL");
        Assert.assertEquals( expectedUrl, currentUrl, "URL didn't match!");

        // Verifying the page title
        String pageTitle = driver.getTitle();
        logger.info("Page title: {}", pageTitle);
        Assert.assertTrue( pageTitle.contains("Car Checking"), "Page title is incorrect!");
    }


}