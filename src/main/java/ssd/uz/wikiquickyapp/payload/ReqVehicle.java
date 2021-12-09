package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class ReqVehicle {
    private Long id;
    private Long vehicleTypeId;
    private String name;
    private Long vehiclePhotoId;
    private String color;
    private String carNumber;
}
