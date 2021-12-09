package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ssd.uz.wikiquickyapp.entity.enums.Gender;
import ssd.uz.wikiquickyapp.entity.enums.IsWorker;
import ssd.uz.wikiquickyapp.entity.enums.UserType;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialStruct;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class Users extends AbsEntity implements UserDetails {
    private String verificationCode;//* twilio firebase dan kegan kodni saqlab qoyadigan joy
    private String firstName;//*
    private String lastName;//*
    private String phoneNumber;//*
    private String password;//*u
    private String email;//*
    private String dateOfBirth;//tug'ilgan
    private String livingAddress;//yashash manzili
    private Double balance;//hisobidaga pul miqdir
    private Double profitOfDay;//bir kunlik foydasi
    private Long clientOrder;//client bergan buyurtmalar soni
    private Long workerOrder;//worker bajargan yurtmalar soni
    private Double points;// achkosi yani bahosi ishchi sifatifda olgan bali
    private Double lan;
    private Double lat;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private Boolean isVerified;
    private Boolean canChangePassword;
    private Boolean isAndroid = null;
    private Boolean isCheckedWorker = null;
    private Boolean complateSUworker;
    private String deviceToken;
    @ManyToOne
    private Company company;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment avatarPhoto;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Attachment> passportPhotos;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Vehicle vehicle; //mashinasi

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<WorkerBalanceChange> workerBalanceChanges;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;


    public Users(String phoneNumber, String password, String firstName, String lastName, List<Role> roles) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public Users(String firstName, String lastName, String phoneNumber, String password, Boolean isVerified, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isVerified = isVerified;
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
