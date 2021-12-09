package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import ssd.uz.wikiquickyapp.collection.UsersCol;
import ssd.uz.wikiquickyapp.entity.Address;
import ssd.uz.wikiquickyapp.entity.LoadType;
import ssd.uz.wikiquickyapp.entity.VehicleType;

@Data
@AllArgsConstructor
public class ResStoredOrder {
    private Long id;
    private Address address;
    private VehicleType vehicleType;
    private UsersCol users;
    private String receiverNumber;
    private String senderNumber;
    private Double cost;
    private String doorToDoor;
    private LoadType loadType;
}
