package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import ssd.uz.wikiquickyapp.collection.PaymentCol;
import ssd.uz.wikiquickyapp.collection.PaymentCol;
import ssd.uz.wikiquickyapp.entity.Order;
import ssd.uz.wikiquickyapp.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select * from payment p where p.id = :paymentId", nativeQuery = true)
    Optional<PaymentCol> findByIdNative(@Param("paymentId") Long id);

    @Query(value = "select * from payment p limit :sizePage offset :number", nativeQuery = true)
    List<PaymentCol> getAllByPageable(@Param(value = "number") Integer number,
                                      @Param(value = "sizePage") Integer sizePage);
}
