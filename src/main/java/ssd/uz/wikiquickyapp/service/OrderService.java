package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.entity.enums.OrderStatus;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.entity.enums.WhoPay;
import ssd.uz.wikiquickyapp.entity.enums.WorkerLocation;
import ssd.uz.wikiquickyapp.map.fcm.FcmService;
import ssd.uz.wikiquickyapp.map.websocket.NotificationService;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.*;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderActiveRepository orderActiveRepository;
    @Autowired
    WorkerActiveRepository workerActiveRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderActiveService orderActiveService;
    @Autowired
    WorkerActiveInRadiusService workerActiveInRadiusService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    LoadTypeRepository loadTypeRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    ClientWorkerOrderRepository clientWorkerOrderRepository;
    @Autowired
    FcmService fcmService;



    public ApiResponseModel addOrder(WorkerActive workerActive, OrderActive orderActive) {
        try {
            Order order = new Order();
            order.setAddressId(orderActive.getAddress());
            order.setDescription(orderActive.getDescription());
            order.setLoadType(orderActive.getLoadType());
            order.setStartTime(new Timestamp(System.currentTimeMillis()));
            order.setClient(orderActive.getClientId());
            order.setDeleted(false);
            order.setVehicleType(order.getVehicleType());
            order.setWorker(workerActive.getWorkerId());
            order.setDoorToDoor(orderActive.getDoorToDoor());
            order.setWorkerLocation(WorkerLocation.WORKER_GOING_TO_A);
            workerActive.setBusy(true);
            if (orderActive.getWhoPay().equals(WhoPay.A_CLIENT)){
                order.setWhoPay(WhoPay.A_CLIENT);
            }else {
                order.setWhoPay(WhoPay.B_CLIENT);
            }
            order.setReceiverNumber(orderActive.getReceiverNumber());
            order.setSenderNumber(orderActive.getSenderNumber());
            order.setOrderCost(orderActive.getCost());
            Order order1 = orderRepository.save(order);

            return new ApiResponseModel(true, "order saqlandi !", order1);
            } catch (Exception e) {
            return new ApiResponseModel(false, "orderni saqlashda xatolik", null);
        }
    }

    public ApiResponse cancel(Users users,String cause) {
        if (RoleName.ROLE_WORKER.name() ==users.getRoles().get(0).getName().name()) {
            WorkerActive workerActive = workerActiveRepository.findWorkerActive(users.getId());
            if (workerActive.getOrderId()!=null) {
                Order order = workerActive.getOrderId();
                if (order.getPaid() != null || order.getOrderStatusEnum() != OrderStatus.BUYRUTMA_OLINDI || order.getOrderStatusEnum() != OrderStatus.BUYRUTMA_BERILDI ||
                        order.getOrderStatusEnum() != OrderStatus.BUYRUTMA_YETKAZILDI
                ) {
                    order.setRejectFrom("worker");
                    order.setOrderStatusEnum(OrderStatus.BUYURTMA_BEKORQILINDI);
                    workerActive.setBusy(false);
                    workerActive.setOrderId(null);
                    workerActive.setOrderId(null);
                    workerActiveRepository.save(workerActive);
                    workerActiveInRadiusService.deleteCOW(order.getId());
                    notificationService.cencelOrder("haydovchi buyurtmani bekor qildi , iltimos qaytadan buyurtma bering !", order.getClient().getId().toString());
                    fcmService.sendMessage(
                            order.getClient().getDeviceToken(),
                            "Bekor bo'lish !",
                            "Client buyurtmani bekor qildi !",
                            order.getClient().getRoles().get(0).getName().name()
                    );
                    order.setCancelCause(cause);
                    orderRepository.save(order);
                    return new ApiResponse("zakazni bekor qildingiz", true);
                }
            }else {
                return new ApiResponse("workerni ayni paytda orderi yoq !", false);
            }
        } else {
            Order order = orderRepository.selectClient(users.getId());
            Long workerId = order.getWorker().getId();
            WorkerActive workerActive = workerActiveRepository.findWorkerActive(workerId);
            if (workerActive!=null){
                if (workerActive.getOrderId().getClient().getId()==users.getId()){
                    workerActive.setBusy(false);
                    workerActive.setOrderId(null);
                    workerActive.setOrderId(null);
                    workerActiveRepository.save(workerActive);
                    order.setRejectFrom("client");
                    workerActiveInRadiusService.deleteCOW(order.getId());
                    notificationService.cencelOrder("mijoz zakazni bekor qildi",order.getWorker().getId().toString());
                    fcmService.sendMessage(
                            order.getWorker().getDeviceToken(),
                            "Bekor bo'lish !",
                            "Haydovchi buyurtmani bekor qildi !",
                            order.getWorker().getRoles().get(0).getName().name()
                    );
                    order.setOrderStatusEnum(OrderStatus.BUYURTMA_BEKORQILINDI);
                    order.setCancelCause(cause);
                    orderRepository.save(order);
                }else {
                    return new ApiResponse("siz hali zakaz bermagansiz ! ", false);
                }
            }else {
                return new ApiResponse("siz hali zakaz bermagansiz ! ", false);
            }
        }
        return new ApiResponse("order is canceled ", true);
    }

    public ApiResponse editOrder(Users users, ReqComplateOrder reqComplateOrder) {
        try {
            Order order = orderRepository.selectWorkerActive(users.getId());

            if (reqComplateOrder.getWaitingTime() != null && reqComplateOrder.getWaitingTime() > 180) { // agar dastavchik 3 min dan kam vaqt kutgan bulsa waiting taim save bulmaydi
                double time = reqComplateOrder.getWaitingTime() / 60 - 3;
                Double cost = order.getOrderCost();
                order.setOrderCost(cost + time * 800);

                if (order.getWaitingTime() != null && reqComplateOrder.getWaitingTime() != null) {
                    order.setWaitingTime(reqComplateOrder.getWaitingTime() + order.getWaitingTime());
                } else {
                    order.setWaitingTime(reqComplateOrder.getWaitingTime());
                }

                if (order.getWorkerLocation() == WorkerLocation.WORKER_ON_A) {
                    order.setWorkerLocation(WorkerLocation.WORKER_GOING_TO_B);
                }
                orderRepository.save(order);
            }

            if (reqComplateOrder.getWorkerLocation() != null) {
                order.setWorkerLocation(reqComplateOrder.getWorkerLocation());
            }

            double costDefault = order.getOrderCost();
            order.setOrderCost(costDefault);
            order.setOrderStatusEnum(reqComplateOrder.getOrderStatus());

            if (order.getPaid() == null && reqComplateOrder.getPaid() != null) {
                order.setPaid(reqComplateOrder.getPaid());
            }

            if (reqComplateOrder.getWorkerLocation() == WorkerLocation.WORKER_ON_A) {// bu narsa notification bulishi kerak !
                notificationService.sendToClientArrivalA("haydovchi A nuqtaga yetib keldi !! ",order.getClient().getId().toString());
                fcmService.sendMessage(
                        order.getClient().getDeviceToken(),
                        "Haydovchi yetib keldi !",
                        "haydovchi manzilga yetib keldi iltimos iltimos buyurtmani unga topshiring !",
                        order.getClient().getRoles().get(0).getName().name()
                );
            }
            if (reqComplateOrder.getWorkerLocation() == WorkerLocation.WORKER_ON_B) {
               notificationService.sendToClientArrivalB("haydovchi B nuqtaga yetib keldi !!",order.getClient().getId().toString());
                fcmService.sendMessage(
                        order.getClient().getDeviceToken(),
                        "Haydovchi yetib keldi !",
                        "haydovchi manzilga yetib keldi iltimos iltimos buyurtmani undan oling !",
                        order.getClient().getRoles().get(0).getName().name()
                );
            }
            if (reqComplateOrder.getOrderStatus() == OrderStatus.BUYRUTMA_YETKAZILDI) {
                notificationService.sendToClientOrderFinished("buyurtma B nuqtaga yetkazildi",order.getClient().getId().toString());
                fcmService.sendMessage(
                        order.getClient().getDeviceToken(),
                        "Haydovchi yetib keldi !",
                        "buyurtma manzilga yetkazildi Xayr salomat buling !",
                        order.getClient().getRoles().get(0).getName().name()
                );
                order.setEndTime(new Timestamp(System.currentTimeMillis()));
                ReqCompanyInterest reqCompanyInterest = new ReqCompanyInterest();
                reqCompanyInterest.setOrderId(order.getId());
                paymentService.getCompanyInterestFromOrder(reqCompanyInterest);
                Long workerActiveId = order.getWorker().getId();
                workerActiveInRadiusService.deleteCOW(order.getId());
                WorkerActive workerActive = workerActiveRepository.selectWorkerActiev(workerActiveId);
                workerActive.setBusy(false);
                workerActive.setOrderId(null);
                workerActiveRepository.save(workerActive);
                try {
                    workerActiveInRadiusService.deleteCOW(order.getId());
                } catch (Exception e) {
                    return new ApiResponse("uxshamadi", false);
                }
            }
            orderRepository.save(order);
            return new ApiResponse("Edited successfully", true);
        } catch (Exception e) {
            return new ApiResponse("saqlashda hatolik", false);
        }
    }

    public ApiResponse deleteOne(Long id){
        try {
            Order order = orderRepository.findById(id).get();
            order.setDeleted(false);
            orderRepository.save(order);
            return new ApiResponse("kdmelkmcf",true);
        }catch (Exception e){
            return new ApiResponse("kdmelkmcf",false);
        }
    }

}
