//package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

//@EqualsAndHashCode(callSuper = true)
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class Card extends AbsEntity {
//    @Column(length = 16, unique = true)
//    private String cardNumber;
//    private String expirationDate;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private CardType cardType;
//
//    private String verificationCode;
//
//    private boolean isVerified;

//    public static void main(String[] args) {
//        String date = "09/22";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/YY", Locale.US);
//        LocalDate parsedDate = LocalDate.parse(date,formatter);
//        System.out.println(parsedDate);
//    }
//}
