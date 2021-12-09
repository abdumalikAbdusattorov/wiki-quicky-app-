package ssd.uz.wikiquickyapp.entity;

import lombok.*;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address extends AbsEntity {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Location> locations;
    private Double distance;
}
