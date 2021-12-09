package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.component.BalanceListener;
import ssd.uz.wikiquickyapp.component.PaymentListener;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(PaymentListener.class)
public class Payment extends AbsEntity { // Kompaniyaga tushadigan pullar
    private double sum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users worker;

}
