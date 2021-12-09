package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ReqFeedBack;
import ssd.uz.wikiquickyapp.security.CurrentUser;
import ssd.uz.wikiquickyapp.service.FeedBackService;

@RestController
@RequestMapping("/api/FeedBack")
public class FeedbackController {
    @Autowired
    FeedBackService feedBackService;

    @PostMapping
    public HttpEntity<?> saveOrEdit(@RequestBody ReqFeedBack reqFeedBack, @CurrentUser Users users) {
        ApiResponse response = feedBackService.saveOrEdit(reqFeedBack, users);
        return ResponseEntity.status(response.isSuccess() ? response.getMessage().equals("Saved") ? HttpStatus.CREATED : HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

//    @GetMapping
//    public HttpEntity<?> getAvgRating(@RequestParam String user){
//        ApiResponseModel responseModel = feedBackService.getAvgRating(user);
//        return ResponseEntity.status(responseModel.isSuccess() ? HttpStatus.CREATED: HttpStatus.CONFLICT).body(responseModel);
//    }


    @DeleteMapping("/{id}")
    public ApiResponse removeFeedback(@PathVariable Long id) {
        return feedBackService.removeFeedBack(id);
    }
}
