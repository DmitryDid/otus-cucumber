package com.otus.pages;

import com.google.inject.Inject;
import com.otus.annotations.UrlPrefix;
import com.otus.di.GuiceScoped;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@UrlPrefix("/")
public class MainPage extends AbsBasePage<MainPage> {

    @Inject
    public MainPage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    @FindBy(css = ".transitional-main__courses-more")
    WebElement moreCoursesButton;

    String agreeButtonLocator = "div[class=\"cookies\"] button";

    public MainPage agreeCookies() {
        standardWaiter.waitElement(By.cssSelector(agreeButtonLocator))
                .click();
        return this;
    }

    public MainPage clickByMoreCoursesButton() {
        standardWaiter.waitElement(moreCoursesButton)
                .click();
        return this;
    }
}
