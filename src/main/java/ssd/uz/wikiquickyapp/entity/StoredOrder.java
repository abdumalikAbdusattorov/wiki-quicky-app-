package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoredOrder extends AbsEntity {
    @ManyToOne
    private Address address;
    @ManyToOne
    private VehicleType vehicleType;
    private String description;
    @ManyToOne
    private Users client;
    private String receiverNumber;
    private String senderNumber;
    private Double cost;
    private String doorToDoor;
    @ManyToOne(fetch = FetchType.LAZY)
    private LoadType loadType;

    private boolean deleted;
}
