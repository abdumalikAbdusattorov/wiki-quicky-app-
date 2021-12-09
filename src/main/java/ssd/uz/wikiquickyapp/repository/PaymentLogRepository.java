package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.PaymentLog;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
}
