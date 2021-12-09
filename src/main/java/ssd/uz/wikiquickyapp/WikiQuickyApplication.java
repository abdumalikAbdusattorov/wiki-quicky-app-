package ssd.uz.wikiquickyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WikiQuickyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiQuickyApplication.class, args);
    }

}
