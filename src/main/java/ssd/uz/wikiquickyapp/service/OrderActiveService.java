package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.entity.enums.WhoPay;
import ssd.uz.wikiquickyapp.map.GoogleMap;
import ssd.uz.wikiquickyapp.map.websocket.NotificationService;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.*;

import java.io.IOException;

@Service
public class OrderActiveService {

    @Autowired
    AddressService addressService;
    @Autowired
    OrderActiveRepository orderActiveRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    WorkerActiveInRadiusService workerActiveInRadiusService;
    @Autowired
    WorkerActiveInRadiusRepository wairr;
    @Autowired
    OrderActiveTimeRepository orderActiveTimeRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    SuperAdminValuesRepository savr;
    @Autowired
    WorkerActiveRepository workerActiveRepository;
    @Autowired
    SuperAdminValueService savs;
    @Autowired
    NotificationService notificationService;
    @Autowired
    LoadTypeRepository loadTypeRepository;
    @Autowired
    WorkerService workerService;
    @Autowired
    CompanyRepository companyRepository;


    public ApiResponse addOrderActive(ReqOrderActive reqOrderActive, Users users) throws IOException {

        OrderActive orderActive = new OrderActive();
        orderActive.setAddress(addressService.addAddress(reqOrderActive.getReqAddress()));
        orderActive.setDescription(reqOrderActive.getDescription());
        orderActive.setReceiverNumber(reqOrderActive.getReceiverNumber());

        if (reqOrderActive.getWhoPay().equals(WhoPay.A_CLIENT.toString())){
            orderActive.setWhoPay(WhoPay.A_CLIENT);
        }else {
            orderActive.setWhoPay(WhoPay.B_CLIENT);
        }

        double orderCostFromReq = GoogleMap.getDistance(reqOrderActive.getReqAddress());
        orderActive.setCost(orderCostFromReq);

            try {
                VehicleType vehicleType = vehicleTypeRepository.findVehicleType(reqOrderActive.getVehicleType());
                orderActive.setVehicleType(vehicleType);
                orderActive.setClientId(users);
                orderActive.setSenderNumber(reqOrderActive.getSenderNumber());
                orderActive.setLoadType(loadTypeRepository.findById(reqOrderActive.getLoadType()).get());
                if (reqOrderActive.getDoorToDoor()!=null){
                    orderActive.setDoorToDoor(reqOrderActive.getDoorToDoor());
                }else {
                    return new ApiResponse("doorToDoor did not entered",false);
                }

                Double lan = reqOrderActive.getReqAddress().getReqLocations().get(0).getLan();
                Double lat = reqOrderActive.getReqAddress().getReqLocations().get(0).getLat();

                OrderActive orderActive1 = orderActiveRepository.save(orderActive);

                Company company = null;
                if (reqOrderActive.getCompanyId()!=null){
                    company = companyRepository.findById(reqOrderActive.getCompanyId()).get();
                }else {
                    company = companyRepository.findById(1L).get();
                }

                ApiResponseModel apiResponseModel = workerActiveInRadiusService.filterWorkerActive(vehicleType, orderActive, new Location(lan, lat),company);

                if (apiResponseModel.getData() == null) {
                        orderActiveRepository.deleteById(orderActive1.getId());
                        return new ApiResponse("10 km radius da birorta ham ishchi chiqmadi , va order active uchirildi" + apiResponseModel.getMessage(), false);
                } else {
                    return new ApiResponse("order active is saved successfully !", true);
                }
            } catch (Exception e) {
                return new ApiResponse("failed on finding vehicle type ", false);
            }
    }

}

