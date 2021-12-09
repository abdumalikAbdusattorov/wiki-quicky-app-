package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.Vehicle;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqVehicle;
import ssd.uz.wikiquickyapp.payload.ResPageable;
import ssd.uz.wikiquickyapp.payload.ResVehicle;
import ssd.uz.wikiquickyapp.repository.*;
import ssd.uz.wikiquickyapp.utils.CommonUtils;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    VehiclePhotoRepository vehiclePhotoRepository;
    @Autowired
    UserRepository userRepository;

    public ApiResponseModel addOrEditVehicle(ReqVehicle reqVehicle) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            Vehicle vehicle = new Vehicle();
            responseModel.setMessage("Vehicle added");
            if (reqVehicle.getId() != null) {
                vehicle = vehicleRepository.findById(reqVehicle.getId()).orElseThrow(() -> new ResourceNotFoundException("VehicleRepository", "id", reqVehicle.getId()));
                responseModel.setMessage("Vehicle edited");
            }
            vehicle.setName(reqVehicle.getName());
            vehicle.setCarNumber(reqVehicle.getCarNumber());
            vehicle.setColor(reqVehicle.getColor());
            vehicle.setVehicleType(vehicleTypeRepository.findById(reqVehicle.getVehicleTypeId()).orElseThrow(() -> new ResourceNotFoundException("vehicleTypeRepository", "VehicleTypeId", reqVehicle.getVehicleTypeId())));
            vehicle.setVehiclePhoto(vehiclePhotoRepository.findById(reqVehicle.getVehiclePhotoId()).orElseThrow(() -> new ResourceNotFoundException("vehicle photo", "id", reqVehicle.getVehiclePhotoId())));
            vehicleRepository.save(vehicle);
            responseModel.setSuccess(true);
            ResVehicle resVehicle = new ResVehicle(
                    vehicle.getId(),
                    vehicle.getName(),
                    vehicle.getVehicleType().getId(),
                    vehicle.getVehiclePhoto().getId(),
                    vehicle.getCarNumber(),
                    vehicle.getColor()
            );
            responseModel.setData(resVehicle);
        } catch (Exception e) {
            responseModel.setMessage("Error");
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

    public ResVehicle getVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));
        return new ResVehicle(vehicle.getId(),
                vehicle.getName(),
                vehicle.getVehicleType().getId(),
                vehicle.getVehiclePhoto().getId(),
                vehicle.getColor(),
                vehicle.getCarNumber());
    }

//    public ApiResponseModel getVehicles() {
//        List<ResVehicle> vehicles = vehicleRepository.findAll().stream().map(vehicle -> getVehicle(vehicle.getId())).collect(Collectors.toList());
//        return new ApiResponseModel(true,"found", vehicles);
//    }

    public ApiResponseModel deleteVehicle(Long id) {
        if (vehicleRepository.existsById(id)) {
            Users user = userRepository.findByVehicle(vehicleRepository.getOne(id));
            if (user != null) {
                user.setVehicle(null);
                userRepository.save(user);
                vehicleRepository.deleteById(id);
            } else {
                vehicleRepository.deleteById(id);
            }
            return new ApiResponseModel(true, "vehicle deleted");

        }
        return new ApiResponseModel(false, "vehicle not found");
    }

    public ResPageable pageable(Integer page, Integer size, String search) {
        Page<Vehicle> vehicles = vehicleRepository.findAll(CommonUtils.getPageableById(page, size));
//        vehicles.get().map(vehicle -> {
//            ResVehicle resVehicle = new ResVehicle(
//                    vehicle.getId(),
//                    vehicle.getName(),
//                    vehicle.getVehicleType().getId(),
//                    vehicle.getVehiclePhoto().getId(),
//                    vehicle.getCarNumber(),
//                    vehicle.getColor());
//
//            return resVehicle;
//        });
        if (!search.equals("all")) {
            vehicles = vehicleRepository.findAllByNameStartingWithIgnoreCase(search, CommonUtils.getPageableById(page, size));
        }
        return new ResPageable(vehicles.getContent(), vehicles.getTotalElements(), page);
    }
}
