package com.skyapi.weatherapiservice.features.location;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapiservice.helper.error.LocationNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    @NonNull
    private final LocationRepository lRepository;

    public Location add(Location location) {
        return lRepository.save(location);
    }

    public List<Location> list() {
        return lRepository.findAllUntrashed();
    }

    public Location get(String code) {
        return lRepository.findByCode(code);
    }

    public Location update(Location locationInRequest) throws LocationNotFoundException {
        String code = locationInRequest.getCode();

        Location locationInDB = lRepository.findByCode(code);
        if (locationInDB == null)
            throw new LocationNotFoundException("No location found with the given code: " + code);

        locationInDB.setCityName(locationInRequest.getCityName());
        locationInDB.setRegionName(locationInRequest.getRegionName());
        locationInDB.setCountryCode(locationInRequest.getCountryCode());
        locationInDB.setCountryName(locationInRequest.getCountryName());
        locationInDB.setEnabled(locationInRequest.isEnabled());

        return lRepository.save(locationInDB);
    }

    public void delete(String code) throws LocationNotFoundException {
        if (!lRepository.existsById(code)) {
            throw new LocationNotFoundException("No locationn found with the given coe: " + code);
        }

        lRepository.trashByCode(code);
    }
}
