package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.BalanceLog;

public interface BalanceLogRepository extends JpaRepository<BalanceLog, Long> {
}
