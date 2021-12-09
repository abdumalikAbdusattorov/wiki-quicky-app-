package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqChangePassword {
    private String number;
    private String password1;
}
