package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;
import ssd.uz.wikiquickyapp.payload.ReqOrderActive;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderActiveTime extends AbsEntity {
    //    @Column(unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Address address;
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;
    private String description;
    //    @Column(unique = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users clientId;
    @Column(unique = true)
    private String receiverNumber;
    private String senderNumber;
    private Double cost;
    private String doorToDoor;
    @ManyToOne(fetch = FetchType.LAZY)
    private LoadType loadType;
    private String posilkaNomi;
    private Timestamp orderTime;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Location> locationList;
}
