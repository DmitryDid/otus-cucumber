package com.otus.pages;

import com.google.inject.Inject;
import com.otus.annotations.UrlPrefix;
import com.otus.di.GuiceScoped;
import com.otus.pageobject.AbsPageObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public abstract class AbsBasePage<T> extends AbsPageObject {

    @Inject
    public AbsBasePage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    protected static final Random RANDOM = new Random();

    public T open() {
        driver.get(getBaseUrl() + getUrlPrefix());
        return (T) page(getClass());
    }

    public String getHeader() {
        return driver.findElement(By.cssSelector("h1")).getText();
    }

    public T complexClick(WebElement element) {
        standardWaiter.waitForElementVisible(element);
        standardWaiter.waitForElementClickable(element);
        action.moveToElement(element).perform();
        element.click();
        return (T) this;
    }

    private <T> T page(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor(WebDriver.class);
            return clazz.cast(constructor.newInstance(driver));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                 | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getBaseUrl() {
        return StringUtils.stripEnd(System.getProperty("webdriver.base.url"), "/");
    }

    private String getUrlPrefix() {
        UrlPrefix urlAnnotation = getClass().getAnnotation(UrlPrefix.class);
        if (urlAnnotation != null) {
            return urlAnnotation.value();
        }
        return "";
    }
}
