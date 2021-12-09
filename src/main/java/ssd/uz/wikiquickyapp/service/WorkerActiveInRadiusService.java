package ssd.uz.wikiquickyapp.service;

import org.glassfish.grizzly.http.util.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.map.CalculateDistance;
import ssd.uz.wikiquickyapp.map.websocket.NotificationService;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerActiveInRadiusService {

    @Autowired
    WorkerActiveInRadiusRepository w;
    @Autowired
    WorkerActiveRepository workerActiveRepository;
    @Autowired
    VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    WorkerActiveInRadiusRepository wair;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SuperAdminValuesRepository superAdminValuesRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    ClientWorkerOrderRepository clientWorkerOrderRepository;
    @Autowired
    OrderActiveRepository orderActiveRepository;

    @Transactional
    public ApiResponse delete(Long id) {
        w.deleteWorkerActiveInRadiusByOrderActive_Id(id);
        return new ApiResponse("workers in radius deleted", true);
    }

    @Transactional
    public ApiResponse deleteOrderActive(Long id) {
        orderActiveRepository.deleteById(id);
        return new ApiResponse("workers in radius deleted", true);
    }

    @Transactional
    public ApiResponse deleteCOW(Long orderId) {
        clientWorkerOrderRepository.deleteClientWorkerOrderByOrderId(orderId);
        return new ApiResponse("uchirildi", true);
    }

    @Transactional
    public ApiResponse deleteWorkerActive(Users workerActive) {
        workerActiveRepository.deleteWorkerActivesByWorkerId(workerActive);
        return new ApiResponse("uchirildi", true);
    }


    public ApiResponseModel filterWorkerActive(VehicleType vehicleType, OrderActive orderActive, Location location,Company company) {
        try {
            List<WorkerActive> workerActives2;
            Double radius = 1.0;
            int maxRadius = superAdminValuesRepository.findSuperAdminValueByName("maxRadius").get().getValue();
            double mradius = (double) maxRadius;
            try {
                List<WorkerActive> workerActiveList = workerActiveRepository.selectWorkerActive(vehicleType.getId(),company.getId());
                do {
                    workerActives2 = CalculateDistance.searchInRadius(workerActiveList, location, radius);
                    radius++;
                } while (workerActives2.size() == 0 && radius <= mradius);
                if (workerActives2.size()==0){
                    return new ApiResponseModel(false,"bittaham workwer active yuq ekan",null);
                }
                for (int i = 0; i < workerActives2.size(); i++) {
                    w.save(new WorkerActiveInRadius(workerActives2.get(i), orderActive, null, false));
                }
                return new ApiResponseModel(true, "workerlar radius buyicha saralandi", workerActives2.size());
            } catch (Exception e) {
                return new ApiResponseModel(false, "vehicle type buyicha workerlar listini olib kelishda hatolik ❌❌🚫 ", null);
            }
        } catch (Exception e) {
            return new ApiResponseModel(false, "workerlar radius buyicha saralashda xalolik ❌❌🚫 ", null);
        }
    }

    @Scheduled(fixedRateString = "2000")
    public void checkInRadius() {
        if (w.count() > 0) {
            List<WorkerActiveInRadius> www = w.findAll();
            int first_order_number = 0;
            ArrayList<List<WorkerActiveInRadius>> lists = new ArrayList<>();
            for (int i = 0; i < www.size(); i++) {
                if (www.get(first_order_number).isUsed() == false) {
                    Long order_active_id = www.get(first_order_number).getOrderActive().getId();
                    List<WorkerActiveInRadius> workerActiveInRadiusList = w.findWAIRByIsUsedAndOrderActiveId(order_active_id);
                    for (int j = 0; j < workerActiveInRadiusList.size(); j++) {
                        jdbcTemplate.update("update worker_active_in_radius set is_used=true where order_active_id = ?", order_active_id);
                    }
                    lists.add(workerActiveInRadiusList);
                    www = w.findAll();
                } else {
                    first_order_number++;
                }
            }
            System.out.println("list list size : ____"+lists.size());
            sendMessageToUsers(lists);
        }
    }

    private ApiResponse sendMessageToUsers(ArrayList<List<WorkerActiveInRadius>> workerActiveList) {
        for (int i = 0; i < workerActiveList.size(); i++) {
            List<WorkerActiveInRadius> workerActiveInRadiusList = workerActiveList.get(i);
            for (int j = 0; j < workerActiveInRadiusList.size(); j++) {
                WorkerActiveInRadius wair = workerActiveInRadiusList.get(j);
                OrderActive orderActive = wair.getOrderActive();
                ResOrderActive res = new ResOrderActive();
                ResAddress resAddress = new ResAddress();
                resAddress.setLocations(orderActive.getAddress().getLocations());
                res.setAddress(resAddress);
                res.setId(orderActive.getId());
                res.setReceiverNumber(orderActive.getReceiverNumber());
                res.setReceiverNumber(orderActive.getReceiverNumber());
                res.setWhoPay(orderActive.getWhoPay().name());
                res.setOrderCost(orderActive.getCost());
                res.setDoorToDoor(orderActive.getDoorToDoor());
                System.out.println("user id = "+wair.getWorkerActiveId().getWorkerId().getId().toString());
                notificationService.sendToUser(res, wair.getWorkerActiveId().getWorkerId().getId().toString());
            }
        }
        return new ApiResponse("orderActive send to user successfully", true);
    }



}
