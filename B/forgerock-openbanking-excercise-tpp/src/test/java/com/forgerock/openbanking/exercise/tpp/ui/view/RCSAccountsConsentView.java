package com.forgerock.openbanking.exercise.tpp.ui.view;

import com.forgerock.openbanking.exercise.tpp.ui.SeleniumConfig;
import org.openqa.selenium.By;

public class RCSAccountsConsentView {

    private SeleniumConfig config;

    public RCSAccountsConsentView(SeleniumConfig config) {
        this.config = config;
    }

    public void allow() {
        config.getDriver().findElement(By.xpath("//*[contains(text(), 'Allow')]")).click();
    }

}
