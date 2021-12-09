package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.controller.PriceController;
import ssd.uz.wikiquickyapp.entity.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUser {
    private Long id;
    private String createdAt;
    private String updatedAt;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Long clientOrder;
    private String avatarId;
    private String vehicleId;
    private String email;
    private String gender;
    private String dateOfBirth;

    public ResUser(String phoneNumber, String firstName, String lastName, String avatarId, String email, String gender, String dateOfBirth) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarId = avatarId;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}
