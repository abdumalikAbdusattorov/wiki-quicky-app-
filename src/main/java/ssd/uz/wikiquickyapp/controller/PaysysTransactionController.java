package ssd.uz.wikiquickyapp.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssd.uz.wikiquickyapp.service.PaysysTransactionService;


@RestController
@RequestMapping("/api/sandbox")
public class PaysysTransactionController {
    @Autowired
    PaysysTransactionService paysysTransactionService;

    public String convertToJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    @PostMapping(value = "/agr_info")
    public HttpEntity<?> getInfo(@RequestBody String jsonString) {
        return ResponseEntity.ok(convertToJson(paysysTransactionService.getInfo(jsonString)));
    }

    @PostMapping(value = "/agr_pay")
    public HttpEntity<?> pay(@RequestBody String jsonString) {
        return ResponseEntity.ok(convertToJson(paysysTransactionService.pay(jsonString)));
    }

    @PostMapping(value = "/agr_notify")
    public HttpEntity<?> notify(@RequestBody String jsonString) {
        return ResponseEntity.ok(convertToJson(paysysTransactionService.notify(jsonString)));
    }

    @PostMapping(value = "/agr_cancel")
    public HttpEntity<?> cancel(@RequestBody String jsonString) {
        return ResponseEntity.ok(convertToJson(paysysTransactionService.cancel(jsonString)));
    }

    @PostMapping(value = "/agr_statement")
    public HttpEntity<?> statement(@RequestBody String jsonString) {
        return ResponseEntity.ok(convertToJson(paysysTransactionService.statement(jsonString)));
    }

}
