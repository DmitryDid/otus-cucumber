package com.otus.di;

import io.cucumber.guice.ScenarioScoped;
import org.openqa.selenium.support.events.EventFiringWebDriver;

@ScenarioScoped
public class GuiceScoped {

    public EventFiringWebDriver driver;

}
