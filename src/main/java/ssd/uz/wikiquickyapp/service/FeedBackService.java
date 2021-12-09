package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.FeedBack;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ApiResponse;

import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqFeedBack;
import ssd.uz.wikiquickyapp.repository.FeedBackRepository;
import ssd.uz.wikiquickyapp.repository.OrderRepository;
import ssd.uz.wikiquickyapp.repository.UserRepository;
import ssd.uz.wikiquickyapp.repository.WorkerActiveRepository;

import java.awt.*;

@Service
public class FeedBackService {
    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WorkerActiveRepository workerActiveRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;


    public ApiResponse saveOrEdit(ReqFeedBack reqFeedBack, Users users) {
        FeedBack feedBack = new FeedBack();
        Long client;
        Long worker;
        if (users.getIsCheckedWorker()) {
            worker = users.getId();
            client = reqFeedBack.getTo();
            feedBack.setFromWho("worker");
        } else {
            client = users.getId();
            worker = reqFeedBack.getTo();
            feedBack.setFromWho("client");
        }
        feedBack.setClient(client);
        feedBack.setWorker(worker);
        feedBack.setDescription(reqFeedBack.getDescription());
        feedBack.setRating(reqFeedBack.getRating());
        feedBackRepository.save(feedBack);

        Users worker1 = userRepository.getOne(worker);

        if (worker1.getPoints() == null) {
            worker1.setPoints(0.0);
        }

        if (reqFeedBack.getRating() == 1) {
            if (worker1.getPoints() > 2) {
                worker1.setPoints(worker1.getPoints() - 2);
            }
            worker1.setPoints(0.0);

        } else if (reqFeedBack.getRating() == 2) {
            if (worker1.getPoints() > 1) {
                worker1.setPoints(worker1.getPoints() - 1);
            }
            worker1.setPoints(0.0);

        } else if (reqFeedBack.getRating() == 3) {

            return new ApiResponse("saved", true, TrayIcon.MessageType.INFO);

        } else if (reqFeedBack.getRating() == 4) {

            worker1.setPoints(worker1.getPoints() + 1);

        } else if (reqFeedBack.getRating() == 5) {

            worker1.setPoints(worker1.getPoints() + 2);
        }
        userRepository.save(worker1);

        return new ApiResponse("saved", true, null);
    }

    public ApiResponse removeFeedBack(Long id) {
        try {
            feedBackRepository.deleteById(id);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }

    }
}
