package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.Location;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResFromFireBaseToWorker {
    private Location a_location;
    private Location b_location;
    private Location workerLocation;
    private String arrivalTime;
    private Double distance;
    private Double price;

}
