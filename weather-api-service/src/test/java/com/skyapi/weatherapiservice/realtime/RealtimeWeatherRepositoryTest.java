package com.skyapi.weatherapiservice.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherapicommon.RealtimeWeather;

@SpringBootTest(classes = { com.skyapi.weatherapiservice.WeatherApiServiceApplication.class })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class RealtimeWeatherRepositoryTest {

    @Autowired
    private RealtimeWeatherRepository repos;

    @Test
    void testUpdate() {
        String locationCode = "DELHI_IN";

        RealtimeWeather weather = repos.findById(locationCode).get();

        weather.setTemperature(-2);
        weather.setHumidity(32);
        weather.setPrecipitation(42);
        weather.setStatus("Snowy");
        weather.setWindSpeed(12);
        weather.setLastUpdated(new Date());

        RealtimeWeather updatedWeather = repos.save(weather);

        assertThat(updatedWeather.getHumidity()).isEqualTo(32);
    }


    @Test
    void testFindByCountryCodeAdCityNameNotFound(){
        String countryCode = "JP";
        String cityName = "Tokyo";

        RealtimeWeather realtimeWeather = repos.findByCountryCodeAndCityName(countryCode, cityName);

        assertThat(realtimeWeather).isNull();
    }

    @Test
    void testFindByCountryCodeAdCityNameFound(){
        String countryCode = "US";
        String cityName = "New York City";

        RealtimeWeather realtimeWeather = repos.findByCountryCodeAndCityName(countryCode, cityName);

        assertThat(realtimeWeather).isNotNull();
        assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo(cityName);
    }
}
