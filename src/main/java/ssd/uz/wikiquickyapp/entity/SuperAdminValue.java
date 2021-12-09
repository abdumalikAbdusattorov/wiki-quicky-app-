package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminValue extends AbsEntity {

    private String name;
    private Integer value;

}
