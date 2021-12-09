package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.LoadType;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ResPageable;
import ssd.uz.wikiquickyapp.projection.ReqLoadType;
import ssd.uz.wikiquickyapp.repository.LoadTypeRepository;
import ssd.uz.wikiquickyapp.utils.CommonUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoadTypeService {
    @Autowired
    LoadTypeRepository loadTypeRepository;

    public ApiResponse saveOrEdit(ReqLoadType reqLoadType) {
        ApiResponse response = new ApiResponse();
        try {
            if (loadTypeRepository.existsAllByName(reqLoadType.getName())) {
                response.setMessage("Bunday nom mavjud boshqa nom kiriting");
                response.setSuccess(false);
                response.setMessageType(TrayIcon.MessageType.WARNING);
            } else if (reqLoadType.getName().length() == 0) {
                response.setMessage("Nomni kiriting");
                response.setSuccess(false);
                response.setMessageType(TrayIcon.MessageType.WARNING);
            } else if (!reqLoadType.getName().equals(loadTypeRepository.findByName(reqLoadType.getName()))) {
                response.setMessage("Qo'shildi");
                response.setSuccess(true);
                response.setMessageType(TrayIcon.MessageType.INFO);
                LoadType loadType = new LoadType();
                if (reqLoadType.getId() != null) {
                    loadType = loadTypeRepository.findById(reqLoadType.getId()).orElseThrow(() -> new ResourceNotFoundException("Load Type", "id", reqLoadType.getId()));
                    response.setMessage("O'zgartirildi");
                }
                loadType.setName(reqLoadType.getName());
                loadTypeRepository.save(loadType);

            }

        } catch (Exception e) {
            response.setMessage("Error");
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
        }
        return response;
    }

    public ApiResponse removeLoadType(Long id) {
        try {
            loadTypeRepository.deleteById(id);
            return new ApiResponse("Deleted", true, TrayIcon.MessageType.INFO);
        } catch (Exception e) {
            return new ApiResponse("Error", false, TrayIcon.MessageType.ERROR);
        }
    }


    public ApiResponseModel getAllLoadType() {
        return new ApiResponseModel(true, "Ok", loadTypeRepository.findAll());
    }


}
