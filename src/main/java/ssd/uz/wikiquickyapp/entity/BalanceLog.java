package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.enums.OperationName;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BalanceLog extends AbsEntity {
    private double balance;
    private String difference;
    private String bonus;
    private Long orderId;
    private Long workerId;
    private OperationName operationName;
}
