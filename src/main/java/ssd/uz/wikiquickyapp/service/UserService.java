package ssd.uz.wikiquickyapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqDriver;
import ssd.uz.wikiquickyapp.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkerActiveRepository workerActiveRepository;

    @Autowired
    OrderActiveRepository orderActiveRepository;

    @Autowired
    WorkerService workerService;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RoleRepository roleRepository;

    public Long sentPointToWorker(Long workerId) {
//        Users worker = userRepository.findById(workerId).get();
        Long sum = null;
        for (int i = 0; i < feedBackRepository.findFeedBack(workerId).size(); i++) {
            sum += feedBackRepository.findFeedBack(workerId).get(i).getRating();
        }
        return sum / feedBackRepository.count();
    }

    public Long sentPointToClient(Long clientId) {
//        Users client = userRepository.findById(clientId).get();
        Long sum = null;
        for (int j = 0; j < feedBackRepository.findFeedBack1(clientId).size(); j++) {
            sum += feedBackRepository.findFeedBack1(clientId).get(j).getRating();
        }
        return sum / feedBackRepository.count();
    }

    public ApiResponse creatDriver(Users users, ReqDriver reqDriver) {
        if (users.getId() == null) return null;
        if (userRepository.existsByEmail(reqDriver.getEmail())) {
            return new ApiResponse("Bunday email ruyxatdan utgan,lekin email muhim emas", false);
        } else {
            users.setEmail(reqDriver.getEmail());
        }
        List<Attachment> attachments = new ArrayList<>();
        for (Long passportPhoto : reqDriver.getPassportPhotos()) {
            attachments.add(attachmentRepository.getOne(passportPhoto));
        }
        users.setDateOfBirth(reqDriver.getDateOfBirth());
        users.setPassportPhotos(attachments);
        users.setLivingAddress(reqDriver.getLivingAddress());
        if (reqDriver.getVehicleId()!=null){
            users.setVehicle(vehicleRepository.getOne(reqDriver.getVehicleId()));
        }
        if (users.getCompany()!=null){
            users.setIsCheckedWorker(true);
        }else {
            users.setIsCheckedWorker(null);
        }
        users.setComplateSUworker(true);
        userRepository.save(users);
        return new ApiResponse("ok", true);
    }

    public ApiResponseModel avgFeedBack(Long id) {
        return new ApiResponseModel(true, "ok", feedBackRepository.pointr(id));

    }

    public ApiResponseModel avgFeedBack1(Long id) {
        return new ApiResponseModel(true, "ok", feedBackRepository.point(id));
    }

    public ApiResponseModel avgPoints(String user) {
        ApiResponseModel response = new ApiResponseModel();
        String userType = "client";
        if (user.equals("client")) {
            userType = "worker";
        }
        try {
            List<Object> list = new ArrayList<>();
            List<Map<String, Object>> maps = jdbcTemplate.queryForList("Select client, count(client),sum(rating)/count(client) from feed_back  where from_who='" + userType + "' GROUP BY client;");
            int i = 0;
            int size = maps.size();
            while (i != size) {
                Map<String, Object> item = maps.get(i);
                //               System.out.println(item);
                list.add(item);

                i++;
            }
            response.setData(list);
        } catch (Exception e) {
            response.setMessage("user points not found");
            response.setSuccess(false);

        }
        return response;
    }

    public List<Users> findAll() {
        log.info("retrieving all users");
        return userRepository.findAll();
    }

    public Optional<Users> findById(Long id) {
        log.info("retrieving user {}", id);
        return userRepository.findById(id);
    }

}
