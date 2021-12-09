package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.WorkerActive;

import java.util.List;
import java.util.Optional;

public interface WorkerActiveRepository extends JpaRepository<WorkerActive, Long> {
    Optional<WorkerActive> findById(Long id);

    @Query(value = "select * from worker_active where vehicle_type_id=:typeV and company_id=:companyId and busy=false ", nativeQuery = true)
    List<WorkerActive> selectWorkerActive(@Param("typeV") Long typeV,@Param("companyId") Long companyId);

    @Query(value = "select * from worker_active where worker_id_id=:workerId", nativeQuery = true)
    WorkerActive selectWorkerActiev(@Param("workerId") Long workerId);

    @Query(value = "select * from worker_active where order_active=:orderId", nativeQuery = true)
    WorkerActive findWorkerActiev(@Param("orderId") Long orderId);

    @Query(value = "select * from  worker_active where worker_active.worker_id_id=:wokerId", nativeQuery = true)
    WorkerActive findWorkerActive(@Param("wokerId") Long WorkerId);

    @Query(value = "select * from worker_active where company_id=:id",nativeQuery = true)
    List<WorkerActive> findAllByCompany(@Param("id") Long id);

    Boolean existsByWorkerId(Users workerId);

    void deleteWorkerActivesByWorkerId(Users workerId);

}
