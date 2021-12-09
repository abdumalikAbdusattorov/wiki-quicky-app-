package ssd.uz.wikiquickyapp.payload;

import lombok.Data;
import ssd.uz.wikiquickyapp.entity.enums.Gender;
import ssd.uz.wikiquickyapp.entity.enums.IsWorker;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.entity.enums.UserType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;

@Data

public class ReqSignUp {

    private Long id;

    @Pattern(regexp = "[+][9][9][8][0-9]{9}")
    private String phoneNumber;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private RoleName user_type;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Long companyId;

}
