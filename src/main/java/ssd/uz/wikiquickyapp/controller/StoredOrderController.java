package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqStoredOrder;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.service.StoredOrderService;

@RestController
@RequestMapping("/api/storedOrder")
public class StoredOrderController {

    @Autowired
    StoredOrderService orderService;

    @PostMapping("/saveOrEdit")
    public HttpEntity<?> saveOrderActive(@RequestBody ReqStoredOrder reqStoredOrder, @CurrentUser Users users) {
        ApiResponse response = orderService.saveStoredOrder(users,reqStoredOrder);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable Long id) {
        return orderService.deleteStoredOrder(id);
    }

//    @GetMapping("getAllByUser")
//    public ApiResponseModel getAll(@CurrentUser Users users){
//        return orderService.getAllStoredOrder(users);
//    }

}
