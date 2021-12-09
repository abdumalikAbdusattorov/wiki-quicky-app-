package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResVehicle {
    private Long id;
    private String name;
    private Long vehicleTypeId;
    private Long vehiclePhotoId;
    private String color;
    private String carNumber;
}
