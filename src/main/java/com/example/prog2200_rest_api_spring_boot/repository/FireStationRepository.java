package com.example.alertsystem.repository;

import com.example.alertsystem.model.FireStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FireStationRepository extends JpaRepository<FireStation, Long> {

    List<FireStation> findAll();

    /**
     *
     * @param address String to include in the WHERE clause of the query joining FireStation, ServiceArea and Address tables.
     * @return Integer of the station number associated with the address parameter
     */
    @Query("SELECT fs.stationNumber FROM FireStation fs " +
            "JOIN ServiceArea sa  ON sa.servicearea_id = fs.servicearea.servicearea_id " +
            "JOIN Address a  ON a.servicearea.servicearea_id = sa.servicearea_id " +
            "WHERE a.address = :address")
    Integer findStationNumberByAddress(@Param("address") String address);

}