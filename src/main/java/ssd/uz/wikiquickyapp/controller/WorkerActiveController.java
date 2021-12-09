package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ReqWorkerActive;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.service.WorkerActiveService;
import ssd.uz.wikiquickyapp.utils.AppConstants;

import javax.jws.soap.SOAPBinding;

@RestController
@RequestMapping("/api/workerActive")
public class WorkerActiveController {

    @Autowired
    WorkerActiveService workerActiveService;

    @PostMapping
    public HttpEntity<?> saveOrEditWorkerActive(@CurrentUser Users users, @RequestBody ReqWorkerActive reqWorkerActive) {
        ApiResponse apiResponse = workerActiveService.saveOrEditWorkerActive(users, reqWorkerActive);
        return ResponseEntity.status(apiResponse.isSuccess() ? apiResponse.getMessage().equals("saved") ?
                HttpStatus.CREATED : HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> pageable(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                  @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return ResponseEntity.ok(workerActiveService.pageable(page, size));
    }

    @DeleteMapping("/remove")
    public ApiResponse removeController(@CurrentUser Users users) {
        return workerActiveService.removeWorkerActive(users);
    }

    @PostMapping("/get/worker")
    public HttpEntity<?> getWorkers(@CurrentUser Users users, @RequestParam("lan") Double lan,@RequestParam("lat") Double lat) {
        ApiResponse apiResponse = workerActiveService.getWorkerAtive(users,lan,lat);
        return ResponseEntity.status(apiResponse.isSuccess() ? apiResponse.getMessage().equals("saved") ?
                HttpStatus.CREATED : HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

}
