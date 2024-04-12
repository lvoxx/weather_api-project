package com.skyapi.weatherapiservice.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.skyapi.weatherapicommon.Location;

public interface LocationRepository extends JpaRepository<Location, String> {

    @Query("SELECT l from Location l WHERE l.trashed = false")
    public List<Location> findAllUntrashed();

    @Query("SELECT l from Location l WHERE l.trashed = false AND l.code = ?1")
    public Location findByCode(String code);

    @Modifying
    @Query("UPDATE Location SET trashed = true WHERE code = ?1")
    public int trashByCode(String code);
}
