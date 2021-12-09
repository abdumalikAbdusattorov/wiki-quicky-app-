package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqSdkCancel {
    private int agr_trans_id;
    private int vendor_trans_id;
    private int sign_time;
    private String sign_string;
}
