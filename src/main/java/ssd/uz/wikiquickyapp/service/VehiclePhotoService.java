package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.Attachment;
import ssd.uz.wikiquickyapp.entity.Vehicle;
import ssd.uz.wikiquickyapp.entity.VehiclePhoto;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqVehiclePhoto;
import ssd.uz.wikiquickyapp.repository.AttachmentRepository;
import ssd.uz.wikiquickyapp.repository.VehiclePhotoRepository;
import ssd.uz.wikiquickyapp.repository.VehicleRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiclePhotoService {
    @Autowired
    VehiclePhotoRepository vehiclePhotoRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    VehicleRepository vehicleRepository;

    public ApiResponseModel addOrEditVehiclePhoto(ReqVehiclePhoto reqVehiclePhoto) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            VehiclePhoto vehiclePhoto = new VehiclePhoto();
            responseModel.setMessage("Vehicle photo saved");
            if (reqVehiclePhoto.getId() != null) {
                vehiclePhoto = vehiclePhotoRepository.findById(reqVehiclePhoto.getId()).orElseThrow(() -> new ResourceNotFoundException("vehicle photo", "id", reqVehiclePhoto.getId()));
                responseModel.setMessage("Vehicle photo edited");
            }
            vehiclePhoto.setFrontSide(getAttachment(reqVehiclePhoto.getFrontSide()));
            vehiclePhoto.setLeftSide(getAttachment(reqVehiclePhoto.getLeftSide()));
            vehiclePhoto.setRightSide(getAttachment(reqVehiclePhoto.getRightSide()));
            vehiclePhoto.setBaggage(getAttachment(reqVehiclePhoto.getBaggage()));
            vehiclePhoto.setFrontLicense(getAttachment(reqVehiclePhoto.getFrontLicense()));
            vehiclePhoto.setBackLicense(getAttachment(reqVehiclePhoto.getBackLicense()));
            vehiclePhoto.setLicenseWithWorker(getAttachment(reqVehiclePhoto.getLicenseWithWorker()));
            vehiclePhoto.setTexPassportFront(getAttachment(reqVehiclePhoto.getTexPassportFront()));
            vehiclePhoto.setTexPassportBack(getAttachment(reqVehiclePhoto.getTexPassportBack()));
            responseModel.setSuccess(true);
            VehiclePhoto vehiclePhoto1 = vehiclePhotoRepository.save(vehiclePhoto);
            responseModel.setData(vehiclePhoto1.getId());
        } catch (Exception e) {
            responseModel.setMessage("Error");
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

    public Attachment getAttachment(Long id) {
        return attachmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("attachment", "id", id));
    }

    public ReqVehiclePhoto getVehiclePhoto(Long id) {
        VehiclePhoto vehiclePhoto = vehiclePhotoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("vehicle photo", "id", id));
        return new ReqVehiclePhoto(vehiclePhoto.getId(),
                vehiclePhoto.getFrontSide().getId(),
                vehiclePhoto.getLeftSide().getId(),
                vehiclePhoto.getRightSide().getId(),
                vehiclePhoto.getBaggage().getId(),
                vehiclePhoto.getFrontLicense().getId(),
                vehiclePhoto.getBackLicense().getId(),
                vehiclePhoto.getLicenseWithWorker().getId(),
                vehiclePhoto.getTexPassportFront().getId(),
                vehiclePhoto.getTexPassportBack().getId()
        );
    }

    public ApiResponseModel getVehiclePhotos() {
        List<ReqVehiclePhoto> vehiclePhotos = vehiclePhotoRepository.findAll().stream().map(vehiclePhoto -> getVehiclePhoto(vehiclePhoto.getId())).collect(Collectors.toList());
        return new ApiResponseModel(true, "Found", vehiclePhotos);
    }

    @Transactional
    public ApiResponse deleteVehiclePhoto(Long id) {
        ApiResponse response = new ApiResponse();
        if (vehiclePhotoRepository.existsById(id)) {
            try {
                Vehicle vehicle = vehicleRepository.getVehicleByVehiclePhoto(id);
                vehicle.setVehiclePhoto(null);
                vehicleRepository.save(vehicle);
                vehiclePhotoRepository.deleteById(id);
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMessage(e.getMessage());
                return response;
            }
            response.setSuccess(true);
            response.setMessage("VehiclePhoto deleted");
            return response;
        }
        response.setMessage("VehiclePhoto not found");
        response.setSuccess(false);
        return response;
    }

}
