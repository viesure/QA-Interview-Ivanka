package io.viesure.dimitrova.weatherapi;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features_tests/weather_test.feature",
        glue = "io.viesure.dimitrova.weatherapi",
        plugin = {"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class WeatherApiTest extends AbstractTestNGCucumberTests {
}