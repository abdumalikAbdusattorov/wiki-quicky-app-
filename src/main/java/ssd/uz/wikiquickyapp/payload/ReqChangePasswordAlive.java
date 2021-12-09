package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqChangePasswordAlive {
    private String passwordActive;
    private String passwordNew1;
    private String passwordNew2;

}
