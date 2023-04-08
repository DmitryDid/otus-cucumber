package com.otus.steps.pages;

import com.google.inject.Inject;
import com.otus.pages.CoursePage;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;

public class CoursePageStep {

    @Inject
    public CoursePage coursePage;

    @Тогда("Кликаем по плитке с одним из преподавателей")
    public void clickByRandomTeacherStep() {
        coursePage.clickByRandomTeacher();
    }

    @И("Принимаем куки на странице курса")
    public void agreeCookiesStep() {
        coursePage.agreeCookies();
    }

    @Тогда("Выводим имя выбранного преподавателя")
    public void printChooseTeacherNameStep() {
        coursePage.printChooseTeacherName();
    }
}
