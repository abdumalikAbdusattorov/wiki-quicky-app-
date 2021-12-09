package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.collection.OrderCol;
import ssd.uz.wikiquickyapp.entity.Order;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ResOrder;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {

// ***
    // all order by company id
    @Query(value = "select * from orders where worker_id in (select id from users where company_id=:companyId)",nativeQuery = true)
   List<Order> findAllOrdersBYCompany(@Param("companyId") Long companyId);

    @Query(value = "select * from orders where order_status_enum=:statusParam and worker_id in (select id from users where company_id=:companyId)",nativeQuery = true)
    List<Order> findAllCancelledOrdersBYCompany(@Param("companyId") Long companyId,@Param("statusParam") String statusEnum);

    @Query(value = "select * from orders where cast(created_at as date) between cast(:start as date) and cast(:end as date)" +
            " and worker_id in (select id from users where company_id=:companyId)",nativeQuery = true)
    List<Order> findAllOrdersBYTime(@Param("companyId") Long companyId,@Param("start") String start,@Param("end") String end);

    @Query(value = "select * from orders where order_status_enum=:statusParam and cast(created_at as date) between cast(:start as date) and cast(:end as date)" +
            " and worker_id in (select id from users where company_id=:companyId)",nativeQuery = true)
    List<Order> findAllOrdersBYTimeAndStatus(@Param("companyId") Long companyId,@Param("start") String start,@Param("end") String end,@Param("statusParam") String status);

    @Query(value = "select * from orders where worker_id=:id and cast(created_at as date) between cast(:start as date) and cast(:end as date)",nativeQuery = true)
    List<Order> findOrdersByWorker(@Param("id") Long id,@Param("start") String start,@Param("end") String end);

// ***
    //jami orderlar sonini chiqaradi
    @Query(value = "select count(*) from orders o where o.created_at between cast(:startTime as timestamp) and cast(:endTime as timestamp)",
            nativeQuery = true)
    Long findAllByOrderCount(@Param("startTime") String start,
                             @Param("endTime") String end);

    @Query(value = "select * from orders where created_at = (select max(created_at) from orders where worker_id=:workerId)", nativeQuery = true)
    Order selectWorkerActive(@Param("workerId") Long workerId);

    @Query(value = "select * from orders where created_at = (select max(created_at) from orders where client_id=:clientId)", nativeQuery = true)
    Order selectClient(@Param("clientId") Long clientId);

    //     orderlarni vehicle_type bilan saralash
    @Query(value = "select count(*) from orders o where o.worker_id in (select u.id from users u where u.vehicle_id in " +
            "(select v.id from vehicle v where v.vehicle_type_id=:vehicleTypeId)) and o.created_at between cast(:startTime as timestamp) and cast(:endTime as timestamp)",
            nativeQuery = true)
    Long countAllByVehicleType(@Param("startTime") String start, @Param("endTime") String end, @Param("vehicleTypeId") Long vehicleType);

    List<Order> findAllByClientAndDeleted(Users client,Boolean delete, Pageable pageable);

    List<Order> findAllByClientAndDeleted(Users client, Boolean deleted);

    List<Order> findAllByWorkerAndDeleted(Users worker, Boolean deleted);

    @Query(value = "select * from orders where client_id=:cId and deleted=false and created_at between cast(:startTime as date ) and cast(:endTime as date )",nativeQuery = true)
    List<Order> selectOrdersByTimeForClient(@Param("cId") Long id,@Param("startTime") String startTime,@Param("endTime") String endTime,Pageable pageable);

    @Query(value = "select * from orders where client_id=:cId and deleted=false and created_at between cast(:startTime as date ) and cast(:endTime as date )",nativeQuery = true)
    List<Order> selectOrdersByTimeForClientAll(@Param("cId") Long id,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Query(value = "select * from orders where worker_id=:wId and deleted=false and created_at between cast(:startTime as date ) and cast(:endTime as date )",nativeQuery = true)
    List<Order> selectOrdersByTimeForWorker(@Param("wId") Long id,@Param("startTime") String startTime,@Param("endTime") String endTime,Pageable pageable);

    @Query(value = "select * from orders where worker_id=:wId and deleted=false and created_at between cast(:startTime as date ) and cast(:endTime as date )",nativeQuery = true)
    List<Order> selectOrdersByTimeForWorkerAll(@Param("wId") Long id,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<Order> findAllByWorker(Users worker, Pageable pageable);
//    @Query(value = "select * from orders where client_id=:clientId limit=:limNumber offset=:offNumber",nativeQuery = true)
//    List<Order> findAllordersByClient(@Param("clientId") Long client,@Param("limNumber") Long lim,@Param("offNumber") Long off);
//
//    @Query(value = "select * from orders where client_id=:workerId limit=:limNumber offset=:offNumber",nativeQuery = true)
//    List<Order> findAllordersByWorker(@Param("workerId") Long client,@Param("limNumber") Long lim,@Param("offNumber") Long off);

    @Query(value = "select * from orders  where client_id=:id", nativeQuery = true)
    List<Order> findOrderByClient(@Param("id") Long id);

    @Query(value = "select * from orders  where worker_id=:id", nativeQuery = true)
    List<Order> findOrderByWorker(@Param("id") Long id);

}
