package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.PayType;

public interface PayTypeRepository extends JpaRepository<PayType, Long> {
}
