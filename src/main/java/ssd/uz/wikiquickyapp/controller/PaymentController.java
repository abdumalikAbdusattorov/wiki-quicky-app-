package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.collection.WorkerBalanceChangeCol;
import ssd.uz.wikiquickyapp.entity.WorkerBalanceChange;
import ssd.uz.wikiquickyapp.payload.ReqCompanyInterest;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ReqBalanceChange;
import ssd.uz.wikiquickyapp.service.PaymentService;
import ssd.uz.wikiquickyapp.utils.AppConstants;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

//    @PostMapping("/companyInterest")
//    public HttpEntity<?> getCompanyInterestFromOrder(@RequestBody ReqCompanyInterest reqCompanyInterest){
//        ApiResponse response = paymentService.getCompanyInterestFromOrder(reqCompanyInterest);
//        return ResponseEntity.status(response.isSuccess()? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
//    }

    @GetMapping("/companyInterest")
    public HttpEntity<?> getAllPayments(@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                        @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                        @RequestParam(name = "search", defaultValue = "all") String search) {
        return ResponseEntity.ok(paymentService.getAllPaymentByPageable(page, size, search));
    }

    @GetMapping("/companyInterest/{id}")
    public HttpEntity<?> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @PostMapping("/balanceChange")
    public HttpEntity<?> replenishTheBalance(@RequestBody ReqBalanceChange reqBalanceChange) {
        ApiResponse response = paymentService.replenishTheBalance(reqBalanceChange);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/balanceChange")
    public HttpEntity<?> getAllBalanceChanges(@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                              @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                              @RequestParam(name = "search", defaultValue = "all") String search) {
        return ResponseEntity.ok(paymentService.getAllBalanceChanges(page, size, search));
    }

    @GetMapping("/balanceChange/{id}")
    public HttpEntity<?> getBalanceChange(@PathVariable Long id) {
        WorkerBalanceChangeCol workerBalanceChange = paymentService.getBalanceChangeCol(id);
        return ResponseEntity.ok(workerBalanceChange);
    }

    @PutMapping("/balanceChange")
    public HttpEntity<?> editBalanceChange(@RequestBody ReqBalanceChange reqBalanceChange) {
        ApiResponse response = paymentService.editBalanceChange(reqBalanceChange);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @DeleteMapping("/balanceChange/{id}")
    public HttpEntity<?> deleteBalanceChange(@PathVariable Long id) {
        ApiResponse response = paymentService.deleteBalanceChange(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }
}
