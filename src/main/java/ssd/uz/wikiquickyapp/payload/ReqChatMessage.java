package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

import java.util.Date;

@Data
public class ReqChatMessage {
    private Integer id;
    private String content;
    private Date timestamp;
}
