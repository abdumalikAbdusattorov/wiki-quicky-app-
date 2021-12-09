package ssd.uz.wikiquickyapp.service;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssd.uz.wikiquickyapp.collection.OrderCol;
import ssd.uz.wikiquickyapp.collection.UsersCol;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.entity.enums.IsWorker;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.entity.enums.UserType;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;
import ssd.uz.wikiquickyapp.map.websocket.NotificationService;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.*;
import ssd.uz.wikiquickyapp.utils.CommonUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientOrderHistoryService clientOrderHistoryService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    WorkerActiveRepository workerActiveRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    WorkerActiveService workerActiveService;

    @Autowired
    CompanyRepository companyRepository;

    public ApiResponseModel addWorkersWithAdmin(SignUpForBussiness signUp,Users users1){
        ApiResponseModel model = new ApiResponseModel();
        try {
            if (!userRepository.existsByPhoneNumber(signUp.getPhoneNumber())){
                Users users = new Users();
                users.setPassword(passwordEncoder.encode(signUp.getPassword()));
                users.setFirstName(signUp.getFirstName());
                users.setLastName(signUp.getLastName());
                users.setRoles(roleRepository.findAllByName(RoleName.ROLE_WORKER));
                users.setPhoneNumber(signUp.getPhoneNumber());
                users.setUserType(UserType.BUSINESS);
                users.setIsVerified(true);
                users.setIsCheckedWorker(true);
                users.setCompany(users1.getCompany());
                userRepository.save(users);
                model.setMessage("saqlandi");
                model.setSuccess(true);
            }else {
                model.setMessage("bunaqa nomerlik user bazada mavjud !");
                model.setSuccess(false);
            }
        }catch (Exception e){
            model.setMessage("error !");
            model.setSuccess(false);
        }
        return model;
    }

    // campaniyaning hamma orderlari
    public ApiResponseModel getAllOrdersByCompany(Users users,Integer page,Integer size){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            List<Order> orderList = orderRepository.findAllOrdersBYCompany(users.getCompany().getId());
            Long maxSize = (long) orderList.size();
            Double sum = clientOrderHistoryService.calculateSum(orderList);
            ResOrderHistory resOrderHistory = clientOrderHistoryService.GenerationResOrder(orderList, maxSize, page, size,sum);
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success !");
            apiResponseModel.setData(resOrderHistory);
        }catch (Exception e){
            apiResponseModel.setSuccess(false);
            apiResponseModel.setMessage("error !");
        }
        return apiResponseModel;
    }

    // statusi buyicha hamma orderlari
    public ApiResponseModel getAllCancelledOrdersByCompany(Users users,Integer page,Integer size,String status){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            List<Order> orderList = orderRepository.findAllCancelledOrdersBYCompany(users.getCompany().getId(),status);
            Long maxSize = (long) orderList.size();
            Double sum = clientOrderHistoryService.calculateSum(orderList);
            ResOrderHistory resOrderHistory = clientOrderHistoryService.GenerationResOrder(orderList, maxSize, page, size,sum);
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success !");
            apiResponseModel.setData(resOrderHistory);
        }catch (Exception e){
            apiResponseModel.setSuccess(false);
            apiResponseModel.setMessage("error !");
        }
        return apiResponseModel;
    }

    // vaqt filter qilib hamma orderlar
    public ApiResponseModel getAllOrdersByTime(Users users,Integer page,Integer size,String startDay,String endDate ){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            List<Order> orderList = orderRepository.findAllOrdersBYTime(users.getCompany().getId(),startDay,endDate);
            Long maxSize = (long) orderList.size();
            Double sum = clientOrderHistoryService.calculateSum(orderList);
            ResOrderHistory resOrderHistory = clientOrderHistoryService.GenerationResOrder(orderList, maxSize, page, size,sum);
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success !");
            apiResponseModel.setData(resOrderHistory);
        }catch (Exception e){
            apiResponseModel.setSuccess(false);
            apiResponseModel.setMessage("error !");
        }
        return apiResponseModel;
    }

    // vaqt-status filter qilib hamma orderlar
    public ApiResponseModel getAllOrdersByTimeandStatus(Users users,Integer page,Integer size,String startDay,String endDate, String status ){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            List<Order> orderList = orderRepository.findAllOrdersBYTimeAndStatus(users.getCompany().getId(),startDay,endDate,status);
            Long maxSize = (long) orderList.size();
            Double sum = clientOrderHistoryService.calculateSum(orderList);
            ResOrderHistory resOrderHistory = clientOrderHistoryService.GenerationResOrder(orderList, maxSize, page, size,sum);
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success !");
            apiResponseModel.setData(resOrderHistory);
        }catch (Exception e){
            apiResponseModel.setSuccess(false);
            apiResponseModel.setMessage("error !");
        }
        return apiResponseModel;
    }

    // campaniyaning hamma userlarini chaqirish
    public ApiResponseModel getAllUsers(Users users){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            List<ResUser> usersList = userRepository.findAllUsersByComapaby(users.getId()).stream().map(this::getUser).collect(Collectors.toList());
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success !");
            apiResponseModel.setData(usersList);
        }catch (Exception e){
            apiResponseModel.setSuccess(false);
            apiResponseModel.setMessage("error !");
        }
        return apiResponseModel;
    }

    // orderlarni userlarga qarab chaqirish
    public ApiResponseModel getOrdersByUsers(Long id,String start , String end,Integer page,Integer size){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            List<Order> orderList = orderRepository.findOrdersByWorker(id,start,end);
            Long maxSize = (long) orderList.size();
            Double sum = clientOrderHistoryService.calculateSum(orderList);
            ResOrderHistory resOrderHistory = clientOrderHistoryService.GenerationResOrder(orderList, maxSize, page, size,sum);
            apiResponseModel.setSuccess(true);
            apiResponseModel.setMessage("success !");
            apiResponseModel.setData(resOrderHistory);
        }catch (Exception e){
            apiResponseModel.setSuccess(false);
            apiResponseModel.setMessage("error !");
        }
        return apiResponseModel;
    }

    // userlarning qachon registerdan utishini bilish
    public ApiResponseModel getAllUserByRegisterTime(Users users,Integer page,Integer size,String startDay,String endDate){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            List<ResUser> resUserList = userRepository.findAllUsersByTime(users.getCompany().getId(),startDay,endDate,size,page*size)
                    .stream().map(this::getUser).collect(Collectors.toList());
            apiResponseModel.setData(resUserList);
            apiResponseModel.setMessage("send!");
            apiResponseModel.setSuccess(true);
        }catch (Exception e){
            apiResponseModel.setMessage("error!");
            apiResponseModel.setSuccess(false);
        }
        return apiResponseModel;
    }

    public ResUser getUser(Users users){
        return new ResUser(
                users.getId(),
                users.getCreatedAt().toString(),
                users.getUpdatedAt().toString(),
                users.getPhoneNumber(),
                users.getFirstName(),
                users.getLastName(),
                users.getClientOrder(),
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(users.getAvatarPhoto().getId().toString()).toUriString(),
                users.getVehicle().getName()+" "+" "+users.getVehicle().getCarNumber()+" "+users.getVehicle().getColor(),
                users.getEmail(),
                users.getGender().name(),
                users.getDateOfBirth()
        );
    }

