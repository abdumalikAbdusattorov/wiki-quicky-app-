package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.ClientWorkerOrder;

import java.util.List;

public interface ClientWorkerOrderRepository extends JpaRepository<ClientWorkerOrder, Long> {
    @Query(value = "select * from client_worker_order where is_used=false ", nativeQuery = true)
    List<ClientWorkerOrder> find_client_order_by_is_used();

    int deleteClientWorkerOrderByOrderId(Long order_id);

}
