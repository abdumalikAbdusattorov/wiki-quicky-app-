package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.service.WorkerService;

import java.io.IOException;

@RestController
@RequestMapping("/api/workerActive")
public class WorkerController {

    @Autowired
    WorkerService workerService;

    @GetMapping("/getOrderActive/{orderActiveId}")
    public HttpEntity<?> getOrderActive(@CurrentUser Users users, @PathVariable Long orderActiveId) throws IOException {
        ApiResponse response = workerService.getOrder(users, orderActiveId);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/getWorkerId/{id}")
    public ApiResponseModel getId(@PathVariable Long id){
        return workerService.getWorkerId(id);
    }
}


