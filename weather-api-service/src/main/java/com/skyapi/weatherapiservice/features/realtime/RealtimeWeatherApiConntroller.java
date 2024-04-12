package com.skyapi.weatherapiservice.features.realtime;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapicommon.RealtimeWeather;
import com.skyapi.weatherapiservice.dto.RealtimeWeatherDTO;
import com.skyapi.weatherapiservice.features.geo_location.GeoLocationService;
import com.skyapi.weatherapiservice.helper.error.GeoLocationException;
import com.skyapi.weatherapiservice.helper.error.LocationNotFoundException;
import com.skyapi.weatherapiservice.helper.utils.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/v1/realtime")
@RequiredArgsConstructor
@Slf4j
public class RealtimeWeatherApiConntroller {

    @NonNull
    private GeoLocationService geoService;
    @NonNull
    private RealtimeWeatherService realtimeService;
    @NonNull
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<RealtimeWeatherDTO> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            Location locationFromIp = geoService.getLocationFromIP(ipAddress);
            RealtimeWeather realtimeWeather = realtimeService.getByLocation(locationFromIp);

            RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
            return ResponseEntity.ok(dto);
        } catch (GeoLocationException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<RealtimeWeatherDTO> getRealtimeWeatherByLocationCode(
            @PathVariable(name = "locationCode") String locationCode) {
        try {
            RealtimeWeather realtimeWeather = realtimeService.getByLocationCode(locationCode);
            RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

            return ResponseEntity.ok(dto);
        } catch (LocationNotFoundException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

}
