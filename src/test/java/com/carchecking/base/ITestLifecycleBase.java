package com.carchecking.base;

import org.testng.annotations.BeforeMethod;

import java.io.IOException;

public interface ITestLifecycleBase {

    @BeforeMethod
    default void setUp() throws IOException {
        // Reinitialize WebDriver only if it's null
        if (TestBase.driver == null) {
            TestBase.driver = TestBase.initialize(); // Initialize the driver if it's not already initialized
        }
    }



}