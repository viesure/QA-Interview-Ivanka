package io.viesure.dimitrova;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features_tests",
        glue = "io.viesure.dimitrova",
        plugin = {"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class WeatherApiTest extends AbstractTestNGCucumberTests {
}