package ssd.uz.wikiquickyapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.collection.UsersCol;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqDriver;
import ssd.uz.wikiquickyapp.payload.ResOrderClient;
import ssd.uz.wikiquickyapp.repository.UserRepository;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PutMapping("/creatDriver")
    public ApiResponse creatDriver(@Valid @RequestBody ReqDriver reqDriver, @CurrentUser Users users) {
        return userService.creatDriver(users, reqDriver);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findUser(@PathVariable("id") Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new org.springframework.data.rest.webmvc.ResourceNotFoundException(id.toString()));
    }

    @GetMapping("/workerProfile/{id}")
    public HttpEntity<?> workerProfile(@PathVariable Long id) {
        Optional<Users> byId = userRepository.findUsersById(id);
        return ResponseEntity.ok(new ApiResponseModel(true, "Worker Profile", byId.orElseThrow(() -> new ResourceNotFoundException("user", "id", id))));
    }

    @GetMapping("/sentPointToWorker/{id}")
    public HttpEntity<?> sentPointToWorker(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponseModel(true, "Worker Profile", userService.sentPointToWorker(id)));
    }

    @GetMapping("/avgPointClient/{id}")
    public HttpEntity<?> point(@PathVariable Long id) {
        return ResponseEntity.ok(userService.avgFeedBack(id));
    }

    @GetMapping("/avgPointWorker/{id}")
    public HttpEntity<?> pointr(@PathVariable Long id) {
        return ResponseEntity.ok(userService.avgFeedBack1(id));
    }

    @GetMapping("/AveragePoint/{user}")
    public HttpEntity<?> sumWorkerPoint(@PathVariable String user) {
        return ResponseEntity.ok(userService.avgPoints(user));

    }

}
