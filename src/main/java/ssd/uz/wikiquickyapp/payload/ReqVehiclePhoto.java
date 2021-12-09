package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqVehiclePhoto {
    private Long id;
    private Long frontSide;
    private Long rightSide;
    private Long leftSide;
    private Long baggage;
    private Long frontLicense;
    private Long backLicense;
    private Long licenseWithWorker;
    private Long texPassportFront;
    private Long texPassportBack;
}
