package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUserFR {
    private Long id;
    private String role;
    private Boolean verified;
    private Boolean compaleRegister;
    private Long vehicleTypeId;
    private String token;

    public ResUserFR(Long id, List<Role> roles) {

    }
}
