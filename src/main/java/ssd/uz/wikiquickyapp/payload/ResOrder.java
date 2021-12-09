package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.Address;
import ssd.uz.wikiquickyapp.entity.LoadType;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.VehicleType;
import ssd.uz.wikiquickyapp.entity.enums.OrderStatus;
import ssd.uz.wikiquickyapp.entity.enums.WhoPay;
import ssd.uz.wikiquickyapp.entity.enums.WorkerLocation;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResOrder {
    private Address address;
    private String receiverNumber;
    private String senderNumber;
    private Timestamp startTime;
    private ResWorker resWorker;
    private String clientId;
    private String receiver;
    private Double orderCost;
    private String rejectFrom;
    private OrderStatus orderStatusEnum;
    private LoadType loadType;
    private VehicleType vehicleType;
    private WhoPay whoPay;
    private String shipmentDetails;
    private String doorToDoor;
    private WorkerLocation workerLocation;

    public ResOrder(Address address, String receiverNumber, String senderNumber, Timestamp startTime, Double orderCost, OrderStatus orderStatusEnum, LoadType loadType, WhoPay whoPay, String shipmentDetails , String doorToDoor) {
        this.address = address;
        this.receiverNumber = receiverNumber;
        this.senderNumber = senderNumber;
        this.startTime = startTime;
        this.orderCost = orderCost;
        this.orderStatusEnum = orderStatusEnum;
        this.loadType = loadType;
        this.whoPay = whoPay;
        this.shipmentDetails = shipmentDetails;
        this.doorToDoor = doorToDoor;
    }

    public ResOrder(Address addressId, String receiverNumber, String senderNumber, String doorToDoor, Double orderCost) {

    }
    //    private String from;
//    private String to;

}
