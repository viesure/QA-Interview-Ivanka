Feature: Test Get Weather API
  Verify GET Weather API which is to be used by weather related application's MVP backend.

  Scenario: Test city name is Vienna
    When weather forecast is retrieved
    Then all values in the response exist
    And expected city name is Vienna

  Scenario Outline: Test Fahrenheit and Celsius temperatures
    Given Fahrenheit temperature is set with value <inputFahrenheit>
    When weather forecast is retrieved
    Then all values in the response exist
    And expected Fahrenheit temperature is same as set value <inputFahrenheit> and Celsius calculation is correct
    Examples:
      | inputFahrenheit |
      | -1              |
      | 0               |
      | 1               |
      | 32              |
      | 75              |

  Scenario Outline: Test weather descriptions
    Given Celsius temperature value is <inputCelsius>
    When weather forecast is retrieved
    When Celsius temperature <inputCelsius> is set correctly
    Then all values in the response exist
    And expected description suffix is <expectedSuffix>
    Examples:
      | celsiusCondition | inputCelsius | expectedSuffix |
      | "celsius <= 0"   | -1           | "freezing"     |
      | "celsius <= 0"   | 0            | "freezing"     |
      | "celsius < 10"   | 1            | "cold"         |
      | "celsius < 10"   | 9            | "cold"         |
      | "celsius < 20"   | 10           | "mild"         |
      | "celsius < 20"   | 11           | "mild"         |
      | "celsius < 20"   | 19           | "mild"         |
      | "celsius < 25"   | 20           | "warm"         |
      | "celsius < 25"   | 21           | "warm"         |
      | "celsius < 25"   | 24           | "warm"         |
      | "celsius >= 25"  | 25           | "hot"          |
      | "celsius >= 25"  | 26           | "hot"          |


  Scenario Outline: Test weather conditions
    Given condition id is set with value <inputCond>
    When weather forecast is retrieved
    Then all values in the response exist
    And expected condition is <expCond>
    Examples:
      | inputCond | expCond   |
      | 1         | "clear"   |
      | 2         | "windy"   |
      | 3         | "mist"    |
      | 4         | "drizzle" |
      | 5         | "dust"    |

  Scenario Outline: Test weather icon names
    Given condition id is set with value <inputCond>
    When weather forecast is retrieved
    Then all values in the response exist
    And expected icon name is <expCond>".png"
    Examples:
      | inputCond | expCond   |
      | 1         | "clear"   |
      | 2         | "windy"   |
      | 3         | "mist"    |
      | 4         | "drizzle" |
      | 5         | "dust"    |

