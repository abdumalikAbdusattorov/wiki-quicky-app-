package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqSdkNotify {
    private int agr_trans_id;
    private int vendor_trans_id;
    private int status;
    private int sign_time;
    private String sign_string;
}
