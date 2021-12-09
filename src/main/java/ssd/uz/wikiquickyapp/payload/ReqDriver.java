package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReqDriver {

    private String email;//
    private String dateOfBirth;//  tug'ilgan
    private String livingAddress;//  yashash manzili//
    private String firstName;
    private String lastName;
    private String fatherName;
    private Long userAvatar;
    private List<Long> passportPhotos;//

    private Long vehicleId; //  mashinasi//

//    *******  FOR ADMINKA *********
    private String phoneNumber;

}
