package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.service.CompaniyService;

@RestController
@RequestMapping("/api/company")
public class CompaniyController {

    @Autowired
    CompaniyService companiyService;

    @GetMapping
    public HttpEntity<?> getAllCom(){
        ApiResponseModel responseModel = companiyService.getAllCompanies();
        return ResponseEntity.ok(responseModel);
    }

}
