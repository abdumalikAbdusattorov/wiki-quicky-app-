package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.PaysysTransaction;
import ssd.uz.wikiquickyapp.payload.ReqTransaction;

import java.util.List;
import java.util.Optional;

public interface PaysysTransactionsRepository extends JpaRepository<PaysysTransaction, Long> {
    @Query(value = "select * from paysys_transaction t where t.vendor_id = :vendorId AND t.agr_trans_id = :agrTransId AND t.payment_id = :paymentId AND t.merchant_trans_id = :merchantTransId", nativeQuery = true)
    Optional<PaysysTransaction> getTransaction(
            @Param(value = "vendorId") long vendorId,
            @Param(value = "agrTransId") long agrTransId,
            @Param(value = "paymentId") int paymentId,
            @Param(value = "merchantTransId") String merchantTransId);

    @Query(value = "select * from paysys_transaction t where t.agr_trans_id = :transactionId", nativeQuery = true)
    Optional<PaysysTransaction> getTransactionByAgrTransId(
            @Param(value = "transactionId") long transactionId);

    @Query(value = "update paysys_transaction t set status = :status where t.id = :vendorTransId", nativeQuery = true)
    boolean updateTransaction(
            @Param(value = "vendorTransId") long vendorTransId,
            @Param(value = "status") int status);

    @Query(value = "select * from paysys_transaction t where t.id = :vendorTransId", nativeQuery = true)
    Optional<PaysysTransaction> getTransactionByVendorTransId(
            @Param(value = "vendorTransId") long vendorTransId);

    @Query(value = "select * from paysys_transaction t where t.sign_time between :fromDate and :toDate", nativeQuery = true)
    List<PaysysTransaction> getTransactions(
            @Param(value = "fromDate") long from,
            @Param(value = "toDate") long to
    );

}

