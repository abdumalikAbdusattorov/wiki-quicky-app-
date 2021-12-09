package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.map.GoogleMap;
import ssd.uz.wikiquickyapp.map.MapObject;
import ssd.uz.wikiquickyapp.map.fcm.FcmService;
import ssd.uz.wikiquickyapp.map.websocket.NotificationService;

import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ResOrder;
import ssd.uz.wikiquickyapp.repository.*;

import java.io.IOException;
import java.util.List;

@Service
public class WorkerService {

    @Autowired
    WorkerActiveInRadiusRepository workerActiveInRadiusRepository;
    @Autowired
    WorkerActiveRepository workerActiveRepository;
    @Autowired
    OrderActiveRepository orderActiveRepository;
    @Autowired
    WorkerActiveInRadiusService workerActiveInRadiusService;
    @Autowired
    OrderService orderService;
    @Autowired
    WorkerActiveService workerActiveService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    ClientWorkerOrderRepository clientWorkerOrderRepository;
    @Autowired
    FcmService fcmService;

    public ApiResponseModel getOrder(Users users, Long orderId) throws IOException {
        Long id = users.getId();
        WorkerActive workerActive = workerActiveRepository.selectWorkerActiev(id);
        WorkerActiveInRadius workerActiveInRadius = workerActiveInRadiusRepository.search2(workerActive.getId(), orderId);
        OrderActive orderActive = workerActiveInRadius.getOrderActive();
        Order order = (Order) orderService.addOrder(workerActive, orderActive).getData();
        workerActive.setOrderId(order);
        workerActive.setBusy(true);
        workerActiveRepository.save(workerActive);
        Location location = new Location(workerActiveInRadius.getWorkerActiveId().getLan(),workerActiveInRadius.getWorkerActiveId().getLat());
        MapObject mapObject = GoogleMap.getDistanceDouble(location, orderActive.getAddress().getLocations().get(0));

        notificationService.sendToClientForTime(
                "" + mapObject.getArrivalTime() / 60000 + "",
                workerActiveInRadius.getOrderActive().getClientId().getId().toString());
        fcmService.sendMessage(
                orderActive.getClientId().getDeviceToken(),
                ""+mapObject.getArrivalTime() / 60000+" - min",
                "yetkazib beruvchi " + mapObject.getArrivalTime() / 60000 + " daqiqada yetib keladi",
                orderActive.getClientId().getRoles().get(0).getName().name()
                );
        List<WorkerActiveInRadius> userIds = workerActiveInRadiusRepository.findWAIRByIsUsedAndOrderActive(orderActive.getId());
        for (int i = 0; i < userIds.size(); i++) {
            notificationService.deleteNotification("false",userIds.get(i).getWorkerActiveId().getWorkerId().getId().toString());
        }
        clientWorkerOrderRepository.save(new ClientWorkerOrder(workerActive, order.getClient(), order));
        workerActiveInRadiusService.delete(workerActiveInRadius.getOrderActive().getId());
        orderActiveRepository.deleteById(workerActiveInRadius.getOrderActive().getId());
        ResOrder resOrder = new ResOrder();
        resOrder.setAddress(order.getAddressId());
        resOrder.setDoorToDoor(order.getDoorToDoor());
        resOrder.setReceiverNumber(order.getReceiverNumber());
        resOrder.setSenderNumber(order.getSenderNumber());
        resOrder.setOrderCost(order.getOrderCost());
        resOrder.setWhoPay(order.getWhoPay());
        return new ApiResponseModel(true, "order is given to worker", resOrder);
    }

    public ApiResponseModel getWorkerId(Long workerId){
        Long id = workerActiveRepository.selectWorkerActiev(workerId).getId();
        return new ApiResponseModel(true,"worker topildi !",id);
    }

    public ResOrder getOrder(Order order){
        return new ResOrder(
                order.getAddressId(),
                order.getReceiverNumber(),
                order.getSenderNumber(),
                order.getDoorToDoor(),
                order.getOrderCost()
        );
    }

}
