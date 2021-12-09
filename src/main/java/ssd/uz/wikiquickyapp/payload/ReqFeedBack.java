package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqFeedBack {
    private String description;
    private byte rating;
    private Long to;
}
