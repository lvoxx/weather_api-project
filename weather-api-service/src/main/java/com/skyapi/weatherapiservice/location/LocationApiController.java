package com.skyapi.weatherapiservice.location;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapiservice.helper.error.LocationNotFoundException;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/locations")
@RequiredArgsConstructor
public class LocationApiController {

    @NonNull
    private final LocationService lService;

    @PostMapping
    public ResponseEntity<Location> addLocation(@Valid @RequestBody Location location) {
        Location addedLocation = lService.add(location);
        URI uri = URI.create("/v1/location/" + addedLocation.getCode());
        return ResponseEntity.created(uri).body(addedLocation);
    }

    @GetMapping
    public ResponseEntity<List<Location>> listLocations() {
        List<Location> locations = lService.list();

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Location> getLocation(@PathVariable("code") String code) {
        Location location = lService.get(code);

        if (location == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(location);
    }

    @PutMapping
    public ResponseEntity<Location> updateLocation(@Valid @RequestBody Location location) {
        try {
            Location updatedLocation = lService.update(location);

            return ResponseEntity.ok(updatedLocation);
        } catch (LocationNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Integer> deleteLocation(@PathVariable("code") String code) {
        try {
            lService.delete(code);
            return ResponseEntity.noContent().build();
        } catch (LocationNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
