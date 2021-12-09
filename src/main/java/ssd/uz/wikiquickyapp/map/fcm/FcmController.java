package ssd.uz.wikiquickyapp.map.fcm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/fcm")
public class FcmController {

    @Autowired
    FcmService fcmService;

    @PostMapping
    public void getFcm(@RequestParam("token") String token) throws IOException {
        fcmService.sendMessage2(token);
    }

}