//    @Scheduled(fixedRateString = "2000")
//    public void sendWorkerActiveDataToClient(){
//        List<Long> companyList = companyRepository.findAll().stream().map(AbsEntity::getId).collect(Collectors.toList());
//        for (int i = 0; i < companyList.size(); i++) {
//            List<ResWorkerActive> workerActiveList = workerActiveRepository.findAllByCompany(companyList.get(i)).stream().map(WorkerActiveService::getWorkerActive).collect(Collectors.toList());
//            notificationService.sendToAdmin(workerActiveList,companyList.get(i).toString());
//        }
//
//    }

//         ***********************************************************************************

    public ApiResponseModel getUsersByPage(String startTime, String endTime, RoleName roleName, Integer page, Integer size, String search) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            Optional<Role> role = roleRepository.findByName(roleName);
            if (role.isPresent()) {
                Integer roleId = role.get().getId();
                Page<Users> users = userRepository.findAllByCreatedAtBetweenAndRolesQuery(startTime, endTime, roleId, CommonUtils.getPageableById(page, size));
                if (!search.equals("all")) {
                    users = userRepository.findAllByCreatedAtBetweenAndRolesContainsQuery(startTime, endTime, roleId, search,
                            CommonUtils.getPageableById(page, size));
                }
                responseModel.setMessage("Users found");
                responseModel.setSuccess(true);
                responseModel.setMessageType(TrayIcon.MessageType.INFO);
                responseModel.setData(users);
            }
        } catch (Exception e) {
            responseModel.setMessage("Users not found");
            responseModel.setMessageType(TrayIcon.MessageType.ERROR);
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

    public ApiResponseModel getUserInfo(Long id) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from orders where client_id=" + id + " or worker_id=" + id);
            responseModel.setMessage("User orders found");
            responseModel.setSuccess(true);
            responseModel.setMessageType(TrayIcon.MessageType.ERROR);
            responseModel.setData(maps);
        } catch (Exception e) {
            responseModel.setMessage("User orders not found");
            responseModel.setSuccess(false);
            responseModel.setMessageType(TrayIcon.MessageType.ERROR);
        }
        return responseModel;
    }

    public ApiResponse updateBalance(Long id, Double cost) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Users users = userRepository.findById(id).get();
            if (users.getBalance() != null) {
                users.setBalance(users.getBalance() + cost);
            } else {
                users.setBalance(cost);
            }
            userRepository.save(users);
            apiResponse.setMessage("Successful!!!");
            apiResponse.setSuccess(true);
        } catch (Exception e) {
            apiResponse.setMessage("Unsuccessful!!!");
            apiResponse.setSuccess(false);
        }
        return apiResponse;
    }

    public ApiResponseModel getWorkerProfit(Long id) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            List<Map<String, Object>> maps = jdbcTemplate.queryForList("select sum(order_cost) from orders where reject_from is null and order_status_enum=4 and worker_id=" + id);
            if (maps.size() != 0) {
                responseModel.setMessage("Successful");
                responseModel.setSuccess(true);
                responseModel.setData(maps);
            } else {
                responseModel.setMessage("No money!");
                responseModel.setSuccess(false);
            }
        } catch (Exception e) {
            responseModel.setMessage("Unsuccessful");
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

}
