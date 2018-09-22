package com.forgerock.openbanking.exercise.tpp.ui.view;

import com.forgerock.openbanking.exercise.tpp.ui.SeleniumConfig;
import org.openqa.selenium.By;

public class AMLoginView {

    private SeleniumConfig config;

    public AMLoginView(SeleniumConfig config) {
        this.config = config;
    }

    public void navigate(String url) {
        config.getDriver().get(url);
    }

    public void login(String username, String password) {
        config.getDriver().findElement(By.xpath("//input[@id='idToken1']")).sendKeys(username);
        config.getDriver().findElement(By.xpath("//input[@id='idToken2']")).sendKeys(password);
        config.getDriver().findElement(By.xpath("//input[@id='loginButton_0']")).click();
    }

    public void closeWindow() {
        this.config.getDriver().close();
    }
}
