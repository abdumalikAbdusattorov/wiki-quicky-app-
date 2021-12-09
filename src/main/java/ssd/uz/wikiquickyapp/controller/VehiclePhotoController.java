package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqVehiclePhoto;
import ssd.uz.wikiquickyapp.service.VehiclePhotoService;

@RestController
@RequestMapping("/api/vehiclePhoto")
public class VehiclePhotoController {
    @Autowired
    VehiclePhotoService vehiclePhotoService;

    @PostMapping
    public HttpEntity<?> addVehiclePhoto(@RequestBody ReqVehiclePhoto reqVehiclePhoto) {
        ApiResponse response = vehiclePhotoService.addOrEditVehiclePhoto(reqVehiclePhoto);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getVehiclePhoto(@PathVariable Long id) {
        return ResponseEntity.ok(vehiclePhotoService.getVehiclePhoto(id));
    }

    @GetMapping
    public HttpEntity<?> getVehiclePhotos() {
        return ResponseEntity.ok(vehiclePhotoService.getVehiclePhotos());
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteVehiclePhoto(@PathVariable Long id) {
        ApiResponse response = vehiclePhotoService.deleteVehiclePhoto(id);
        return ResponseEntity.ok(response);
    }
}
