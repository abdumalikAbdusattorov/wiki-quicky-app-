package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclePhoto extends AbsEntity {
    //oldi, bagaj, o'ng, chap tomoni, ichi(salon- oldi, orqa otdelno), prava(oldi, orqasi, haydovchi bilan birga)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment frontSide;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment baggage;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment rightSide;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment leftSide;

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Attachment frontSalon;
//
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Attachment backSalon;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment frontLicense;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment backLicense;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment licenseWithWorker;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment texPassportFront;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment texPassportBack;

}
