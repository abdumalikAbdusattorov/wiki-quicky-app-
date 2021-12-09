package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.SuperAdminValue;
import ssd.uz.wikiquickyapp.map.TimeApi;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.repository.SuperAdminValuesRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SuperAdminValueService {
    @Autowired
    SuperAdminValuesRepository savr;

    public ApiResponseModel getTimeServer() {
        try {
            TimeApi timeApi = new TimeApi();
            Timestamp timestamp = new Timestamp(timeApi.getTime());
            int timeServer = Integer.parseInt(timestamp.toString().substring(11, 13));
            return new ApiResponseModel(true, "success", timeServer);
        } catch (Exception e) {
            return new ApiResponseModel(false, "error in geting time of the server", null);
        }
    }

    public ApiResponseModel getMorning1() {
        try {
            int morning1 = savr.findSuperAdminValueByName("morning1").get().getValue();
            return new ApiResponseModel(true, "success", morning1);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getMorning2() {
        try {
            int morning2 = savr.findSuperAdminValueByName("morning2").get().getValue();
            return new ApiResponseModel(true, "success", morning2);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getNight1() {
        try {
            int night1 = savr.findSuperAdminValueByName("night1").get().getValue();
            return new ApiResponseModel(true, "success", night1);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getNight2() {
        try {
            int night2 = savr.findSuperAdminValueByName("night2").get().getValue();
            return new ApiResponseModel(true, "success", night2);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getDoorToDoor() {
        try {
            int doorToDoor = savr.findSuperAdminValueByName("doorToDoor").get().getValue();
            return new ApiResponseModel(true, "success", doorToDoor);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getTrafficTime() {
        try {
            int trafficTime = savr.findSuperAdminValueByName("trafficTime").get().getValue();
            return new ApiResponseModel(true, "success", trafficTime);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getBike() {
        try {
            int bike = savr.findSuperAdminValueByName("costForMinDistanceBike").get().getValue();
            return new ApiResponseModel(true, "success", bike);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getScooter() {
        try {
            int scooter = savr.findSuperAdminValueByName("costForMinDistanceScooter").get().getValue();
            return new ApiResponseModel(true, "success", scooter);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getCar() {
        try {
            int car = savr.findSuperAdminValueByName("costForMinDistanceCar").get().getValue();
            return new ApiResponseModel(true, "success", car);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getOnFoot() {
        try {
            int onFoot = savr.findSuperAdminValueByName("costForMinDistanceFoot").get().getValue();
            return new ApiResponseModel(true, "success", onFoot);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getLabo() {
        try {
            int labo = savr.findSuperAdminValueByName("costForMinDistanceLabo").get().getValue();
            return new ApiResponseModel(true, "success", labo);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public ApiResponseModel getMaxRadius() {
        try {
            int maxRadius = savr.findSuperAdminValueByName("maxRadius").get().getValue();
            return new ApiResponseModel(true, "success", maxRadius);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }
//    **************----------*************--------------***********---------//////////----------

    public ApiResponseModel getCostPerKmForFoot() {
        try {
            int maxRadius = savr.findSuperAdminValueByName("costForPerKMFoot").get().getValue();
            return new ApiResponseModel(true, "success", maxRadius);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }
    public ApiResponseModel getCostPerKmForBike() {
        try {
            int maxRadius = savr.findSuperAdminValueByName("costForPerKMBike").get().getValue();
            return new ApiResponseModel(true, "success", maxRadius);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }
    public ApiResponseModel getCostPerKmForScooter() {
        try {
            int maxRadius = savr.findSuperAdminValueByName("costForPerKMScooter").get().getValue();
            return new ApiResponseModel(true, "success", maxRadius);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }
    public ApiResponseModel getCostPerKmForCar() {
        try {
            int maxRadius = savr.findSuperAdminValueByName("costForPerKMCar").get().getValue();
            return new ApiResponseModel(true, "success", maxRadius);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }
    public ApiResponseModel getCostPerKmForLabo() {
        try {
            int maxRadius = savr.findSuperAdminValueByName("costForPerKMLabo").get().getValue();
            return new ApiResponseModel(true, "success", maxRadius);
        } catch (Exception e) {
            return new ApiResponseModel(false, "exception on getting morning1 value", null);
        }
    }

    public List<SuperAdminValue> getAllSuperAdminValue(){
        return savr.findAll();
    }

    public Integer getByName(List<SuperAdminValue> values,String name){
        Integer value = null;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getName().equals(name)){
                value = values.get(i).getValue();
            }
        }
        return value;
    }


}
