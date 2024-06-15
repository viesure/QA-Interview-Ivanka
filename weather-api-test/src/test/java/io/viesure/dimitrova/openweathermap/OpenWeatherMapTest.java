package io.viesure.dimitrova.openweathermap;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features_tests/openweathermap_page_test.feature",
        glue = "io.viesure.dimitrova.openweathermap",
        plugin = {"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class OpenWeatherMapTest extends AbstractTestNGCucumberTests {
}