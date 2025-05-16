package com.carchecking.base;

import org.testng.annotations.BeforeMethod;

import java.io.IOException;

public interface ITestLifecycleBase {

    @BeforeMethod
    default void setUp() throws IOException, InterruptedException {
        // Reinitialize WebDriver only if it's null

        if (TestBase.driver == null) {
            TestBase.logger.info("Driver is NULL");
            String browserToUse = TestBase.browserToUse != null
                    ? TestBase.browserToUse
                    : TestBase.config.getBrowser();
            TestBase.logger.info("Browser from testNg used interface [{}]", TestBase.browserToUse);
            TestBase.driver = TestBase.initialize(browserToUse); // Initialize the driver if it's not already initialized
        }
    }



}