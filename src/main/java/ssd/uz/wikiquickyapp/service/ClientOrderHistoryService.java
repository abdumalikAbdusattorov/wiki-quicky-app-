package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssd.uz.wikiquickyapp.entity.Order;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.OrderRepository;
import ssd.uz.wikiquickyapp.repository.UserRepository;
import ssd.uz.wikiquickyapp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class ClientOrderHistoryService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    public ApiResponseModel getOrderHistory(Users users, Integer page, Integer size) {
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success");
            ResOrderHistory resOrderHistory = null;
            if (users.getRoles().get(0).getName()== RoleName.ROLE_CLIENT){
                List<Order> allByClientAndDeleted = orderRepository.findAllByClientAndDeleted(users, false, CommonUtils.getPageableById(page, size));
                List<Order> list = orderRepository.findAllByClientAndDeleted(users,false);
                Long allCount = (long) list.size();
                Double sum = calculateSum(list);
                resOrderHistory = GenerationResOrder(allByClientAndDeleted,allCount,page,size,sum);
            }
            else {
                List<Order> allByWorker = orderRepository.findAllByWorker(users, CommonUtils.getPageableById(page, size));
                List<Order> list = orderRepository.findAllByWorkerAndDeleted(users,false);
                Long allCount = (long) list.size();
                Double sum = calculateSum(list);
                resOrderHistory = GenerationResOrder(allByWorker,allCount,page,size,sum);
            }
            apiResponseModel.setData(resOrderHistory);
        }catch (Exception e){
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success");
        }
        return apiResponseModel;
    }

    public ApiResponseModel getOrderHistoryByTime(Users users, Integer page, Integer size , String dateFrom , String dateTo) {
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success");
            ResOrderHistory resOrderHistory = null;
            if (users.getRoles().get(0).getName()== RoleName.ROLE_CLIENT){
                List<Order> orderList = orderRepository.selectOrdersByTimeForClient(users.getId(), dateFrom, dateTo, CommonUtils.getPageableById(page, size));
                List<Order> list = orderRepository.selectOrdersByTimeForClientAll(users.getId(), dateFrom, dateTo);
                Long allCount = (long) list.size();
                Double sum = calculateSum(list);
                resOrderHistory = GenerationResOrder(orderList,allCount,page,size,sum);
            }
            else {
                List<Order> orderList = orderRepository.selectOrdersByTimeForWorker(users.getId(), dateFrom, dateTo, CommonUtils.getPageableById(page, size));
                List<Order> list = orderRepository.selectOrdersByTimeForWorkerAll(users.getId(), dateFrom, dateTo);
                Long allCount = (long) list.size();
                Double sum = calculateSum(list);
                resOrderHistory = GenerationResOrder(orderList,allCount,page,size,sum);
            }
            apiResponseModel.setData(resOrderHistory);
        }catch (Exception e){
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success");
        }
        return apiResponseModel;
    }

    public Double calculateSum(List<Order> list){
        Double sum = 0.0;
        for (int i = 0; i < list.size(); i++) {
            sum+=list.get(i).getOrderCost();
        }
        return sum;
    }

    public ResOrderHistory GenerationResOrder(List<Order> orderList,Long allcount,Integer currentPage,Integer size,Double sum) {
        List<ResOrder> list = new ArrayList<>();
        for (Order order : orderList) {
            list.add(new ResOrder(
                    order.getAddressId(),
                    order.getReceiverNumber(),
                    order.getSenderNumber(),
                    order.getStartTime(),
                    new ResWorker(
                            order.getWorker().getId(),
                            order.getWorker().getFirstName()+" "+order.getWorker().getLastName(),
                            order.getWorker().getVehicle().getColor()+" "+order.getWorker().getVehicle().getName()+" "+order.getWorker().getVehicle().getCarNumber()),
                    order.getClient().getFirstName()+" : "+order.getClient().getPhoneNumber(),
                    order.getReceiverNumber(),
                    order.getOrderCost(),
                    order.getRejectFrom(),

                    order.getOrderStatusEnum(),
                    order.getLoadType(),
                    order.getVehicleType(),
                    order.getWhoPay(),
                    order.getLoadType()+" : "+order.getDescription(),
                    order.getDoorToDoor(),
                    null
                    ));
        }
        return new ResOrderHistory(
                allcount,
                allcount>5?allcount/size:1,
                currentPage,
                sum,
                list
        );

    }

    public ApiResponse deleteOrderHistory(Long id){
        ApiResponse response = new ApiResponse();
        try {
            Order order = orderRepository.findById(id).get();
            order.setDeleted(true);
            orderRepository.save(order);
            response.setMessage("deleted");
            response.setSuccess(true);
        }catch (Exception e){
            response.setMessage("bunaqa id lik order yuq !");
            response.setSuccess(false);
        }
        return response;
    }

}
