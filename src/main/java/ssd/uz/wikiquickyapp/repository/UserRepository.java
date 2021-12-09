
package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.collection.UsersCol;
import ssd.uz.wikiquickyapp.entity.Role;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.Vehicle;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query(value = "select * from users where company_id=:companyId ",nativeQuery = true)
    List<Users> findAllUsersByComapaby(@Param("companyId") Long id);

    @Query(value = "select * from users where company_id=:companyId and cast(created_at as date ) between cast(:start as date ) and cast(:end as date )" +
            "limit :max offset :size",nativeQuery = true)
    List<Users> findAllUsersByTime(@Param("companyId") Long id,
                                   @Param("start") String start,
                                   @Param("end") String end,
                                   @Param("max") Integer max,
                                   @Param("size") Integer size
                                   );

//    *********************************************

    Optional<Users> findByPhoneNumber(String phoneNumber);

    Users findUsersByPhoneNumber(String phoneNumber);

    Optional<Users> getByPhoneNumber(String phoneNumber);

    List<Users> findAllByRoles(Role role);

    boolean existsByEmail(String email);

    Users findByVehicle(Vehicle vehicle);

    @Query(value = "select case when exists(select * from users u where u.phone_number = :phoneNumber) then true else false end", nativeQuery = true)
    boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    @Query(value = "select * from users u where u.id in(select user_id from user_role ur where ur.role_id = :roleId " +
            "intersect select id from users u where u.created_at between cast(:startTime as timestamp) and cast(:endTime as timestamp))"
            , nativeQuery = true)
    Page<Users> findAllByCreatedAtBetweenAndRolesQuery(@Param(value = "startTime") String start,
                                                       @Param("endTime") String end,
                                                       @Param("roleId") Integer id,
                                                       Pageable pageable);

    @Query(value = "select * from users u where u.id in \n" +
            "(select user_id from user_role ur where ur.role_id = :roleId " +
            "intersect " +
            "select id from users u where u.created_at between :startTime and :endTime" +
            "and (position(:search in users.first_name) > 0 or position(:search in users.last_name) > 0)",
            nativeQuery = true)
    Page<Users> findAllByCreatedAtBetweenAndRolesContainsQuery(@Param("startTime") String start,
                                                               @Param("endTime") String end,
                                                               @Param("roleId") Integer id,
                                                               @Param("search") String search,
                                                               Pageable pageable);

    @Query(value = "select count(*) from users u where u.id in \n" +
            "(select user_id from user_role ur where ur.role_id = :roleId \n" +
            "intersect \n" +
            "select id from users u where u.created_at between cast(:startTime as timestamp) and cast(:endTime as timestamp))", nativeQuery = true)
    long countByCreatedAtBetweenAndRolesQuery(@Param("startTime") String start,
                                              @Param("endTime") String end,
                                              @Param("roleId") Integer id);


    @Query(value = "select count(*) from users u where u.created_at between cast(:startTime as timestamp) and cast(:endTime as timestamp)", nativeQuery = true)
    long countAllUsersCountInTimeInterval(@Param("startTime") String start,
                                          @Param("endTime") String end);

    @Query(value = "select * from vehicle v where v.created_at between cast(:startTime as timestamp) and cast(:endTime as timestamp) and v.vehicle_type_id = :vehicleTypeId", nativeQuery = true)
    long countByCreatedAtBetweenAndWorkerAndVehicleType(@Param("startTime") String start,
                                                        @Param("endTime") String end,
                                                        @Param("vehicleTypeId") Long vehicleTypeId);


    //    // todo agar shu matotda xatolik bo`ladigan bo`lsa userCol ni o`rniga users ni yozing !
//    @Query(value = "select * from users u where u.vehicle_id=:vehicleId and u.is_verified=:status order by id DESC limit :sizePage offset :number", nativeQuery = true)
//    List<UsersCol> findAllByPageable(@Param(value = "vehicleId") Long vehicleId, @Param(value = "status") Boolean status, @Param(value = "number") Integer number, @Param(value = "sizePage") Integer sizePage);
//
//    Optional<UsersCol> findUsersById(Long id);
//// todo agar shu matotda xatolik bo`ladigan bo`lsa userCol ni o`rniga users ni yozing !
    @Query(value = "select * from users u where u.id in\n" +
            "(select up.users_id from users_passport_photos up)\n" +
            "and u.vehicle_id in (select v.id from vehicle v\n" +
            "where v.vehicle_type_id =:vehicleTypeId) and u.is_checked_worker is null" +
            " order by id DESC limit :sizePage offset :number", nativeQuery = true)
    List<Users> findAllByPageableNull(
            @Param(value = "vehicleTypeId") Long vehicleTypeId,
            @Param(value = "number") Integer number,
            @Param(value = "sizePage") Integer sizePage);

    @Query(value = "select * from users u where u.id in\n" +
            "(select up.users_id from users_passport_photos up)\n" +
            "and u.vehicle_id in (select v.id from vehicle v\n" +
            "where v.vehicle_type_id =:vehicleTypeId) and u.is_checked_worker=:status" +
            " order by id DESC limit :sizePage offset :number", nativeQuery = true)
    List<Users> findAllByPageableStatus(@Param(value = "vehicleTypeId") Long vehicleTypeId, @Param(value = "status") Boolean status,
            @Param(value = "number") Integer number, @Param(value = "sizePage") Integer sizePage);


    Optional<Users> findUsersById(Long id);

    @Query(value = "select * from users where users.id in (select d.user_id from decription d)", nativeQuery = true)
    List<UsersCol> findAllUsersByDescription();


    @Query(value = "select AVG(points) from users where is_verified=true and is_checked_worker=true", nativeQuery = true)
    Long sentPointToWorker();


}