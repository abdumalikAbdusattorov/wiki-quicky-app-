package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.OrderActive;
import ssd.uz.wikiquickyapp.entity.WorkerActive;
import ssd.uz.wikiquickyapp.entity.WorkerActiveInRadius;

import java.util.List;
import java.util.Optional;

public interface WorkerActiveInRadiusRepository extends JpaRepository<WorkerActiveInRadius, Long> {

    Optional<WorkerActiveInRadius> findById(Long id);

    int deleteWorkerActiveInRadiusByOrderActive_Id(Long orderActive_id);

    @Query(value = "select * from worker_active_in_radius where worker_active_id_id=:workerId and order_active_id=:orderActiveId", nativeQuery = true)
    WorkerActiveInRadius search(@Param("workerId") Long workerId, @Param("orderActiveId") Long orderActiveId);

    @Query(value = "select * from worker_active_in_radius where worker_active_id_id=:workerId and order_active_id=:orderActiveId", nativeQuery = true)
    WorkerActiveInRadius search2(@Param("workerId") Long workerId, @Param("orderActiveId") Long orderActiveId);

    @Modifying
    @Query(value = "delete from worker_active_in_radius where order_active=:orderId", nativeQuery = true)
    void deleteT(@Param("orderId") Long orderId);

    @Query(value = "select worker_active_id_id from worker_active_in_radius where order_active_id=:orderActiveId", nativeQuery = true)
    List<WorkerActive> findAllWorkerByWorkerId(@Param("orderActiveId") Long orderActiveId);

    @Query(value = "select * from worker_active_id where order_active=:orderId", nativeQuery = true)
    List<Long> find(@Param("orderId") Long orderId);

    @Query(value = "select * from worker_active_in_radius where order_active_id=:orderId and is_used=false ", nativeQuery = true)
    List<WorkerActiveInRadius> findWAIRByIsUsedAndOrderActiveId(@Param("orderId") Long orderId);

    @Query(value = "select * from worker_active_in_radius where order_active_id=:orderId and is_used=true ", nativeQuery = true)
    List<WorkerActiveInRadius> findWAIRByIsUsedAndOrderActiveId2(@Param("orderId") Long orderId);

    @Query(value = "select * from worker_active_in_radius where order_active_id=:orderId and is_used=true ", nativeQuery = true)
    List<WorkerActiveInRadius> findWAIRByIsUsedAndOrderActive(@Param("orderId") Long orderId);

    @Query(value = "select count(is_used) from worker_active_in_radius where is_used=false", nativeQuery = true)
    long count();


}
