package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.enums.OrderStatus;
import ssd.uz.wikiquickyapp.entity.enums.WhoPay;
import ssd.uz.wikiquickyapp.entity.enums.WorkerLocation;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order extends AbsEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Address addressId;

    private String description;
    private String receiverNumber;
    private String senderNumber;
    private Timestamp startTime;
    //    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Europe/Zagreb")
    private Timestamp endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users worker;

    @ManyToOne(fetch = FetchType.LAZY)
    private LoadType loadType;

    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;

    private Double orderCost;
    private String doorToDoor;
    private String posilkaNomi;
    private Long waitingTime;
    private String rejectFrom;
    @Enumerated(EnumType.STRING)
    private WhoPay whoPay;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatusEnum;
    @Enumerated(EnumType.STRING)
    private WorkerLocation workerLocation;

    private Boolean paid;
    private Boolean deleted;
    private Boolean storedOrders;

    private String cancelCause;

}
