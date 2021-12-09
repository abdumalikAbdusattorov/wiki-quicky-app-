package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.repository.AttachmentRepository;
import ssd.uz.wikiquickyapp.service.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/attach")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @PostMapping("/upload")
    public HttpEntity<?> uploadFile(MultipartHttpServletRequest request) throws IOException {
        ApiResponseModel apiResponseModel = attachmentService.uploadFile(request);
        return ResponseEntity.status(apiResponseModel.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponseModel);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable Long id) {
        return attachmentService.getAttachmentContent(id);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteFile(@PathVariable Long id) {
        ApiResponse apiResponse = attachmentService.deleteAttachment(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }
}
