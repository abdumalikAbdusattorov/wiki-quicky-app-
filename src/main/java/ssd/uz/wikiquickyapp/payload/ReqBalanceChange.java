package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqBalanceChange {
    private Long id;
    private Double difference;
    private Long workerId;
    private Long payTypeId; //superadmin yoki admin balansni toldirsa, paytypeId=cashId keliwi kk,
    // agar haydovchi plastigidan toldirsa paytypeId=cardId
}
