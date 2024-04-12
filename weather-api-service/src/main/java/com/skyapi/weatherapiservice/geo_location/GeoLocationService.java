package com.skyapi.weatherapiservice.geo_location;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapiservice.helper.error.GeoLocationException;
import com.skyapi.weatherapiservice.helper.string.Ip2LocPath;
import com.skyapi.weatherapiservice.helper.string.Ip2LocStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeoLocationService {

    private String dbPath = Ip2LocPath.DB_PATH;

    private IP2Location ip2Location;

    public GeoLocationService() {
        ip2Location = new IP2Location();
        try {
            ip2Location.Open(dbPath);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public Location getLocationFromIP(String ipAddress) throws GeoLocationException {
        try {
            IPResult result = ip2Location.IPQuery(ipAddress);

            if (!Ip2LocStatus.OK.getStatus().equals(result.getStatus())) {
                throw new GeoLocationException("Geolocation failed with status: " + result.getStatus());
            }
            return new Location(result.getCity(), result.getRegion(), result.getCountryLong(),
                    result.getCountryShort());
        } catch (IOException ex) {
            throw new GeoLocationException("Error querying IP database", ex);
        }
    }
}
