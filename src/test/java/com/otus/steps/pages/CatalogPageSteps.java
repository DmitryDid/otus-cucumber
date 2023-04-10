package com.otus.steps.pages;

import com.google.inject.Inject;
import com.otus.data.ConditionData;
import com.otus.pages.CatalogPage;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.Assertions;

public class CatalogPageSteps {

    @Inject
    public CatalogPage catalogPage;

    @Когда("Фильтруем курсы по тексту {string}")
    public void goToMoreCoursesStep(String text) {
        catalogPage.filterCoursesByText(text);
    }

    @Тогда("Выводим в консоль названия курсов стартующих c даты: {string}")
    public void printCoursesNameStep(String startDate) {
        System.out.printf("Курсы стартующие с даты %s:%n", startDate);
        catalogPage.printCoursesNameByStartDate(startDate);
    }

    @Тогда("Выводим в консоль информацию о самом {string} курсе")
    public void printCourseInfoByCostStep(String cost) {
        switch (cost) {
            case "дорогом": {
                System.out.println("Информация о самом дорогом курсе:");
                catalogPage.printInfoAboutCourseByCondition(true);
                break;
            }
            case "дешевом": {
                System.out.println("Информация о самом дешевом курсе:");
                catalogPage.printInfoAboutCourseByCondition(false);
                break;
            }
            default:
                Assertions.fail(String.format("Ошибка! Не распознан признак стоимости %s/. Допустимо: дорогом, дешевом", cost));
        }
    }

    @Когда("Выбираем самый ранний курс")
    public void chooseEarlyCourseStep() {
        catalogPage.chooseCourseByCondition(ConditionData.EARLIEST);
    }

    @Когда("Выбираем самый поздний курс")
    public void chooseLatestCourseStep() {
        catalogPage.chooseCourseByCondition(ConditionData.LATEST);
    }

    @Тогда("Принимаем куки на странице каталога")
    public void agreeCookiesStep() {
        catalogPage.agreeCookies();
    }
}
