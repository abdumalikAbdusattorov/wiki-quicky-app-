package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.component.BalanceListener;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(BalanceListener.class)
public class WorkerBalanceChange extends AbsEntity {
    private Double balance;
    //    private Double dailyProfit;
    private String diff; //+150000 -9000
    private String bonus;
    private Long orderId;
    private Long workerId;
}
