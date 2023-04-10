package com.otus.steps.common;

import com.google.inject.Inject;
import com.otus.di.GuiceScoped;
import com.otus.driver.WebDriverFactory;
import com.otus.listeners.MouseListener;
import com.otus.pages.AbsBasePage;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.Assertions;

public class CommonSteps {

    @Inject
    public GuiceScoped guiceScoped;

    @Дано("Открыт браузер {string}")
    public void openBrowser(String browserName) {
        guiceScoped.driver = new WebDriverFactory().getDriver(browserName);
        guiceScoped.driver.register(new MouseListener());
    }

    @Пусть("Открываем страницу {string}")
    public void openPage(String pageName) {
        guiceScoped.driver.get(pageName);
    }

    @Тогда("Заголовок страницы соответствует {string}")
    public void headerPageEquals(String header) {
        Assertions.assertEquals(
                header,
                new AbsBasePage<AbsBasePage>(guiceScoped) {
                }.getHeader()
        );
    }
}
