package com.skyapi.weatherapiservice.realtime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapicommon.RealtimeWeather;
import com.skyapi.weatherapiservice.geo_location.GeoLocationService;
import com.skyapi.weatherapiservice.helper.error.GeoLocationException;
import com.skyapi.weatherapiservice.helper.error.LocationNotFoundException;
import com.skyapi.weatherapiservice.helper.utils.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/v1/realtime")
@RequiredArgsConstructor
@Slf4j
public class RealtimeWeatherApiConntroller {
    
    @NonNull
    private GeoLocationService geoService;
    @NonNull
    private RealtimeWeatherService realtimeService;

    @GetMapping
    public ResponseEntity<RealtimeWeather> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            Location locationFromIp = geoService.getLocationFromIP(ipAddress);
            RealtimeWeather realtimeWeather = realtimeService.getByLocation(locationFromIp);

            return ResponseEntity.ok(realtimeWeather);
        } catch (GeoLocationException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
        
    }
    
}
