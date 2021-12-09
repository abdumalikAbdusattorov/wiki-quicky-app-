package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import ssd.uz.wikiquickyapp.entity.VehicleType;

@Data
@AllArgsConstructor
public class ResWorkerActive {
    private Long id;
    private Boolean busy;
    private Double lan;
    private Double lat;
    private ResOrder resOrder;
    private VehicleType vehicleType;
    private String companyName;
}
