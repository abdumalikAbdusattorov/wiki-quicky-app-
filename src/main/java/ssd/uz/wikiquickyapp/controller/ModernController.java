package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.service.ModerService;


@RestController
@RequestMapping("/api/modern")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class ModernController {

    @Autowired
    ModerService moderService;

    @GetMapping
    public HttpEntity<?> getByPageable(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                       @RequestParam(name = "size", defaultValue = "12") Integer size,
                                       @RequestParam(name = "status") String status,
                                       @RequestParam(name = "vehicleType") Integer vehicleType) {
        return ResponseEntity.ok(moderService.getByPageable(page, size, status, vehicleType));
    }

    @PutMapping("/{id}")
    public HttpEntity<?> changeIsCheckedWorker(@PathVariable Long id,
                                               @RequestParam boolean IsCheckedWorker,
                                               @RequestParam(name = "commit", defaultValue = "null") String commit) {
        ApiResponse apiResponse = null;
        if (IsCheckedWorker) {
            apiResponse = moderService.changeIsCheckedWorker(id);
        } else {
            apiResponse = moderService.changeFalseAndCommit(id, commit);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneUser(@PathVariable Long id) {
        return ResponseEntity.ok(moderService.getOneUser(id));
    }

    @GetMapping("/commit")
    public HttpEntity<?> getCommit(@PathVariable Long id) {
        return ResponseEntity.ok(moderService.getCommits(id));
    }
}