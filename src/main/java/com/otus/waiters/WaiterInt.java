package com.otus.waiters;

import org.openqa.selenium.support.ui.ExpectedCondition;

public interface WaiterInt {

    long IMPLICITLY_WAIT_SECOND = Integer.parseInt(System.getProperty("webdriver.timeouts.implicitlywait", "5000")) / 1000;

    boolean waitForCondition(ExpectedCondition condition);

}
