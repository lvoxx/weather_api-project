package com.skyapi.weatherapiservice.features.realtime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skyapi.weatherapicommon.RealtimeWeather;


public interface RealtimeWeatherRepository extends JpaRepository<RealtimeWeather, String> {
    
    @Query("SELECT r FROM RealtimeWeather r WHERE r.location.countryCode = ?1 AND r.location.cityName = ?2")
    public RealtimeWeather findByCountryCodeAndCityName(String coutryCode, String cityName);

    @Query("SELECT r FROM RealtimeWeather r WHERE r.locationCode = ?1 AND r.location.trashed = false")
    public RealtimeWeather findByLocationCode(String locationCode);
    
}
