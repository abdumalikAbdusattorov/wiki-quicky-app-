package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ResLogin {
    private JwtResponse token;
    private Long userId;
    private String role;
    private Boolean verified;
    private Boolean compaleRegister;
    private Long vehicleTypeId;
}
