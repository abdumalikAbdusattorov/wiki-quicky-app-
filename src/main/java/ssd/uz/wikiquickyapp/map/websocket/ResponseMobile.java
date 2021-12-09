package ssd.uz.wikiquickyapp.map.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMobile {
    private String message;
    private Boolean success;
}
