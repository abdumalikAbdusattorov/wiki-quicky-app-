package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(value = "select * from address where client_id=:clientId", nativeQuery = true)
    Address select(@Param("clientId") Long clientId);
}
