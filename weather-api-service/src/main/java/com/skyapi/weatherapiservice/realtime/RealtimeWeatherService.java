package com.skyapi.weatherapiservice.realtime;

import org.springframework.stereotype.Service;

import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapicommon.RealtimeWeather;
import com.skyapi.weatherapiservice.helper.error.LocationNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealtimeWeatherService {

    @NonNull
    private RealtimeWeatherRepository realtimeWeatherRepository;

    public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCityName(countryCode, cityName);
        if(realtimeWeather == null){
            throw new LocationNotFoundException("No location found with the given country name and city name");
        }

        return realtimeWeather;
    }

}
