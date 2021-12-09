package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.Address;
import ssd.uz.wikiquickyapp.entity.LoadType;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.enums.OrderStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResOrderToWorker {
    private Address addressId;
    private String description;
    private String receiverNumber;
    private Users client;
    private Users worker;
    private LoadType loadType;
    private Double orderCost;
    private String doorToDoor;
    private String posilkaNomi;
    private Long waitingTime;
    private String rejectFrom;
}
