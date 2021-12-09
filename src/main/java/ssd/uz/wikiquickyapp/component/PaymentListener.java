package ssd.uz.wikiquickyapp.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ssd.uz.wikiquickyapp.entity.Payment;
import ssd.uz.wikiquickyapp.entity.PaymentLog;
import ssd.uz.wikiquickyapp.entity.enums.OperationName;
import ssd.uz.wikiquickyapp.repository.PaymentLogRepository;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Component
public class PaymentListener {
    @Autowired
    PaymentLogRepository paymentLogRepository;

    @PrePersist
    public void addPayment(Object object) {
        Payment payment = (Payment) object;
        paymentLogRepository.save(new PaymentLog(
                payment.getSum(),
                payment.getWorker().getId(),
                OperationName.INSERT));
    }

    @PreUpdate
    public void editPayment(Object object) {
        Payment payment = (Payment) object;
        paymentLogRepository.save(new PaymentLog(
                payment.getSum(),
                payment.getWorker().getId(),
                OperationName.UPDATE));
    }

    @PreRemove
    public void deletePayment(Object object) {
        Payment payment = (Payment) object;
        paymentLogRepository.save(new PaymentLog(
                payment.getSum(),
                payment.getWorker().getId(),
                OperationName.DELETE));
    }
}
