package com.carchecking.pages;

import com.carchecking.behaviour.GetMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CarOutputComparisonPage {

    GetMethods getMethods = GetMethods.getInstance;

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Make')]]//td[@class='td-right']")
    private WebElement make;

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Model')]]//td[@class='td-right']")
    private WebElement model;

    @FindBy(xpath = "//table//tr[td[contains(text(), 'Year of manufacture')]]")
    private WebElement year;

    public String getMake() {
        return getMethods.getDetailValueByLabel("Make");
    }

    public String getModel() {
        return getMethods.getDetailValueByLabel("Model");
    }

    public String getYear() {
        return getMethods.getDetailValueByLabel("Year");
    }
}
