package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.StoredOrder;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqStoredOrder;
import ssd.uz.wikiquickyapp.repository.LoadTypeRepository;
import ssd.uz.wikiquickyapp.repository.StoredOrderRepository;
import ssd.uz.wikiquickyapp.repository.UserRepository;
import ssd.uz.wikiquickyapp.repository.VehicleTypeRepository;

@Service
public class StoredOrderService {
    @Autowired
    AddressService addressService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    StoredOrderRepository storedOrderRepository;

    @Autowired
    LoadTypeRepository loadTypeRepository;

    @Autowired
    OrderActiveService orderActiveService;

    public ApiResponse deleteStoredOrder(Long id) {
        try {
            if (storedOrderRepository.existsById(id)) {
                storedOrderRepository.deleteById(id);
                return new ApiResponse("storedOrder deleted", true);
            } else {
                return new ApiResponse("bunaqa stroredOrder yuq", false);
            }
        } catch (Exception e) {
            return new ApiResponse("saqlashda hatolik", false);
        }
    }

//    public ApiResponseModel getAllStoredOrder(Users users){
//        ApiResponseModel apiResponseModel = new ApiResponseModel();
//        try {
//            List<SoC> storedOrderList = storedOrderRepository.findAllByClientAndDeleted(users,false);
//            if (storedOrderList!=null){
//                apiResponseModel.setSuccess(true);
//                apiResponseModel.setMessage("stored orders was sent !");
//                apiResponseModel.setData(storedOrderList);
//            }else {
//                apiResponseModel.setSuccess(false);
//                apiResponseModel.setMessage("bu userda bitta ham stored order yuq !");
//                apiResponseModel.setData(0);
//            }
//        }catch (Exception e){
//            apiResponseModel.setSuccess(false);
//            apiResponseModel.setMessage("stored orders wasn't sent !");
//            apiResponseModel.setData(null);
//        }
//        return apiResponseModel;
//    }

    public ApiResponseModel saveStoredOrder(Users users,ReqStoredOrder reqStoredOrder){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            StoredOrder storedOrder = new StoredOrder();
            storedOrder.setLoadType(loadTypeRepository.findById(reqStoredOrder.getLoadType()).get());
//            Users usersByPhoneNumber = userRepository.findUsersByPhoneNumber(users.getPhoneNumber());
            storedOrder.setClient(users);
            storedOrder.setSenderNumber(reqStoredOrder.getSenderNumber());
            storedOrder.setReceiverNumber(reqStoredOrder.getReceiverNumber());
            storedOrder.setDoorToDoor(reqStoredOrder.getDoorToDoor());
            storedOrder.setAddress(addressService.addAddress(reqStoredOrder.getReqAddress()));
            storedOrder.setCost(reqStoredOrder.getOrderCost());
            storedOrder.setVehicleType(vehicleTypeRepository.findById(reqStoredOrder.getVehicleType()).get());
            storedOrderRepository.save(storedOrder);
            apiResponseModel.setMessage("success");
            apiResponseModel.setSuccess(true);
        }catch (Exception e){
            apiResponseModel.setMessage("error");
            apiResponseModel.setSuccess(false);
        }
        return apiResponseModel;
    }

}
