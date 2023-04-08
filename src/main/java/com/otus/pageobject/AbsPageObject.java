package com.otus.pageobject;

import com.google.inject.Inject;
import com.otus.di.GuiceScoped;
import com.otus.waiters.StandardWaiter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

public abstract class AbsPageObject {

    protected WebDriver driver;
    protected Actions action;
    protected StandardWaiter standardWaiter;

    @Inject
    public AbsPageObject(GuiceScoped guiceScoped) {
        this.driver = guiceScoped.driver;
        this.action = new Actions(driver);
        this.standardWaiter = new StandardWaiter(driver);
        PageFactory.initElements(driver, this);
    }

    protected void log(String text) {
        System.out.println(text);
    }

    protected void log() {
        log("");
    }
}
