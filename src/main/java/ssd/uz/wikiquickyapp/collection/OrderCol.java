package ssd.uz.wikiquickyapp.collection;

import ssd.uz.wikiquickyapp.entity.Address;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.enums.OrderStatus;

import java.sql.Timestamp;

public interface OrderCol {
    Long getId();

    Address getAddressId();

    String getDescription();

    String getReceiverNumber();

    Timestamp getStartTime();

    Timestamp getEndTime();

    Users getClient();

    Users getWorker();

    Double getOrderCost();

    String getRejectFrom();

    OrderStatus getOrderStatusEnum();
}
