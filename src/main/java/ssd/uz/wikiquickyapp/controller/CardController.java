package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqCard;
//import ssd.uz.wikiquickyapp.service.CardService;

//@RestController
//@RequestMapping("/card")
//public class CardController {
//    @Autowired
//    CardService cardService;

//    @PostMapping
//    public HttpEntity<?> addCard(@RequestBody ReqCard reqCard){
//        ApiResponseModel response = cardService.addCard(reqCard);
//        return ResponseEntity.status(response.isSuccess()? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
//    }
//
//    @GetMapping("/{id}")
//    public HttpEntity<?> getCard(@PathVariable Long id){
//        ApiResponseModel response = cardService.getCard(id);
//        return ResponseEntity.status(response.isSuccess()? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public HttpEntity<?> deleteCard(@PathVariable Long id){
//        ApiResponse response = cardService.deleteCard(id);
//        return ResponseEntity.status(response.isSuccess()? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
//    }

//}
