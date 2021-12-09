package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ReqOrderActive;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.service.OrderActiveService;

import java.io.IOException;

@RestController
@RequestMapping("/api/orderActive")
public class OrderActiveController {

    @Autowired
    OrderActiveService orderActiveService;

    @PostMapping("/save")
    public HttpEntity<?> saveOrderActive(@RequestBody ReqOrderActive reqOrderActive, @CurrentUser Users users) throws IOException {
        ApiResponse response = orderActiveService.addOrderActive(reqOrderActive, users);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

//    @GetMapping("/{orderActiveId}")
//    public ApiResponseModel getOrderActive(@CurrentUser Users users, @PathVariable Long orderActiveId) throws IOException {// workerActive ning id si
//        return orderActiveService.getOrderActive(users, orderActiveId);
//    }
}
