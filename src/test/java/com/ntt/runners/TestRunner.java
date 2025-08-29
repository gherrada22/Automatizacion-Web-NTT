package com.ntt.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.ntt.stepdefinitions",
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        tags = "not @wip" // Ejecuta todos los escenarios que no tengan la etiqueta @wip (trabajo en progreso)
)
public class TestRunner {

}