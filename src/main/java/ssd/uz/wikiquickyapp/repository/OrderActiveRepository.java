package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.OrderActive;

public interface OrderActiveRepository extends JpaRepository<OrderActive, Long> {

    boolean existsById(Long id);

    @Query(value = "select * from order_active where client_id_id=:clientId", nativeQuery = true)
    OrderActive selectOA(@Param("clientId") Long clientId);

    @Query(value = "select * from order_active where worker_id_id=:workerActiveId", nativeQuery = true)
    OrderActive selectOrderActiveByWorkerActiveId(@Param("workerActiveId") Long workerActiveId);

}
