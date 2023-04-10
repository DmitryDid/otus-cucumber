package com.otus.pages;

import com.google.inject.Inject;
import com.otus.annotations.Component;
import com.otus.data.ConditionData;
import com.otus.di.GuiceScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component("/catalog/courses?categories=programming")
public class CatalogPage extends AbsBasePage<CatalogPage> {

    @Inject
    public GuiceScoped guiceScoped;

    @Inject
    public CatalogPage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    @FindBy(css = "input[type=\"search\"]")
    WebElement filter;

    String filteredLessonsSelector = "section a[href][class*=\"sc-1pyq5ti-0\"]";
    String startLessonsDateSelector = "div[class*=\"jDZyxI icwxwp-1\"] div";
    String filteredLessonsNamesSelector = " h6 div";
    String lessonsNamesSelector = filteredLessonsSelector + filteredLessonsNamesSelector;
    String agreeButtonSelector = "button[type]";

    String jsoupCostXPath = "//nobr[contains(text(), \"₽\")]";
    String jsoupCostXPathAlternative = "//div[@class=\"tn-atom\" and contains(text(), \"₽\" ) and not(sub)]";

    public CatalogPage agreeCookies() {
        standardWaiter.waitElement(By.cssSelector(agreeButtonSelector))
                .click();
        return this;
    }

    public CatalogPage filterCoursesByText(String text) {
        standardWaiter.waitElement(filter).sendKeys(text);

        List<WebElement> namesElements = null;
        int count = 0;
        do {
            if (count > 20) {
                Assertions.fail("ERROR! Courses after filtering are less than the test suggests.");
            }
            try {
                namesElements = standardWaiter.waitElements(By.cssSelector(lessonsNamesSelector)).stream()
                        .filter(e -> e.getText().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))
                        .collect(Collectors.toList());
            } catch (StaleElementReferenceException e) {
                standardWaiter.easySleep(100);
                continue;
            }
            count++;
        } while (namesElements.size() < 1);

        return this;
    }

    public CatalogPage printCoursesNameByStartDate(String startDate) {
        LocalDate date = parseDate(startDate);

        while (true) {
            try {
                standardWaiter.waitElements(By.cssSelector(filteredLessonsSelector)).stream()
                        .filter(e -> {
                            LocalDate elementDate;
                            try {
                                elementDate = parseDate(e.findElement(By.cssSelector(startLessonsDateSelector)).getText());
                            } catch (NoSuchElementException e1) {
                                return false;
                            }
                            if (elementDate == null) {
                                return false;
                            } else {
                                return elementDate.isAfter(date) || elementDate.equals(date);
                            }
                        })
                        .peek(e -> {
                            System.out.printf("%s - %s%n",
                                    e.findElement(By.cssSelector(lessonsNamesSelector)).getText(),
                                    e.findElement(By.cssSelector(startLessonsDateSelector)).getText());

                        })
                        .collect(Collectors.toList());
            } catch (StaleElementReferenceException ex) {
                standardWaiter.easySleep(100);
                continue;
            }
            return this;
        }
    }

    public CoursePage chooseCourseByCondition(ConditionData condition) {
        List<WebElement> coursesElements = standardWaiter.waitElements(By.cssSelector(filteredLessonsSelector));
        Map<LocalDate, WebElement> coursesMap = new HashMap<>();
        LocalDate startDate = null;

        for (WebElement element : coursesElements) {
            try {
                startDate = parseDate(element.findElement(By.cssSelector(startLessonsDateSelector)).getText());
            } catch (NoSuchElementException ignore) {
                continue;
            }
            if (startDate != null)
                coursesMap.put(startDate, element);
        }

        if (condition.equals(ConditionData.LATEST)) {
            startDate = coursesMap.keySet().stream()
                    .reduce((last, current) -> current.isAfter(last) ? current : last)
                    .get();
        }
        if (condition.equals(ConditionData.EARLIEST)) {
            startDate = coursesMap.keySet().stream()
                    .reduce((first, current) -> current.isBefore(first) ? current : first)
                    .get();
        }

        standardWaiter.waitElement(coursesMap.get(startDate))
                .click();

        return new CoursePage(guiceScoped);
    }

    public void printInfoAboutCourseByCondition(boolean isExpensive) {
        List<WebElement> courses = standardWaiter.waitElements(By.cssSelector(filteredLessonsSelector));
        Map<Integer, WebElement> costAndCourses = getJsoupInfo(courses);
        Integer cost;
        if (isExpensive) {
            cost = costAndCourses.keySet().stream()
                    .reduce((last, current) -> current > last ? current : last)
                    .get();
        } else {
            cost = costAndCourses.keySet().stream()
                    .reduce((last, current) -> current < last ? current : last)
                    .get();
        }
        WebElement element = costAndCourses.get(cost);
        System.out.printf("%s - %s%n", element.findElement(By.cssSelector(filteredLessonsNamesSelector)).getText(), cost);
    }

    private Map<Integer, WebElement> getJsoupInfo(List<WebElement> elements) {
        List<WebElement> courses = standardWaiter.waitElements(By.cssSelector(filteredLessonsSelector));
        Map<Integer, WebElement> result = new HashMap<>();

        for (int i = 0; i < courses.size(); i++) {
            String href = courses.get(i).getAttribute("href");
            Elements newsHeadlines;
            Document doc;
            int cost;

            try {
                doc = Jsoup.connect(href).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newsHeadlines = doc.selectXpath(jsoupCostXPath);

            if (newsHeadlines.size() == 0) {
                newsHeadlines = doc.selectXpath(jsoupCostXPathAlternative);
            }
            if (newsHeadlines.size() == 0) {
                continue;
            }

            cost = Integer.parseInt(newsHeadlines.get(0).ownText().replaceAll("\\D", ""));
            result.put(cost, courses.get(i));
        }
        return result;
    }

    private LocalDate parseDate(String data) {
        data = data.split(" · ")[0].replace(",", "");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("ru"));
            return LocalDate.parse(data, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}