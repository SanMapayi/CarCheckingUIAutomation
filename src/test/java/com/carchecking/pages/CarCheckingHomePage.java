package com.carchecking.pages;

import com.carchecking.base.TestBase;
import com.carchecking.behaviour.ActionMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;

public class CarCheckingHomePage extends TestBase {

    ActionMethods actionMethods = ActionMethods.getInstance;

    @FindBy(id = "subForm1")
    private WebElement enterRegTextField;


    @FindBy(xpath = "//button[contains(text(), 'Check Now')]")
    private WebElement checkNowButton;

    @FindBy(xpath = "//div[@id='content']//div[contains(@class, 'content-holder')]//div[contains(@class, 'report-header')]")
    private WebElement resultContainer;

    @FindBy(className = "btn-primary")
    private WebElement findOutMoreLink;

    @FindBy(xpath = "//a[@href='/home']")
    private WebElement homeLink;

    @FindBy(className = "alert-danger")
    private WebElement alertMessage;


    public CarCheckingHomePage() {
        PageFactory.initElements(driver, this);
    }


    public void enterRegNumber(String regNum) {
        actionMethods.enterText(enterRegTextField, regNum,
                Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    public void clickCheckNowButton() throws IOException {
        actionMethods.click(checkNowButton,
                Thread.currentThread().getStackTrace()[1].getMethodName());
    }


    public boolean isSearchResultDisplayed() {
      return actionMethods.isDisplayed(resultContainer);
    }

    public void clickHomePageLink() throws IOException {
        actionMethods.click(homeLink,
                Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    public boolean isAlertMessageDisplayed() {
        return actionMethods.isDisplayed(alertMessage);
    }
}
