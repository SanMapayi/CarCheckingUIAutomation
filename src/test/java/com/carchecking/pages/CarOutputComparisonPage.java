package com.carchecking.pages;

import com.carchecking.behaviour.GetMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CarOutputComparisonPage {

    GetMethods getMethods = GetMethods.getInstance;

    @FindBy(xpath = "//table//tr[td[text()='Make']]/td[2]")
    private WebElement make;

    @FindBy(xpath = "//table//tr[td[text()='Model']]/td[2]")
    private WebElement model;

    @FindBy(xpath = "//table//tr[td[text()='Year of manufacture']]/td[2]")
    private WebElement year;

    public String getMake() {
        return getMethods.getText(make);
    }

    public String getModel() {
        return getMethods.getText(model);
    }

    public String getYear() {
        return getMethods.getText(year);
    }
}
