package io.viesure.dimitrova.response;

public record WeatherResponse(
        String city,
        String condition,
        String icon,
        String description,
        Weather weather) {

    public record Weather(
            Integer tempInFahrenheit,
            Integer tempInCelsius) {
    }
}

