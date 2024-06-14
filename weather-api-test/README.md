# Weather-API-test project
This is a sample _java maven_ project for automation testing of **weather API** service used by weather related application's backend.
All test cases are implemented with _Cucumber BDD_ approach, based on acceptance criteria-s required by the backend.

Languages, frameworks and technologies used in the test project:
* Java
* Maven and POM
* BDD and Cucumber automated tests
* TestNG
* ExtentReports

## Test run
Prerequisites to run the tests:
* java 17
* maven


  To run all the tests - in current project folder (weather-api-test) execute following _maven_ command:
```mvn test ``` 

## Test report generation
After every test run completion, a detailed test report is generated automatically as a HTML document under:
    ``` ./target/test-reports/weather-api.html ```
    [test report](./target/test-reports/weather-api.html)

### Remove generated report and classes
To clean generated test report and classes, in current project folder (weather-api-test) execute following _maven_ command:
```mvn clean ``` 

### Test report results
Example for already executed tests and generated report

[weather-api-test-report-20240614-0832.html](weather-api-test-report-20240614-0832.html)


