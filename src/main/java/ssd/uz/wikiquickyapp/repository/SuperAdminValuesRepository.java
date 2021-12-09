package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.SuperAdminValue;

import java.util.List;
import java.util.Optional;


public interface SuperAdminValuesRepository extends JpaRepository<SuperAdminValue, Long> {
    Optional<SuperAdminValue> findSuperAdminValueByName(String name);

    Optional<SuperAdminValue> findByName(String profitPercent);
}
