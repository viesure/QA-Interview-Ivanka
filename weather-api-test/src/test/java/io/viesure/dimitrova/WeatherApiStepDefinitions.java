package io.viesure.dimitrova;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.viesure.dimitrova.response.WeatherResponse;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

public class WeatherApiStepDefinitions {

    private static final String BASE_URL =
            "https://backend-interview.tools.gcp.viesure.io/weather";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private WeatherResponse weatherResponse;


    @Given("Fahrenheit temperature is set with value {int}")
    public void setFahrenheitTemp(Integer fahrenheitTemp) throws IOException {
        Response putFahrenheitResp = Request.put(BASE_URL.concat("/temp"))
                .bodyString("{\"tempInFahrenheit\": " + fahrenheitTemp + " }", ContentType.APPLICATION_JSON)
                .execute();
        assertEquals(putFahrenheitResp.returnResponse().getCode(), 200,
                "Unable to set weather fahrenheit temperature.");
    }

    @Given("Celsius temperature value is {int}")
    public void setCelsiusTemp(Integer celsiusTemp) throws IOException {
        Integer fahrenheitTemp = Math.round((Float.valueOf(celsiusTemp) * 9 / 5) + 32);
        setFahrenheitTemp(fahrenheitTemp);
    }

    @Given("condition id is set with value {int}")
    public void setConditionId(Integer conditionId) throws IOException {
        Response putConditionIdResp = Request.put(BASE_URL.concat("/condition"))
                .bodyString("{\"condition\": " + conditionId + " }", ContentType.APPLICATION_JSON)
                .execute();
        assertEquals(putConditionIdResp.returnResponse().getCode(), 200,
                "Unable to set weather condition.");
    }


    @When("weather forecast is retrieved")
    public void getWeatherForecast() throws IOException {
        Response getWeatherResp = Request.get(BASE_URL).execute();
        weatherResponse = objectMapper.readValue(getWeatherResp.returnContent().asString(), WeatherResponse.class);

    }

    @When("Celsius temperature {int} is set correctly")
    public void validateCelsiusTemperatureCalculation(Integer expectedCelsiusTemp) {
        assertEquals(weatherResponse.weather().tempInCelsius(), expectedCelsiusTemp,
                "expected Celsius value is not set correctly so description suffix is compromised.");
    }


    @Then("expected Fahrenheit temperature is same as set value {int} and Celsius calculation is correct")
    public void validateTemperatures(Integer expectedFahrenheitTemp) {
        assertEquals(weatherResponse.weather().tempInFahrenheit(), expectedFahrenheitTemp,
                "Fahrenheit temperature is not correct.");
        Integer expCelsius = Math.round((Float.valueOf(expectedFahrenheitTemp) - 32) * 5 / 9);
        assertEquals(weatherResponse.weather().tempInCelsius(), expCelsius,
                "Celsius temperature is not correct.");

    }

    @Then("expected description suffix is {string}")
    public void validateSuffix(String expectedSuffix) {
        assertThat(weatherResponse.description())
                .endsWith(expectedSuffix)
                .withFailMessage("Suffix is not as expected.");
    }

    @Then("expected condition is {string}")
    public void validateWeatherCondition(String expCondition) {
        assertEquals(weatherResponse.condition(), expCondition);
    }

    @Then("expected icon name is {string}{string}")
    public void validateIconName(String name, String extension) {
        assertEquals(weatherResponse.icon(), name.concat(extension));
    }

    @Then("all values in the response exist")
    public void validateResponseValuesExist() {
        assertTrue(isNotEmpty(weatherResponse.condition()), "condition is missing");
        assertNotNull(isNotEmpty(weatherResponse.city()), "city is missing");
        assertNotNull(isNotEmpty(weatherResponse.icon()), "icon is missing");
        assertNotNull(isNotEmpty(weatherResponse.description()), "description is missing");
        assertNotNull(weatherResponse.weather(), "weather is missing");
        assertNotNull(weatherResponse.weather().tempInFahrenheit(), "Fahrenheit temperature is missing");
        assertNotNull(weatherResponse.weather().tempInCelsius(), "Celsius temperature is missing");
    }

    @Then("expected city name is Vienna")
    public void validateCity() {
        assertEquals(weatherResponse.city(), "Vienna",
                "Expected city name is not Vienna.");
    }

    private Boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
