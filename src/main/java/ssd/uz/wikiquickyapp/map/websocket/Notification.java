package ssd.uz.wikiquickyapp.map.websocket;

import lombok.Data;

@Data
public class Notification {
    private String message;
    private String transactionId;
}