package ssd.uz.wikiquickyapp.map.fcm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReqFcm {
    private String title;
    private String message;
    private String role;
}
