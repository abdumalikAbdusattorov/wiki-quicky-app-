package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.Order;
import ssd.uz.wikiquickyapp.payload.ResOrder;
import ssd.uz.wikiquickyapp.repository.OrderRepository;
import ssd.uz.wikiquickyapp.service.WorkerService;

@RestController
@RequestMapping("/aa")
public class Aaa {
    @Autowired
    WorkerService workerService;
    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/{id}")
    public ResOrder getOrder(@PathVariable Long id){
        Order order = orderRepository.findById(id).get();
        ResOrder resOrder = new ResOrder();
        resOrder.setAddress(order.getAddressId());
        resOrder.setOrderCost(order.getOrderCost());
        resOrder.setOrderStatusEnum(order.getOrderStatusEnum());
        ResOrder resOrder1 = workerService.getOrder(order);
        return resOrder1;
    }

}
