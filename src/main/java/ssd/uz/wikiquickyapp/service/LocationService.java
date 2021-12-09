package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.SuperAdminValue;
import ssd.uz.wikiquickyapp.map.GoogleMap;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqAddress;
import ssd.uz.wikiquickyapp.payload.ResTulov;
import ssd.uz.wikiquickyapp.repository.LocationRepository;
import ssd.uz.wikiquickyapp.repository.SuperAdminValuesRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    SuperAdminValuesRepository savr;
    @Autowired
    SuperAdminValueService savs;


    public ApiResponseModel showPrice(ReqAddress reqAddress) {

        int morning1 = (int) savs.getMorning1().getData();
        int morning2 = (int) savs.getMorning2().getData();
        int night1 = (int) savs.getNight1().getData();
        int night2 = (int) savs.getNight2().getData();
        int trafficTime = (int) savs.getTrafficTime().getData();
        int costPerKmForFoot = (int) savs.getCostPerKmForFoot().getData();
        int costPerKmForBike = (int) savs.getCostPerKmForBike().getData();
        int costPerKmForScooter = (int) savs.getCostPerKmForScooter().getData();
        int costPerKmForCar = (int) savs.getCostPerKmForCar().getData();
        int costPerKmForLabo = (int) savs.getCostPerKmForLabo().getData();
        int time_server = (int) savs.getTimeServer().getData();
        int radius = (int) savs.getMaxRadius().getData();

//         distance metrda keladi api dan
//        try {

        double distance = 0;
        try {
            distance = GoogleMap.getDistance(reqAddress) / 1000;
        } catch (IOException e) {
            return new ApiResponseModel(false, "Google map API did not work", null);
        }
        List<ResTulov> tulovs = new ArrayList<>();
        if (distance >= radius) {
            if (time_server > morning1 && time_server < morning2 || time_server > night1 && time_server < night2) {

                tulovs.add(new ResTulov("onFoot",((int) distance-radius) * costPerKmForFoot + trafficTime + costPerKmForFoot));
                tulovs.add(new ResTulov("bike", ((int) distance-radius) *  costPerKmForBike +  trafficTime + costPerKmForBike));
                tulovs.add(new ResTulov("scooter",((int) distance-radius) * costPerKmForScooter + trafficTime + costPerKmForScooter));
                tulovs.add(new ResTulov("car",((int) distance-radius) * costPerKmForCar + trafficTime + costPerKmForCar));
                tulovs.add(new ResTulov("labo",((int) distance-radius) * costPerKmForLabo + trafficTime + costPerKmForLabo));
                return new ApiResponseModel(true, "distance 2 katta holatda narx chiqarildi", tulovs);
            }
            tulovs.add(new ResTulov("onFoot", ((int) distance-radius) * costPerKmForFoot + costPerKmForFoot));
            tulovs.add(new ResTulov("bike", ((int) distance-radius) *  costPerKmForBike  + costPerKmForBike));
            tulovs.add(new ResTulov("scooter", ((int) distance-radius) * costPerKmForScooter + costPerKmForScooter));
            tulovs.add(new ResTulov("car", ((int) distance-radius) * costPerKmForCar + costPerKmForCar));
            tulovs.add(new ResTulov("labo", ((int) distance-radius) * costPerKmForLabo + costPerKmForLabo));
            return new ApiResponseModel(true, "distance 2 km dan katta holatda narx chiqarildi", tulovs);
        } else {
            tulovs.add(new ResTulov("onFoot", costPerKmForFoot));
            tulovs.add(new ResTulov("bike", costPerKmForBike));
            tulovs.add(new ResTulov("scooter", costPerKmForScooter));
            tulovs.add(new ResTulov("car", costPerKmForCar));
            tulovs.add(new ResTulov("labo", costPerKmForLabo));
            return new ApiResponseModel(true, "distance 2 km dan kichik holatda narx chiqarildi", tulovs);
        }
//        }catch (Exception e){
//            return new ApiResponseModel(false,"failed on getting distance from Google API",null);
//        }
    }
}
