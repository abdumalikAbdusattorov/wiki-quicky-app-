package ssd.uz.wikiquickyapp.projection;

import ssd.uz.wikiquickyapp.entity.Address;
import ssd.uz.wikiquickyapp.entity.VehicleType;

public interface StoredOrderColl {
    Long getId();

    Address getAddress();

    VehicleType getVehicleType();

    String getReceiverNumber();

    Double getCost();
}
