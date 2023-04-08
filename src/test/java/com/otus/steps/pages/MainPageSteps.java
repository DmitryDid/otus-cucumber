package com.otus.steps.pages;

import com.google.inject.Inject;
import com.otus.pages.MainPage;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.Assertions;

public class MainPageSteps {

    @Inject
    public MainPage mainPage;

    @Пусть("Открываем главную страницу")
    public void openMainPageStep() {
        mainPage.open();
    }

    @Тогда("Заголовок главной страницы соответствует {string}")
    public void mainPageHeaderEqualsStep(String text) {
        Assertions.assertEquals(text, mainPage.getHeader());
    }

    @Когда("Нажимаю на кнопку \"Больше курсов\"")
    public void goToMoreCoursesStep() {
        mainPage.clickByMoreCoursesButton();
    }
}
