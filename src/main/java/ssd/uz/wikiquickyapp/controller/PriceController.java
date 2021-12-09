package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqAddress;
import ssd.uz.wikiquickyapp.payload.ResTulov;
import ssd.uz.wikiquickyapp.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/api/price")
public class PriceController {

    @Autowired
    LocationService locationService;

    @PostMapping
    public ApiResponseModel showPrices(@RequestBody ReqAddress reqAddress) {
        return locationService.showPrice(reqAddress);
    }

}
