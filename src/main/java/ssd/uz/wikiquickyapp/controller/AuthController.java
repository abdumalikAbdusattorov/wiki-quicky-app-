package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.UserRepository;
import ssd.uz.wikiquickyapp.security.AuthService;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.security.JwtTokenProvider;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticate;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/get/token")
    public HttpEntity<?> getToken(@RequestBody ReqSignIn reqSignIn){
        ApiResponseModel apiResponseModel = authService.getToken(reqSignIn);
        return ResponseEntity.ok(apiResponseModel);
    }

    @GetMapping("/user/me")
    public ApiResponse getUser(@CurrentUser Users users) {
        return authService.getUser(users);
    }

    @PostMapping("/register")
    public HttpEntity<?> register(@Valid @RequestBody ReqSignUp reqSignUp,@CurrentUser Users users) {
        ApiResponseModel response = authService.register(reqSignUp,users);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @PutMapping("/getVerificationCode")
    public ApiResponse getVerificationCode(@RequestBody ReqVerCode number) {
        return authService.getVerificationCode(number.getNumber());
    }

    @PutMapping("/verification")
    public ApiResponse userVerification(@RequestBody ReqVerCode reqVerCode) {
        return authService.compareVerification(reqVerCode);
    }

    @PatchMapping("/changePassword")
    public HttpEntity<?> changePassword(@RequestBody ReqSignIn reqSignIn) {
        try {
            return ResponseEntity.ok(authService.savePass(reqSignIn.getPhoneNumber(), reqSignIn.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Mistake", false));
        }
    }

    @PostMapping("/changeActivePassword")
    public ApiResponse changeActivePassword(@RequestBody ReqChangePasswordAlive reqChangePasswordAlive, @CurrentUser Users users){
        return authService.changePasswordAlive(reqChangePasswordAlive,users);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@Valid @RequestBody ReqSignIn reqSignIn) {
        ApiResponseModel responseModel = authService.getApiToken(reqSignIn.getPhoneNumber(), reqSignIn.getPassword());
        return ResponseEntity.ok(responseModel);
    }

    @PutMapping("/edit")
    public ApiResponseModel editUser(@CurrentUser Users users,@RequestBody ReqUser reqUser) {
        return authService.editUser(reqUser, users);
    }

    @PutMapping("/forget/password")
    public ApiResponseModel forgetPassword(@RequestBody ReqVerCode forgetPassword, @CurrentUser Users users) {
        return authService.forgetPassword(forgetPassword, users);
    }

    @PostMapping("/set/device/token")
    public ApiResponseModel userMe(@CurrentUser Users users,@RequestParam("token") String  token){
        ApiResponseModel apiResponseModel = authService.saveDeviceToken(users,token);
        return apiResponseModel;
    }

    @PutMapping("/edit/role/worker")
    public HttpEntity<?> editWorkerRole(@CurrentUser Users users,@RequestParam("role") String role){
        ApiResponseModel apiResponseModel = authService.editWorkerForToUser(users,role);
        return ResponseEntity.ok(apiResponseModel);
    }
}
