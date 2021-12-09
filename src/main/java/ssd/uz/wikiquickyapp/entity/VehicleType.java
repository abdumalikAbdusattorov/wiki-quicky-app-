package ssd.uz.wikiquickyapp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ssd.uz.wikiquickyapp.entity.template.AbsNameEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class VehicleType extends AbsNameEntity {

}
