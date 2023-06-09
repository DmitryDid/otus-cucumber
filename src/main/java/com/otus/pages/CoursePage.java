package com.otus.pages;

import com.google.inject.Inject;
import com.otus.annotations.UrlPrefix;
import com.otus.di.GuiceScoped;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@UrlPrefix("/")
public class CoursePage extends AbsBasePage<CoursePage> {

    @Inject
    public CoursePage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    @FindBy(css = ".course-teacher-items .course-teacher-item__box")
    List<WebElement> teachers;

    String agreeButtonSelector = "button[class*=\"cookies__button\"]";
    String teacherNameSelector = "div[class=\"course-teacher js-teacher\"] .course-teacher__name";

    public CoursePage clickByRandomTeacher() {
        Assertions.assertTrue(teachers.size() > 0);

        standardWaiter.waitElement(teachers.get(RANDOM.nextInt(teachers.size() - 2) + 1))
                .click();

        return this;
    }

    public CoursePage agreeCookies() {
        standardWaiter.waitElement(By.cssSelector(agreeButtonSelector))
                .click();
        return this;
    }

    public void printChooseTeacherName() {
        System.out.println(standardWaiter.waitElement(By.cssSelector(teacherNameSelector)).getText());
    }
}
