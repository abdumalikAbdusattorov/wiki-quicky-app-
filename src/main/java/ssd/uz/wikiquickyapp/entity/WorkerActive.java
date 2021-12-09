package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkerActive extends AbsEntity {
    private Boolean busy;
    private Double lan;
    private Double lat;
    @OneToOne(fetch = FetchType.LAZY)
    private Users workerId;
    @OneToOne(fetch = FetchType.LAZY)
    private Order orderId;
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;
    @ManyToOne
    private Company company;
}
