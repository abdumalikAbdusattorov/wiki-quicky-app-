package ssd.uz.wikiquickyapp.payload;

import lombok.Data;
import ssd.uz.wikiquickyapp.entity.enums.WhoPay;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;

@Data
public class ReqStoredOrder {
    private Long id;
    private ReqAddress reqAddress;
    private String description;
    private Long vehicleType;
    private Double orderCost;
    @Pattern(regexp = "[+][9][9][8][0-9]{9}")
    private String receiverNumber;
    @Pattern(regexp = "[+][9][9][8][0-9]{9}")
    private String senderNumber;
    private String doorToDoor;
    private Long loadType;
    @Enumerated(EnumType.STRING)
    private WhoPay whoPay;
    private Long companyId;
}
