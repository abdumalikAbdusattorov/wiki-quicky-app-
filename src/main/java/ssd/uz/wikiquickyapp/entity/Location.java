package ssd.uz.wikiquickyapp.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Location {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private Double lan;
    private Double lat;
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Address address;

//    public Location(Double lan, Double lat) {
//        this.lan = lan;
//        this.lat = lat;
//        this.address = address;
//    }
//    public Location(Double lan, Double lat, Address address) {
//        this.lan = lan;
//        this.lat = lat;
//        this.address = address;
//    }

    public Location(UUID id, Double lan, Double lat) {
        this.id = id;
        this.lan = lan;
        this.lat = lat;
    }

    public Location(Double lan, Double lat) {
        this.lan = lan;
        this.lat = lat;
    }
}
