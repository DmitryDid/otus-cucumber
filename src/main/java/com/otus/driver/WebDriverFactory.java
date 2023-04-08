package com.otus.driver;

import com.otus.driver.impl.ChromeWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class WebDriverFactory implements IDriverFactory {

    @Override
    public EventFiringWebDriver getDriver(String browserName) {
        switch (browserName) {
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                return new EventFiringWebDriver(new FirefoxDriver());
            }
            case "opera": {
                WebDriverManager.operadriver().setup();
                return new EventFiringWebDriver(new OperaDriver());
            }
            case "chrome": {
                WebDriverManager.chromedriver().setup();
                return new EventFiringWebDriver(new ChromeWebDriver().newDriver());
            }
            default:
                return null;
        }
    }
}
