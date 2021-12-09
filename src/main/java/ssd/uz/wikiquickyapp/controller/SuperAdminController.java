package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.SuperAdminValuesRepository;
import ssd.uz.wikiquickyapp.service.ExcelService;
import ssd.uz.wikiquickyapp.service.SuperAdminService;
import ssd.uz.wikiquickyapp.utils.AppConstants;


@RestController
//@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")
@RequestMapping("/api")
public class SuperAdminController {
    @Autowired
    SuperAdminService superAdminService;
    @Autowired
    SuperAdminValuesRepository superAdminValuesRepository;
    @Autowired
    ExcelService excelService;

    /**************** ADMIN ***************/

    @PostMapping("/admin")
    public HttpEntity<?> createAdmin(@RequestBody ReqSignUp reqSignUp) {
        ApiResponse response = superAdminService.createAdmin(reqSignUp);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @PutMapping("/admin")
    public HttpEntity<?> editAdmin(@RequestBody ReqSignUp reqSignUp) {
        ApiResponse response = superAdminService.editAdmin(reqSignUp);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @DeleteMapping("/admin/{adminId}")
    public HttpEntity<?> deleteAdmin(@PathVariable Long adminId) {
        ApiResponse response = superAdminService.deleteAdmin(adminId);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/admin")
    public HttpEntity<?> getAllAdmins() {
        ApiResponseModel response = superAdminService.getAllAdmins();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/{id}")
    public HttpEntity<?> getAdmin(@PathVariable Long id) {
        ApiResponseModel response = superAdminService.getAdmin(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/blockOrUnblock/{adminId}")
    public HttpEntity<?> blockOrUnblockAdmin(@PathVariable Long adminId) {
        ApiResponse response = superAdminService.blockOrUnblockAdmin(adminId);
        return ResponseEntity.ok(response);
    }


    /**************** VALUES ***************/
    @PostMapping("/values")
    public HttpEntity<?> addValues(@RequestBody ReqValues reqValues) {
        ApiResponse response = superAdminService.addValues(reqValues);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @PatchMapping("/values/{valueId}")
    public HttpEntity<?> editValues(@PathVariable Long valueId, @RequestBody ReqValues reqValues) {
        ApiResponseModel response = superAdminService.editValues(valueId, reqValues);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/values/{valueId}")
    public HttpEntity<?> getValue(@PathVariable Long valueId) {
        return ResponseEntity.ok(superAdminService.getValue(valueId));
    }

    @GetMapping("/values")
    public HttpEntity<?> getValues() {
        return ResponseEntity.ok(superAdminService.getValues());
    }

    @DeleteMapping("/values/{valueId}")
    public HttpEntity<?> deleteValues(@PathVariable Long valueId) {
        ApiResponse response = superAdminService.deleteValue(valueId);
        return ResponseEntity.ok(response);
    }

    /**************** VEHICLE TYPE ***************/

    @PostMapping("/vehicleType")
    public HttpEntity<?> addOrEditVehicleType(@RequestBody ReqVehicleType reqVehicleType) {
        ApiResponseModel response = superAdminService.addOrEditVehicleType(reqVehicleType);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/vehicleType/{id}")
    public HttpEntity<?> getVehicleType(@PathVariable Long id) {
        ReqVehicleType response = superAdminService.getVehicleType(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicleType")
    public HttpEntity<?> getVehicleTypes() {
        ApiResponseModel response = superAdminService.getVehicleTypes();
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @DeleteMapping("/vehicleType/{id}")
    public HttpEntity<?> deleteVehicleType(@PathVariable Long id) {
        ApiResponse response = superAdminService.deleteVehicleType(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    /************************ Reports *************************/
    @PostMapping("/report/getAllUsersCount") //Vaqt oraligida barcha userlar soni
    public HttpEntity<?> getAllUsersCountInTimeInterval(@RequestParam(name = "startDate", defaultValue = AppConstants.BEGIN_DATE) String startTime,
                                                        @RequestParam(name = "endDate", defaultValue = AppConstants.END_DATE) String endTime) {
        ApiResponseModel response = superAdminService.getAllUsersCountInTimeInterval(startTime, endTime);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @PostMapping("/report/getUsers") //Vaqt oraligida sistemaga qoshilgan worker yoki clientlani koriw
    public HttpEntity<?> getUsers(@RequestParam(name = "startDate", defaultValue = AppConstants.BEGIN_DATE) String startTime,
                                  @RequestParam(name = "endDate", defaultValue = AppConstants.END_DATE) String endTime,
                                  @RequestParam(name = "roleName") String roleName,
                                  @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                  @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                  @RequestParam(name = "search", defaultValue = "all") String search) {
        ApiResponseModel response = superAdminService.getUsers(startTime, endTime, RoleName.valueOf(roleName), page, size, search);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @PostMapping("/report/getUsersCount") //Vaqt oraligida sistemaga qoshilgan worker yoki clientlani sonini koriw
    public HttpEntity<?> getUsersCount(@RequestParam(name = "startDate", defaultValue = AppConstants.BEGIN_DATE) String startTime,
                                       @RequestParam(name = "endDate", defaultValue = AppConstants.END_DATE) String endTime,
                                       @RequestParam String roleName) {
        ApiResponseModel response = superAdminService.getUsersCount(startTime, endTime, RoleName.valueOf(roleName));
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }


    @PostMapping("/report/getOrdersCount") //Vaqt oraligida jami zakazlar soni
    public HttpEntity<?> getAllOrderCount(@RequestParam(name = "startDate", defaultValue = AppConstants.BEGIN_DATE) String startTime,
                                          @RequestParam(name = "endDate", defaultValue = AppConstants.END_DATE) String endTime) {
        ApiResponseModel response = superAdminService.getOrdersCount(startTime, endTime);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @PostMapping("/report/getOrdersCountByVehicleType") //Vaqt oraligida vehicle_type boyicha zakazlar soni
    public HttpEntity<?> getAllOrderCountByVehicleType(@RequestParam(name = "startDate", defaultValue = AppConstants.BEGIN_DATE) String startTime,
                                                       @RequestParam(name = "endDate", defaultValue = AppConstants.END_DATE) String endTime,
                                                       @RequestParam(name = "vehicleType") String vehicleType) {
        ApiResponseModel response = superAdminService.getOrdersCountByVehicleType(startTime, endTime, vehicleType);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/report/excel/users")
    public HttpEntity<?> getUsersExcel() {
        return excelService.getUsers();
    }

    @GetMapping("/report/excel/orders")
    public HttpEntity<?> getOrdersExcel() {
        return excelService.getOrders();
    }

}

