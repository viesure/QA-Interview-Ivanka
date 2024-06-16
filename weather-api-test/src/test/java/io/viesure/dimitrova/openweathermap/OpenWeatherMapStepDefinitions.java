package io.viesure.dimitrova.openweathermap;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenWeatherMapStepDefinitions {

    DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy MMM dd, hh:mma");
    private WebDriver driver;
    private Wait<WebDriver> wait;
    private ZoneId zoneId;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        wait =
                new FluentWait<>(driver)
                        .withTimeout(Duration.ofSeconds(20))
                        .pollingEvery(Duration.ofMillis(500))
                        .withMessage("Waited more then 20s to load the widget.");
    }

    @Given("I open the OpenWeatherMap main page")
    public void openMainPage() {
        driver.get("https://openweathermap.org/");
    }

    @When("desktop menu is loaded")
    public void waitDesktopMenuIsLoaded() {
        wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("desktop-menu")));
    }

    @Then("I expect that the placeholder text in search field is {string}")
    public void verifyPlaceholderInMainSearchMenu(String expectedPlaceholder) {

        WebElement mainMenuSearch = driver.findElement(By.xpath("//*[@id=\"desktop-menu\"]/form"))
                .findElement(By.name("q"));
        String placeholderText = mainMenuSearch.getAttribute("placeholder");
        assertThat(placeholderText)
                .isEqualTo(expectedPlaceholder)
                .withFailMessage("The placeholder text is not as expected.");
    }

    @When("the weather widget is loaded")
    public void waitForWeatherWidget() {

        WebElement weatherWidget =
                driver.findElement(By.id("weather-widget"));

        wait.until(ExpectedConditions.not(
                ExpectedConditions.visibilityOf(
                        weatherWidget.findElement(By.className("owm-loader")
                        ))));
    }

    @When("I search for {string} at {string} timezoneId selecting {string} from the list")
    public void searchFor(String cityName, String timezoneId, String cityNameAndCountry) {
        zoneId = ZoneId.of(timezoneId);
        startDateTime = ZonedDateTime.now(zoneId);

        WebElement searchBlock =
                driver.findElement(By.id("weather-widget")).findElement(By.className("search-block"));
        wait.until(ExpectedConditions.visibilityOf(searchBlock));

        WebElement searchBox = searchBlock.findElement(By.cssSelector("input[placeholder='Search city']"));
        searchBox.sendKeys(cityName);

        WebElement button = searchBlock.findElement(By.xpath("*/button"));

        wait.until(ExpectedConditions.visibilityOf(button));
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();

        List<WebElement> dropdownOptions =
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("ul.search-dropdown-menu li")));

        WebElement searchResult = driver.findElement(By.xpath("//div[@class='current-container mobile-padding']//h2"));
        String initialText = searchResult.getText();

        dropdownOptions.stream()
                .filter(p -> p.getText().trim().startsWith(cityNameAndCountry))
                .findFirst().ifPresent(WebElement::click);

        wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(searchResult, initialText)));

        endDateTime = ZonedDateTime.now(zoneId);
    }

    @Then("in the result I expect city name {string} with correct date and time")
    public void shouldSeeTheCityNameInTheResultWithCorrectDateTime(String cityName) {

        WebElement searchResult = driver.findElement(By.xpath("//div[@class='current-container mobile-padding']/div[1]"));

        String resultCity = searchResult.findElement(By.tagName("h2")).getText();
        assertThat(resultCity.trim())
                .isEqualTo(cityName)
                .withFailMessage("The search result does not contain  the expected city name.");


        String dateTimeStringWithoutYear = searchResult.findElement(By.tagName("span"))
                .getText();

        ZonedDateTime cityLocalDateTime =
                LocalDateTime.parse(endDateTime.getYear() + " " + dateTimeStringWithoutYear,
                                dateTimeFormatter)
                        .atZone(zoneId);

        assertThat(cityLocalDateTime.plusMinutes(1))
                .isAfterOrEqualTo(startDateTime.truncatedTo(ChronoUnit.MINUTES))
                .withFailMessage("Provided date time is too early");
        assertThat(cityLocalDateTime.minusMinutes(1))
                .isBeforeOrEqualTo(endDateTime.truncatedTo(ChronoUnit.MINUTES))
                .withFailMessage("Provided date time is too late");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
