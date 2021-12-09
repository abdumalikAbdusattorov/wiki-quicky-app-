package ssd.uz.wikiquickyapp.payload;

import lombok.Data;
import ssd.uz.wikiquickyapp.entity.enums.Gender;
import ssd.uz.wikiquickyapp.entity.enums.IsWorker;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class ReqUser {
    private String firstName;
    private String lastName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Long attachmentId;
    private String dateOfBirth;
}
