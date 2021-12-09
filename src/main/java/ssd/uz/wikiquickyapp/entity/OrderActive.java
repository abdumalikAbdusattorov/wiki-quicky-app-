package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.enums.WhoPay;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderActive extends AbsEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Address address;
    @ManyToOne(fetch = FetchType.EAGER)
    private VehicleType vehicleType;
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    private Users clientId;
    private String receiverNumber;
    private String senderNumber;
    private Double cost;
    private String doorToDoor;
    @ManyToOne(fetch = FetchType.LAZY)
    private LoadType loadType;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WhoPay whoPay;
    private Integer arrivalTimeOfWorker;

//    private String from;
//    private String to;

}
