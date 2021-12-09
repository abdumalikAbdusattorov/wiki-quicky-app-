package ssd.uz.wikiquickyapp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.enums.AttachmentTypeEnum;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentType extends AbsEntity {
    private String contentTypes;
    private int width;
    private int height;

    @Column(unique = true)
    @Enumerated(value = EnumType.STRING)
    private AttachmentTypeEnum type;

    private Long size;

}
