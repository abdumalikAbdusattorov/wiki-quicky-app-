package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResOrderActive {
    private Long id;
    private ResAddress address;
    private String receiverNumber;
    private String senderNumber;
    private Double orderCost;
    private String whoPay;
    private String doorToDoor;

    public ResOrderActive(Long id, String doorToDoor) {

    }


}
