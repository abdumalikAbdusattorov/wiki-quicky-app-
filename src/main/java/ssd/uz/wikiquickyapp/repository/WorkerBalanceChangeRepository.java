package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.collection.WorkerBalanceChangeCol;
import ssd.uz.wikiquickyapp.entity.Payment;
import ssd.uz.wikiquickyapp.entity.WorkerBalanceChange;

import java.util.List;
import java.util.Optional;

public interface WorkerBalanceChangeRepository extends JpaRepository<WorkerBalanceChange, Long> {
    @Query(value = "select case when exists(select * from worker_balance_change wbc where wbc.order_id =:orderId) then true else false end", nativeQuery = true)
    boolean existsByOrder(@Param(value = "orderId") Long orderId);

    @Query(value = "select * from worker_balance_change wbc limit :sizePage offset :number ", nativeQuery = true)
    List<WorkerBalanceChangeCol> getAllByWorkerBalanceChange(@Param(value = "number") Integer number, @Param(value = "sizePage") Integer sizePage
//                                   @Param(value = "search") String search
    );

    @Query(value = "select * from worker_balance_change wbc where wbc.id = :wbcId", nativeQuery = true)
    Optional<WorkerBalanceChangeCol> getWorkerBalanceChangeCol(@Param("wbcId") Long id);
}
