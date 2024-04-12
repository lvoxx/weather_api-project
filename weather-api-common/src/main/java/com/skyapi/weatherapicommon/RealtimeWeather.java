package com.skyapi.weatherapicommon;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "realtime_weather")
public class RealtimeWeather {

    @Id
    @Column(name = "location_code", nullable = false)
    @JsonProperty("location_code")
    private String locationCode;

    private int temperature;

    private int humidity;

    private int precipitation;

    @JsonProperty("wind_speed")
    private int windSpeed;

    @Column(length = 50)
    private String status;

    @JsonProperty("last_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date lastUpdated;

    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;

    public void setLocation(Location location) {
        this.setLocationCode(location.getCode());
        this.location = location;
    }
}
