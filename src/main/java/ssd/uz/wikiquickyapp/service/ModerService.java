package ssd.uz.wikiquickyapp.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.collection.UsersCol;
import ssd.uz.wikiquickyapp.entity.Description;
import ssd.uz.wikiquickyapp.entity.Role;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.Vehicle;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.repository.*;


import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ModerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DescriptionRepository descriptionRepository;

    @Autowired
    ModerService moderService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    VehicleRepository vehicleRepository;

    public ApiResponseModel getByPageable(Integer page, Integer size, String status, Integer vehicleType) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            if (vehicleType != null) {
                responseModel.setSuccess(true);
                responseModel.setMessage("Found");
            } else if (vehicleType == 0) {
                responseModel.setSuccess(false);
                responseModel.setMessage("Bunday vehicle type mavjud emas ‼️");
            } else {
                responseModel.setSuccess(false);
                responseModel.setMessage("Vehicletype bo`lishi shart ⚠️");
            }
            if (!status.equals("null")) {
                List<Users> checkedUsers = userRepository.findAllByPageableStatus(Long.valueOf(vehicleType), status.equals("true"), page > 0 ? page * size : 0, size);
                responseModel.setData(checkedUsers);
            } else {
                List<Users> unCheckedUsers = userRepository.findAllByPageableNull(Long.valueOf(vehicleType), page > 0 ? page * size : 0, size);
                responseModel.setData(unCheckedUsers);
            }

            return responseModel;
        } catch (Exception e) {
            return new ApiResponseModel(false, "Error:)");
        }
    }

    public ApiResponseModel getOneUser(Long id) {
        try {
            Optional<Users> user = userRepository.findUsersById(id);
            if (user.isPresent()) {
                return new ApiResponseModel(true, "", user);
            } else {
                return new ApiResponseModel(false, !user.isPresent() ? id <= 0 ? "ukam hazzlashma :)" : "User mavjud emas" : "Error");
            }
        } catch (Exception e) {
            return new ApiResponseModel(false, "Aniqlanmagan xatolik !");
        }
    }

    public ApiResponse changeIsCheckedWorker(Long id) {
        try {
            Optional<Users> users = userRepository.findById(id);
            if (users.isPresent()) {
                Users user = users.get();
                user.setIsCheckedWorker(true);
                List<Role> roles = user.getRoles();
                roles.add(roleRepository.findByName(RoleName.ROLE_WORKER).get());
                userRepository.save(users.get());
                return new ApiResponse("Tasdiqlandingiz 😋", true);
            } else {
                return new ApiResponse("Bunday user mavjud emas !", false);
            }
        } catch (Exception e) {
            return new ApiResponse("Aniqlanmagan xatolik ⚠️!", false);
        }
    }

    public ApiResponse changeFalseAndCommit(Long id, String commit) {
        try {
            if (id != null && commit != null && commit.length() > 10) {
                Optional<Users> users = userRepository.findById(id);
                if (users.isPresent()) {
                    Users user = users.get();
                    user.setIsCheckedWorker(false);
                    descriptionRepository.save(new Description(commit, user.getId(), false));
                    userRepository.save(user);
                    ApiResponse response = moderService.removeExpire(user.getId());
                    return response;
                } else {
                    return new ApiResponse("User mavjud emas", false);
                }
            } else {
                return new ApiResponse(id == null ? id <= 0 ? commit.length() < 10 ? commit == null ?
                        "commit bo`lishi shart ❗️" : "commit 10ta harfdan ko`proq bo`lishi kerak ‼️"
                        : "Id 0 bo`lishi mumkin emas ⚠️" : "id bo`lishi shart" : "Error", false);
            }
        } catch (Exception e) {
            return new ApiResponse("Aniqlanmagan xatolik ❌", false);
        }
    }

    public ApiResponseModel getCommits(Long id) {
        Optional<Description> massages = descriptionRepository.findByAnswerAndUserId(false, id);
        if (massages.isPresent()) {
            return new ApiResponseModel(true, "Ok :)", massages);
        } else {
            return new ApiResponseModel(false, "Sizga bahar yoq :)");
        }
    }

    @SneakyThrows
    public ApiResponse removeExpire(Long id) {
        ApiResponse response = new ApiResponse();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Users user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));

                descriptionRepository.getDescriptionIds(id).forEach(descId -> descriptionRepository.deleteById(descId));

                Vehicle vehicle = vehicleRepository.findById(user.getVehicle().getId()).orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", user.getVehicle().getId()));
//                vehicleRepository.deleteById(vehicle.getId());
                vehicle.setActive(false);
                vehicleRepository.save(vehicle);

                user.getPassportPhotos().forEach(attachment -> attachmentService.deleteAttachment(attachment.getId()));

                user.setPassportPhotos(null);

                user.setIsCheckedWorker(null);

                userRepository.save(user);

                response.setMessage("vehicle bn passport photos ochirildi");
                response.setSuccess(true);
                timer.cancel();
            }
        };

        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
        response.setMessage("Moderator tekshiruvidan o'tdi, haydovchi statusi(is_checked_worker) false");
        return response;
    }
}