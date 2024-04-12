package com.skyapi.weatherapiservice.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapicommon.RealtimeWeather;

@SpringBootTest(classes = { com.skyapi.weatherapiservice.WeatherApiServiceApplication.class })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class LocationRepositoryTest {

    @Autowired
    private LocationRepository tLocationRepo;

    @Test
    void testAddLocationSuccess() {
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United State of America")
                .enabled(true)
                .build();

        Location savedLocation = tLocationRepo.save(location);

        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");
    }

    @Test
    void testListSuccess() {
        List<Location> locations = tLocationRepo.findAllUntrashed();

        assertThat(locations).isNotEmpty();

        locations.forEach(System.out::println);
    }

    @Test
    void testGetNotFound() {
        String code = "ABCD";
        Location location = tLocationRepo.findByCode(code);

        assertThat(location).isNull();
    }

    @Test
    void testGetFound() {
        String code = "DELHI_IN";
        Location location = tLocationRepo.findByCode(code);

        assertThat(location).isNotNull();
        assertThat(location.getCode()).isEqualTo(code);
    }

    @Test
    void testTrashSuccess() {
        String code = "LACA_USA";

        tLocationRepo.trashByCode(code);

        Location location = tLocationRepo.findByCode(code);

        assertThat(location).isNull();
    }

    @Test
    void testAddRealtimeWeatherData() {
        String code = "NYC_USA";

        Location location = tLocationRepo.findByCode(code);

        RealtimeWeather weather = location.getRealtimeWeather();

        if (weather == null) {
            weather = new RealtimeWeather();
            weather.setLocation(location);
            location.setRealtimeWeather(weather);
        }

        weather.setTemperature(10);
        weather.setHumidity(60);
        weather.setPrecipitation(42);
        weather.setStatus("Sunny");
        weather.setWindSpeed(10);
        weather.setLastUpdated(new Date());

        Location savedLocation = tLocationRepo.save(location);
        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);
    }
}
