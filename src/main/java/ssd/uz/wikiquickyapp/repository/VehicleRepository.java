package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.Vehicle;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Page<Vehicle> findAllByNameStartingWithIgnoreCase(String search, Pageable pageable);

//    void deleteVehicleById(Long id);

//    @Query(value = "select * from vehicle v where v.user_id = :userId", nativeQuery = true)
//    Vehicle getVehicleByUser(@Param(value = "userId") Long userId);

    @Query(value = "select * from vehicle v where v.vehicle_photo_id = :vehicle_photo_id", nativeQuery = true)
    Vehicle getVehicleByVehiclePhoto(@Param(value = "vehicle_photo_id") Long vehicle_photo_id);
}
