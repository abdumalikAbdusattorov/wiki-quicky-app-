package ssd.uz.wikiquickyapp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AttachmentContent extends AbsEntity {

//    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
//    private Attachment attachment;

    private byte[] content;
}
