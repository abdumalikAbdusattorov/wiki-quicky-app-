package ssd.uz.wikiquickyapp.payload;

import lombok.Data;
import ssd.uz.wikiquickyapp.entity.Location;

import java.util.List;
@Data
public class ResAddress {
    private List<Location> locations;
}
