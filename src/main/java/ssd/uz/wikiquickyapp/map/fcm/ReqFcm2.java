package ssd.uz.wikiquickyapp.map.fcm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReqFcm2 {
    private ReqFcm data;
    private String to;
}
