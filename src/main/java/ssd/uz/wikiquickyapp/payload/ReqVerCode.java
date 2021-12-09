package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqVerCode {
    private String number;
    private String code;
    private String password;
}
