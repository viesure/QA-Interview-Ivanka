Feature: Search for a city on OpenWeatherMap

  Scenario: Verify that main page search field contains correct placeholder text
    Given I open the OpenWeatherMap main page
    When desktop menu is loaded
    Then I expect that the placeholder text in search field is "Weather in your city"


  Scenario: User searches for "Sydney AU" and verify its date/time
    Given I open the OpenWeatherMap main page
    When the weather widget is loaded
    And I search for "Sydney" at "Australia/Sydney" timezoneId selecting "Sydney, AU" from the list
    Then in the result I expect city name "Sydney, AU" with correct date and time

