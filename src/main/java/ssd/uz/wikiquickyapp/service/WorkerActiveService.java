package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.map.CalculateDistance;
import ssd.uz.wikiquickyapp.map.websocket.NotificationService;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.*;
import ssd.uz.wikiquickyapp.service.test.WorkerTest;
import ssd.uz.wikiquickyapp.service.test.WorkerTestRepository;
import ssd.uz.wikiquickyapp.utils.CommonUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerActiveService {

    @Autowired
    WorkerActiveRepository workerActiveRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ClientWorkerOrderRepository clientWorkerOrderRepository;

    @Autowired
    WorkerActiveInRadiusService workerActiveInRadiusService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    WorkerTestRepository workerTestRepository;

    public ApiResponseModel saveOrEditWorkerActive(Users users, ReqWorkerActive reqWorkerActive) {
        ApiResponseModel apiResponse = new ApiResponseModel();
        try {
            if (!workerActiveRepository.existsByWorkerId(users)){
                apiResponse.setSuccess(true);
                apiResponse.setMessage("WorkerActive saved");
                WorkerActive workerActive = new WorkerActive();
                workerActive.setId(users.getId());
                workerActive.setBusy(false);//false bosa bandmas, zakaz ololidi!
                workerActive.setWorkerId(users);
                workerActive.setLan(reqWorkerActive.getLan());
                workerActive.setLat(reqWorkerActive.getLat());
                workerActive.setCompany(users.getCompany());
                workerActive.setVehicleType(users.getVehicle().getVehicleType());
                if (workerActive.getOrderId()!=null){
                    Order realTimeOrder = workerActive.getOrderId();
                    ResOrder resOrder = new ResOrder();
                    resOrder.setOrderCost(realTimeOrder.getOrderCost());
                    resOrder.setWhoPay(realTimeOrder.getWhoPay());
                    resOrder.setWorkerLocation(realTimeOrder.getWorkerLocation());
                    resOrder.setOrderStatusEnum(realTimeOrder.getOrderStatusEnum());
                    resOrder.setReceiverNumber(realTimeOrder.getReceiverNumber());
                    resOrder.setAddress(realTimeOrder.getAddressId());
                    resOrder.setSenderNumber(realTimeOrder.getSenderNumber());
                    resOrder.setDoorToDoor(realTimeOrder.getDoorToDoor());
                    apiResponse.setData(realTimeOrder);
                }
                workerActiveRepository.save(workerActive);
            }else {
                WorkerActive workerActive = workerActiveRepository.findWorkerActive(users.getId());
                workerActive.setLat(reqWorkerActive.getLat());
                workerActive.setLan(reqWorkerActive.getLan());
                if (workerActive.getOrderId()!=null){
                    Order realTimeOrder = workerActive.getOrderId();
                    ResOrder resOrder = new ResOrder();
                    resOrder.setOrderCost(realTimeOrder.getOrderCost());
//                    resOrder.setId(realTimeOrder.getId());
                    resOrder.setWhoPay(realTimeOrder.getWhoPay());
                    resOrder.setReceiverNumber(realTimeOrder.getReceiverNumber());
                    resOrder.setOrderStatusEnum(realTimeOrder.getOrderStatusEnum());
                    resOrder.setWorkerLocation(realTimeOrder.getWorkerLocation());
//                    ResAddress resAddress = new ResAddress();
//                    List<Location> locations = new ArrayList<>();
//                    locations.add(realTimeOrder.getAddressId().getLocations().get(0));
//                    locations.add(realTimeOrder.getAddressId().getLocations().get(1));
//                    resAddress.setLocations(locations);
                    resOrder.setAddress(realTimeOrder.getAddressId());
                    resOrder.setSenderNumber(realTimeOrder.getSenderNumber());
                    resOrder.setDoorToDoor(realTimeOrder.getDoorToDoor());
                    apiResponse.setData(resOrder);
                }
                workerActiveRepository.save(workerActive);
                apiResponse.setSuccess(true);
                apiResponse.setMessage("WorkerActive saved");
            }
        } catch (Exception e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("WorkerActive not saved");
        }
        return apiResponse;
    }

    @Scheduled(fixedRateString = "10000")
    public void sendLocationToWorker() {
        List<ClientWorkerOrder> clientWorkerOrders = clientWorkerOrderRepository.findAll();
        for (int i = 0; i < clientWorkerOrders.size(); i++) {
            ClientWorkerOrder clientWorkerOrder = clientWorkerOrders.get(i);
            String lan = clientWorkerOrder.getWorker().getLan().toString();
            String lat = clientWorkerOrder.getWorker().getLat().toString();
            notificationService.sendToClient("/lan:" + lan + "/lat:" + lat + "/", clientWorkerOrder.getClient().getId().toString());
        }
    }

    public ResPageable pageable(Integer page, Integer size) {
        Page<WorkerActive> cashPage = workerActiveRepository.findAll(CommonUtils.getPageableById(page, size));
        return new ResPageable(cashPage.getContent(), cashPage.getTotalElements(), page);
    }

    public ApiResponse removeWorkerActive(Users users) {
        try {
            workerActiveInRadiusService.deleteWorkerActive(users);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    public ApiResponseModel getWorkerAtive(Users users,Double lan,Double lat){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
//            Location location = new Location(lan,lat);
//            List<ResWorkerActive> workerActiveList = CalculateDistance.searchInRadius(workerActiveRepository.findAll(),location, 10d)
//                    .stream().map(this::getWorkerActive).collect(Collectors.toList());#
            List<WorkerTest> workerActiveList = workerTestRepository.findAll();
            apiResponseModel.setMessage("success");
            apiResponseModel.setSuccess(true);
            apiResponseModel.setData(workerActiveList);
        }catch (Exception e){
            apiResponseModel.setMessage("error");
            apiResponseModel.setSuccess(false);
        }
        return apiResponseModel;
    }

    public ResWorkerActive getWorkerActive(WorkerActive workerActive){
        return new ResWorkerActive(
                workerActive.getId(),
                workerActive.getBusy(),
                workerActive.getLan(),
                workerActive.getLat(),
                null,
                workerActive.getVehicleType(),
                workerActive.getCompany().getName()
        );
    }


}
