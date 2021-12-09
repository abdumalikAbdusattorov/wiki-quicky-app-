package ssd.uz.wikiquickyapp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle extends AbsEntity {
    private String name; //transport nomi masalan: Cobalt, Labo, Spark

    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private VehiclePhoto vehiclePhoto;

    private String color;

    @Column(nullable = false, unique = true)
//    @Pattern(regexp = "^[0-9]{2}|[A-Z][0-9]{3}[A-Z]{2}|[0-9]{3}[A-Z]{3}|[0-9]{3}[A-Z]{2}|[0-9]{4}[A-Z]{2}$",
//            message = "For cars use pattern like -> 'A123AA' or '123AAA', for trucks -> '1234AA', for motorbikes -> '123AA'")
    private String carNumber;

    private boolean active = true;


}
