package ssd.uz.wikiquickyapp.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.SignUpForBussiness;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.service.AdminService;
import ssd.uz.wikiquickyapp.utils.AppConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/getAllOrdersByCompany")
    public HttpEntity<?> getAllOrdersByCompany(
                                        @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                        @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                        @CurrentUser Users users) {
        ApiResponseModel response = adminService.getAllOrdersByCompany(users, page , size);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/getAllOrdersByCompanyAndStatus")
    public HttpEntity<?> getAllOrdersByCompanyByStatus(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @CurrentUser Users users,
            @RequestParam(name = "status") String status) {
        ApiResponseModel response = adminService.getAllCancelledOrdersByCompany(users, page , size,status);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

//    ***************

    @GetMapping("/getAllUsersByPage")
    public HttpEntity<?> getUsersByPage(@RequestParam(name = "startDate", defaultValue = AppConstants.BEGIN_DATE) String startTime,
                                        @RequestParam(name = "endDate", defaultValue = AppConstants.END_DATE) String endTime,
                                        @RequestParam("roleName") String roleName,
                                        @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                        @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                        @RequestParam(name = "search", defaultValue = "all") String search) {
        ApiResponseModel response = adminService.getUsersByPage(startTime, endTime, RoleName.valueOf(roleName), page, size, search);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/getUserInfo/{id}")
    public HttpEntity<?> getUserInfo(@PathVariable Long id) {
        ApiResponseModel responseModel = adminService.getUserInfo(id);
        return ResponseEntity.status(responseModel.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(responseModel);
    }

    @PutMapping("/topUpWorkerBalance/{id}")
    public HttpEntity<?> updateBalance(@PathVariable Long id, @RequestParam(name = "cost") Double cost) {
        ApiResponse apiResponse = adminService.updateBalance(id, cost);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @PutMapping("/getWorkerProfit/{id}")
    public HttpEntity<?> getWorkerProfit(@PathVariable Long id) {
        ApiResponseModel responseModel = adminService.getWorkerProfit(id);
        return ResponseEntity.status(responseModel.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(responseModel);
    }

    @PostMapping("/add/worker")
    public HttpEntity<?> saveUser(@RequestBody SignUpForBussiness signUpForBussiness, @CurrentUser Users users){
        ApiResponse apiResponse = adminService.addWorkersWithAdmin(signUpForBussiness,users);
        return ResponseEntity.ok(apiResponse);
    }
}