package com.otus.waiters;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class StandardWaiter implements WaiterInt {

    private final WebDriver driver;
    private final Actions actions;

    public StandardWaiter(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    public WebElement waitElement(By by) {
        WebElement element;
        int count = 0;

        while (true) {
            if (count == 100) {
                return null;
            }
            try {
                element = driver.findElement(by);
                waitForElementVisible(element);
                waitForElementClickable(element);
                actions.moveToElement(element).perform();
            } catch (MoveTargetOutOfBoundsException | NoSuchElementException ex) {
                easySleep(100);
                count++;
                continue;
            }
            return element;
        }
    }

    public WebElement waitElement(WebElement element) {
        int count = 0;
        while (true) {
            if (count == 100) {
                return null;
            }
            waitForElementVisible(element);
            waitForElementClickable(element);
            try {
                actions.moveToElement(element).perform();
            } catch (MoveTargetOutOfBoundsException e) {
                easySleep(100);
                count++;
                continue;
            }
            return element;
        }
    }

    public List<WebElement> waitElements(By by) {
        List<WebElement> elements;
        int count = 0;

        while (true) {
            if (count == 100) {
                Assertions.fail("Ошибка! Не удалось дождаться элемента!");
            }
            elements = driver.findElements(by);
            if (elements.size() == 0) {
                easySleep(100);
                count++;
                continue;
            }
            waitForElementVisible(elements.get(0));
            waitForElementClickable(elements.get(0));
            try {
                actions.moveToElement(elements.get(0)).perform();
            } catch (MoveTargetOutOfBoundsException | StaleElementReferenceException ex) {
                easySleep(100);
                count++;
                continue;
            }
            return elements;
        }
    }

    @Override
    public boolean waitForCondition(ExpectedCondition condition) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, IMPLICITLY_WAIT_SECOND);
        try {
            webDriverWait.until(condition);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean waitForElementClickable(WebElement element) {
        return waitForCondition(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitForElementVisible(WebElement element) {
        return waitForCondition(ExpectedConditions.visibilityOf(element));
    }

    public void easySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}