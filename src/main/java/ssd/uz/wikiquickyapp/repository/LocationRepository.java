package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.Location;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, Long> {
//    List<Location> findLocationByAddress_Id(Long address_id);
}
