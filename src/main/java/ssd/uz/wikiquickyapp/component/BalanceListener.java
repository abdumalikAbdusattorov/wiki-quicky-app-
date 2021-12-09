package ssd.uz.wikiquickyapp.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ssd.uz.wikiquickyapp.entity.BalanceLog;
import ssd.uz.wikiquickyapp.entity.WorkerBalanceChange;
import ssd.uz.wikiquickyapp.entity.enums.OperationName;
import ssd.uz.wikiquickyapp.repository.BalanceLogRepository;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Component
public class BalanceListener {
    @Autowired
    BalanceLogRepository balanceLogRepository;

    @PrePersist
    public void replenishBalanceCreate(Object object) {
        WorkerBalanceChange balanceChange = (WorkerBalanceChange) object;
        balanceLogRepository.save(new BalanceLog(
                balanceChange.getBalance(),
                balanceChange.getDiff(),
                balanceChange.getBonus(),
                balanceChange.getOrderId(),
                balanceChange.getWorkerId(),
                OperationName.INSERT
        ));
    }

    @PreUpdate
    public void replenishBalanceUpdate(Object object) {
        WorkerBalanceChange balanceChange = (WorkerBalanceChange) object;
        balanceLogRepository.save(new BalanceLog(
                balanceChange.getBalance(),
                balanceChange.getDiff(),
                balanceChange.getBonus(),
                balanceChange.getOrderId(),
                balanceChange.getWorkerId(),
                OperationName.UPDATE
        ));
    }

    @PreRemove
    public void replenishBalanceDelete(Object object) {
        WorkerBalanceChange balanceChange = (WorkerBalanceChange) object;
        balanceLogRepository.save(new BalanceLog(
                balanceChange.getBalance(),
                balanceChange.getDiff(),
                balanceChange.getBonus(),
                balanceChange.getOrderId(),
                balanceChange.getWorkerId(),
                OperationName.DELETE
        ));
    }


}
