package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.SuperAdminValue;

public interface SuperAdminRepository extends JpaRepository<SuperAdminValue, Long> {
}
