package com.carchecking.testsCases;

import com.carchecking.base.TestBase;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CarLandingPageTest extends TestBase {


    @Test(description = "Reads the car input file and extracts the vehicle registration numbers.",
            testName = "TC001 - testLoadWebPage")
    public void testLoadWebPage() {
        logger.info("Setting up the WebDriver in the landing page test");
        // The URL from the config
        String expectedUrl = config.getUrl();

        // Get current URL of the loaded page
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.endsWith("/")) {
            currentUrl = currentUrl.substring(0, currentUrl.length() -1);
        }

        // Verifying that the current URL is correct (matches the URL from config)
        logger.info("Verifying the page URL");
        assertEquals("URL didn't match!", expectedUrl, currentUrl);

        // Verifying the page title
        String pageTitle = driver.getTitle();
        System.out.println(pageTitle);
        logger.info("Page title: {}", pageTitle);
        assertTrue("Page title is incorrect!", pageTitle.contains("Car Checking"));
    }


}