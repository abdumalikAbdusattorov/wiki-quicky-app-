package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqCard {
    private Long id;
    private String cardNumber;
    private String expirationDate;
}
