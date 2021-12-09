package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.projection.ReqLoadType;
import ssd.uz.wikiquickyapp.service.LoadTypeService;
import ssd.uz.wikiquickyapp.utils.AppConstants;

@RestController
@RequestMapping("/api/loadtype")
public class LoadTypeController {
    @Autowired
    LoadTypeService loadTypeService;

    @PostMapping
    public HttpEntity<?> saveOrEdit(@RequestBody ReqLoadType reqLoadType) {
        ApiResponse response = loadTypeService.saveOrEdit(reqLoadType);
        return ResponseEntity.status(response.isSuccess() ? response.getMessage().equals("Saved") ? HttpStatus.CREATED : HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    @DeleteMapping("{id}")
    public ApiResponse removeController(@PathVariable Long id) {
        return loadTypeService.removeLoadType(id);
    }

    @GetMapping("/all")
    public ApiResponseModel getAll() {
        return loadTypeService.getAllLoadType();
    }
}


