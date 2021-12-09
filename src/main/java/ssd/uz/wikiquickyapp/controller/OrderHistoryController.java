package ssd.uz.wikiquickyapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.service.ClientOrderHistoryService;
import ssd.uz.wikiquickyapp.utils.AppConstants;


@RestController
@RequestMapping("/api/orderHistoryForClient")
public class OrderHistoryController {
    @Autowired
    ClientOrderHistoryService historyService;

    @GetMapping("/pageSize1")
    public HttpEntity<?> getOrdersByOrderStatus(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @CurrentUser Users users
    ) {
        return ResponseEntity.ok(historyService.getOrderHistory(users, page, size));
    }

    @GetMapping("/pageSize2")
    public HttpEntity<?> getOrdersByOrderStatusLByTime(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(value = "dateFrom") String dateFrom,
            @RequestParam(value = "dateTo") String dateTo,
            @CurrentUser Users users
    ) {
        return ResponseEntity.ok(historyService.getOrderHistoryByTime(users, page, size,dateFrom,dateTo));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOrderHistory(@PathVariable Long id){
        ApiResponse response = historyService.deleteOrderHistory(id);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/pageSize2")
//    public HttpEntity<?> getOrdersByOrderStatus2(
//            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
//            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
//            @CurrentUser Users users) {
//        return ResponseEntity.ok(historyService.getOrderHistory2(users, page, size));
//    }
}
