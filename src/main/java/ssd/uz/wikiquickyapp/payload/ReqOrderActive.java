package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ReqOrderActive {
    private ReqAddress reqAddress;
    private String description;
    private Long vehicleType;
    private Double orderCost;
    @Pattern(regexp = "[+][9][9][8][0-9]{9}")
    private String receiverNumber;
    @Pattern(regexp = "[+][9][9][8][0-9]{9}")
    private String senderNumber;
    private Long loadType;
    private String doorToDoor;
    private String orderTime;
    private String whoPay;
    private String from;
    private String to;
    private Long companyId;
}
//"/navoiy kuchasi dan / tatugacha "
