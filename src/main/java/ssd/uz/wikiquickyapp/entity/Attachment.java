package ssd.uz.wikiquickyapp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attachment extends AbsEntity {

    private String name;
    private String contentType;
    private long size;

    @ManyToOne(fetch = FetchType.LAZY)
    private AttachmentType attachmentType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AttachmentContent attachmentContent;

    public Attachment(String name, String contentType, long size, AttachmentContent attachmentContent) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.attachmentContent = attachmentContent;
    }
}
