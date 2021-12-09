package ssd.uz.wikiquickyapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.entity.enums.UserType;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.AttachmentRepository;
import ssd.uz.wikiquickyapp.repository.CompanyRepository;
import ssd.uz.wikiquickyapp.repository.RoleRepository;
import ssd.uz.wikiquickyapp.repository.UserRepository;
import ssd.uz.wikiquickyapp.service.SmsVerificationService;

import java.util.*;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticate;

    @Autowired
    SmsVerificationService smsVerificationService;

    @Autowired
    CompanyRepository companyRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Value("${twilio.sid}")
    private String twilioSid;

    @Value("${twilio.token}")
    private String twilioToken;

    @Value("${twilio.phoneNumber}")
    private String twiliophoneNumber;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new UsernameNotFoundException(phoneNumber));
    }

    public UserDetails loadUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User id not found: " + userId));
    }

    public ApiResponseModel register(ReqSignUp reqSignUp,Users users) {
        String randomNumber = getRandomNumber();
        Users user = null;
        Optional<Users> optionalUser = userRepository.findByPhoneNumber(reqSignUp.getPhoneNumber());
        if (optionalUser.isPresent() && optionalUser.get().getIsVerified()) {
            return new ApiResponseModel(false, "phone number exists", null);
        } else {
            if (optionalUser.isPresent()){
                user = userRepository.findUsersByPhoneNumber(reqSignUp.getPhoneNumber());
            }else {
                user = new Users();
            }
            if (reqSignUp.getId() != null) {
                user = userRepository.findById(reqSignUp.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", reqSignUp.getId()));
            }
            if (users != null) {
                user.setCompany(users.getCompany());
                user.setUserType(UserType.BUSINESS);
            } else {
                user.setCompany(companyRepository.findById(1L).get());
            }
            user.setPassword(passwordEncoder.encode(reqSignUp.getPassword()));
            user.setRoles(roleRepository.findAllByName(reqSignUp.getUser_type()));
            user.setPhoneNumber(reqSignUp.getPhoneNumber());
            user.setLastName(reqSignUp.getFirstName());
            user.setFirstName(reqSignUp.getLastName());
            user.setEnabled(true);
            user.setId(user.getId());
            user.setVerificationCode(randomNumber);
            user.setIsVerified(false);
            user.setComplateSUworker(false);
            Users users1 = userRepository.save(user);
            smsVerificationService.login(user.getPhoneNumber(), randomNumber);
            ResUserFR resUserFR = new ResUserFR();
            resUserFR.setRole(users1.getRoles().get(0).getName().name());
            resUserFR.setVerified(users1.getIsVerified());
            resUserFR.setCompaleRegister(users1.getComplateSUworker());
            resUserFR.setId(users1.getId());
            return new ApiResponseModel(true, "user created", resUserFR);
        }
    }

    public ApiResponseModel editUser(ReqUser reqUser, Users users) {
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {

            users.setFirstName(reqUser.getFirstName());
            users.setLastName(reqUser.getLastName());
            users.setEmail(reqUser.getEmail());
            users.setGender(reqUser.getGender());
            users.setDateOfBirth(reqUser.getDateOfBirth());
            if (reqUser.getAttachmentId() != null) {
                users.setAvatarPhoto(attachmentRepository.findById(reqUser.getAttachmentId()).get());
            }
            Users users1 = userRepository.save(users);
            ResUser resUser = new ResUser();
            resUser.setFirstName(users1.getFirstName());
            resUser.setLastName(users1.getLastName());
            resUser.setPhoneNumber(users1.getPhoneNumber());
            resUser.setAvatarId(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(users1.getAvatarPhoto().getId().toString()).toUriString());
            resUser.setEmail(users1.getEmail());
            resUser.setGender(users1.getGender().name());
            resUser.setDateOfBirth(users1.getDateOfBirth());
            apiResponseModel.setData(resUser);
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success");
        }catch (Exception e){
            apiResponseModel.setSuccess(false);
            apiResponseModel.setMessage("error");
        }
        return apiResponseModel;
    }

    public ApiResponseModel compareVerification(ReqVerCode reqVerCode) {
        ApiResponseModel apiResponse = new ApiResponseModel();
        try {
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Verified!!!");
            Users users = userRepository.findUsersByPhoneNumber(reqVerCode.getNumber());
            if (users.getVerificationCode().equals(reqVerCode.getCode())) {
                users.setVerificationCode(null);
                users.setIsVerified(true);
                userRepository.save(users);
                apiResponse.setData(getApiToken(reqVerCode.getNumber(), reqVerCode.getPassword()).getData());
            } else {
                apiResponse.setSuccess(false);
                apiResponse.setMessage("Verification code error!!!");
            }

        } catch (Exception e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Error!!!");
        }
        return apiResponse;
    }

    public ApiResponse getVerificationCode(String numberP) {
        ApiResponse apiResponse = new ApiResponse();
        String randomNumber = getRandomNumber();
        try {
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Verification code : " + randomNumber);
            Users userrrr = userRepository.findUsersByPhoneNumber(numberP);
            userrrr.setVerificationCode(randomNumber);
            smsVerificationService.login(numberP,randomNumber);
            userrrr.setIsVerified(false);
            userRepository.save(userrrr);
        } catch (Exception e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Error!");
        }
        return apiResponse;
    }

    private String getRandomNumber() {
        Random random = new Random();
        int code = random.nextInt(999999 - 100000) + 100000;
        return Integer.toString(code);
    }

    public ApiResponseModel savePass(String phoneNumber, String password) {
        try {
            Users byPhoneNumber = userRepository.findByPhoneNumber(phoneNumber).get();
            if (userRepository.existsByPhoneNumber(phoneNumber)){
                byPhoneNumber.setPassword(passwordEncoder.encode(password));
                userRepository.save(byPhoneNumber);
                return new ApiResponseModel(true, "password successfully changed", getApiToken(phoneNumber, password).getData());
            }
            return new ApiResponseModel(false, "phone number did not found!");
        } catch (Exception e) {
            return new ApiResponseModel(false, "Password not changed!", new ArrayList<>());
        }
    }

    public ApiResponse changePasswordAlive(ReqChangePasswordAlive reqChangePasswordAlive,Users users){
        String ss11 = users.getPassword();
        String ss22 = passwordEncoder.encode(reqChangePasswordAlive.getPasswordNew2());
        if (passwordEncoder.matches(reqChangePasswordAlive.getPasswordActive(), users.getPassword())){
            if (reqChangePasswordAlive.getPasswordNew1().equals(reqChangePasswordAlive.getPasswordNew2())){
                users.setPassword(passwordEncoder.encode(reqChangePasswordAlive.getPasswordNew2()));
                userRepository.save(users);
                return new ApiResponse("password edited successfully",true);
            }
            return new ApiResponse("password1 did not equal password2",false);
        }
        return new ApiResponse("incorrect password",false);
    }

    public ApiResponseModel getApiToken(String phoneNumber, String password) {
     try {
         Users users = userRepository.findUsersByPhoneNumber(phoneNumber);
         if (users.getIsVerified()!=null){
             Authentication authentication = authenticate.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
             SecurityContextHolder.getContext().setAuthentication(authentication);
             String jwt = jwtTokenProvider.generateToken(authentication);
             ResLogin resLogin = new ResLogin();
             if (users.getRoles().get(0).getName()== RoleName.ROLE_WORKER){
                 if (users.getVehicle()!=null){
                     resLogin.setVehicleTypeId(users.getVehicle().getVehicleType().getId());
                 }
             }
             resLogin.setToken(new JwtResponse(jwt));
             resLogin.setRole(users.getRoles().get(0).getName().name());
             resLogin.setUserId(users.getId());
             resLogin.setVerified(users.getIsVerified());
             resLogin.setCompaleRegister(users.getComplateSUworker());
             return new ApiResponseModel(true,"token is generated",resLogin);
         }else {
             return new ApiResponseModel(false,"code was not verified !",null);
         }
     }catch (Exception e){
         return new ApiResponseModel(false,"bunaqa user mavjud emas",null);
     }

    }

    public ApiResponse getUser(Users users){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            if (users!=null){
                ResUserFR resUserFR = new ResUserFR();
                resUserFR.setId(users.getId());
                if (users.getVehicle()!=null){
                    resUserFR.setVehicleTypeId(users.getVehicle().getVehicleType().getId());
                }
                resUserFR.setVerified(users.getIsVerified());
                resUserFR.setCompaleRegister(users.getComplateSUworker());
                resUserFR.setRole(users.getRoles().get(0).getName().name());
                apiResponseModel.setData(resUserFR);
                apiResponseModel.setMessage("success");
                apiResponseModel.setSuccess(true);
            }else {
                apiResponseModel.setMessage("number is not verified !");
                apiResponseModel.setSuccess(false);
            }
        }catch (Exception e){
            apiResponseModel.setMessage("error");
            apiResponseModel.setSuccess(true);
        }
        return apiResponseModel;
    }

    public ApiResponseModel forgetPassword(ReqVerCode reqVerCode,Users users){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            if (users.getVerificationCode().equals(reqVerCode.getCode())){
                users.setId(users.getId());
                users.setVerificationCode(null);
                users.setIsVerified(true);
                users.setPassword(passwordEncoder.encode(reqVerCode.getPassword()));
                userRepository.save(users);
                apiResponseModel.setSuccess(true);
                apiResponseModel.setMessage("password edited !");
            }else {
                apiResponseModel.setMessage("code did not equal");
                apiResponseModel.setSuccess(false);
            }
        }catch (Exception e){
            apiResponseModel.setMessage("error !");
            apiResponseModel.setSuccess(false);
        }
        return apiResponseModel;
    }

    public ApiResponseModel getToken(ReqSignIn reqSignIn){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            Authentication authentication = authenticate.authenticate(new UsernamePasswordAuthenticationToken(reqSignIn.getPhoneNumber(), reqSignIn.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success !");
            apiResponseModel.setData(new JwtResponse(jwt));
        }catch (Exception e){
            apiResponseModel.setMessage("error !");
            apiResponseModel.setSuccess(false);
        }
        return apiResponseModel;
    }

    public ApiResponseModel saveDeviceToken(Users users,String token){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try{
            users.setDeviceToken(token);
            userRepository.save(users);
            apiResponseModel.setMessage("success !");
            apiResponseModel.setSuccess(true);
        }catch (Exception e){
            apiResponseModel.setMessage("error !");
            apiResponseModel.setSuccess(false);
        }
        return apiResponseModel;
    }

    public ApiResponseModel editWorkerForToUser(Users users,String roleName){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            if (users.getComplateSUworker()){
                users.setRoles(roleRepository.findAllByName(RoleName.valueOf(roleName)));
                users.setId(users.getId());
                Users users1 = userRepository.save(users);
                apiResponseModel.setSuccess(true);
                apiResponseModel.setMessage("success");
                apiResponseModel.setData(users1.getRoles().get(0).getName());
            }else {
                apiResponseModel.setSuccess(false);
                apiResponseModel.setMessage("siz worker bulib register qilmagansiz !");
            }
        }catch (Exception e){
            apiResponseModel.setSuccess(false);
            apiResponseModel.setMessage("error");
        }
        return apiResponseModel;
    }
}