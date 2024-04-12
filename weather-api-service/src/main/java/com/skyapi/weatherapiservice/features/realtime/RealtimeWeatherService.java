package com.skyapi.weatherapiservice.features.realtime;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapicommon.RealtimeWeather;
import com.skyapi.weatherapiservice.features.location.LocationRepository;
import com.skyapi.weatherapiservice.helper.error.LocationNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealtimeWeatherService {

    @NonNull
    private RealtimeWeatherRepository realtimeWeatherRepository;
    @NonNull
    private LocationRepository locationRepository;

    public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCityName(countryCode, cityName);
        if (realtimeWeather == null) {
            throw new LocationNotFoundException("No location found with the given country name and city name");
        }

        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException {
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        if (realtimeWeather == null) {
            throw new LocationNotFoundException("No location found with the given location code");
        }

        return realtimeWeather;
    }

    public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather)
            throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);

        if (location == null) {
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }

        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdated(new Date());

        return realtimeWeatherRepository.save(realtimeWeather);
    }

}
