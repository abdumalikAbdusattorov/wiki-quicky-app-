package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqTransaction {
    private String environment;
    private String agr_trans_id;
    private String vendor_trans_id;
    private String merchant_trans_id;
    private String merchant_trans_amount;
    private String state;
    private String date;
}
