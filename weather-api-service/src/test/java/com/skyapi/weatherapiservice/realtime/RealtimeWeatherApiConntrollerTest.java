package com.skyapi.weatherapiservice.realtime;

import static org.hamcrest.Matchers.is;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapicommon.RealtimeWeather;
import com.skyapi.weatherapiservice.geo_location.GeoLocationService;
import com.skyapi.weatherapiservice.helper.error.GeoLocationException;
import com.skyapi.weatherapiservice.helper.error.LocationNotFoundException;

@WebMvcTest(RealtimeWeatherApiConntroller.class)
class RealtimeWeatherApiConntrollerTest {

    static final String END_POINT_PATH = "/v1/realtime";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    GeoLocationService geoService;
    @MockBean
    RealtimeWeatherService realtimeService;

    @Test
    void testGetShouldReturnStatus400BadRequest() throws Exception {
        Mockito.when(geoService.getLocationFromIP(Mockito.anyString())).thenThrow(GeoLocationException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetShouldReturn404NotFound() throws Exception {
        Location location = new Location();

        Mockito.when(geoService.getLocationFromIP(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetShouldReturn200OK() throws Exception {
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United State of America")
                .enabled(true)
                .build();
        RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                .temperature(-2)
                .humidity(32)
                .precipitation(42)
                .status("Snowy")
                .windSpeed(12)
                .lastUpdated(new Date())
                .build();

        location.setRealtimeWeather(realtimeWeather);
        realtimeWeather.setLocation(location);

        Mockito.when(geoService.getLocationFromIP(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeService.getByLocation(location)).thenReturn(realtimeWeather);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.code", is("NYC_USA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.humidity", is(32)))
                .andDo(MockMvcResultHandlers.print());
    }
}
