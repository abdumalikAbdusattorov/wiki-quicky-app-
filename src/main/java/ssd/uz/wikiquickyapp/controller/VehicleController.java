package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqVehicle;
import ssd.uz.wikiquickyapp.payload.ResVehicle;
import ssd.uz.wikiquickyapp.service.VehicleService;
import ssd.uz.wikiquickyapp.utils.AppConstants;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @PostMapping
    public HttpEntity<?> addOrEditVehicle(@RequestBody ReqVehicle reqVehicle) {
        ApiResponseModel response = vehicleService.addOrEditVehicle(reqVehicle);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getVehicle(@PathVariable Long id) {
        ResVehicle response = vehicleService.getVehicle(id);
        return ResponseEntity.ok(response);
    }

//    @GetMapping()
//    public HttpEntity<?> getVehicles() {
//        ApiResponseModel response = vehicleService.getVehicles();
//        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
//    }

    @GetMapping
    public HttpEntity<?> pageable(@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                  @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                  @RequestParam(name = "search", defaultValue = "all") String search) {
        return ResponseEntity.ok(vehicleService.pageable(page, size, search));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteVehicle(@PathVariable Long id) {
        ApiResponseModel response = vehicleService.deleteVehicle(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

}
