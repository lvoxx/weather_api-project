package com.skyapi.weatherapiservice.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeWeatherDTO {

    @JsonProperty("location_code")
    private String locationCode;
    
    private int temperature;

    private int humidity;

    private int precipitation;

    @JsonProperty("wind_speed")
    private int windSpeed;

    private String status;

    @JsonProperty("last_updated")
    private Date lastUpdated;

    private String location;

}
