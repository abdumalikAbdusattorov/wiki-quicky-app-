package ssd.uz.wikiquickyapp.payload;

import lombok.Data;

@Data
public class SignUpForBussiness {
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;
}
