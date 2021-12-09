package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PaysysTransaction extends AbsEntity {
    private int vendor_id;
    private int payment_id;
    private String payment_name;
    private long agr_trans_id;
    private String merchant_trans_id;
    private float merchant_trans_amount;
    private String environment;
    private String merchant_trans_data;
    private int status;
    private long sign_time;
}
