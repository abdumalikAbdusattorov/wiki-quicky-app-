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
public class FeedBack extends AbsEntity {
    private String description;
    private byte rating;
    //   @ManyToOne(fetch = FetchType.LAZY)
    private Long client;
    //   @ManyToOne(fetch = FetchType.LAZY)
    private Long worker;
    private String fromWho;

    //   @OneToOne(fetch = FetchType.LAZY)
    //  private Long order;

}
