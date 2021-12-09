package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.collection.UsersCol;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.*;
import ssd.uz.wikiquickyapp.utils.CommonUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SuperAdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SuperAdminValuesRepository superAdminValuesRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CompanyRepository companyRepository;

    public ApiResponse createAdmin(ReqSignUp reqAdmin) {
        ApiResponseModel response = new ApiResponseModel();
        try {
            Users newAdmin = new Users();
            if (userRepository.existsByPhoneNumber(reqAdmin.getPhoneNumber())) {
                Optional<Users> user = userRepository.findByPhoneNumber(reqAdmin.getPhoneNumber());
                if (user.isPresent()) {
                    newAdmin = user.get();
                }
            } else {
                newAdmin.setFirstName(reqAdmin.getFirstName());
                newAdmin.setLastName(reqAdmin.getLastName());
                newAdmin.setIsVerified(true);
                newAdmin.setPhoneNumber(reqAdmin.getPhoneNumber());
                newAdmin.setPassword(passwordEncoder.encode(reqAdmin.getPassword()));
                newAdmin.setCompany(companyRepository.findById(reqAdmin.getCompanyId()).get());
            }
            newAdmin.setRoles(roleRepository.findAllByNameIn(
                    Arrays.asList(RoleName.ROLE_ADMIN)
            ));
            userRepository.save(newAdmin);
            response.setMessage("New admin is created.");
            response.setSuccess(true);
            response.setMessageType(TrayIcon.MessageType.INFO);
            response.setData(newAdmin);
        } catch (Exception e) {
            response.setMessage("Error");
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
        }
        return response;
    }

    public ApiResponse editAdmin(ReqSignUp reqAdmin) {
        ApiResponse response = new ApiResponse();
        Optional<Users> oldAdmin = userRepository.findById(reqAdmin.getId());
        try {
            if (oldAdmin.isPresent()) {
                Users admin = oldAdmin.get();
                admin.setPassword(reqAdmin.getPassword());
                admin.setFirstName(reqAdmin.getFirstName());
                admin.setLastName(reqAdmin.getLastName());
                userRepository.save(admin);
                response.setMessage("Admin is edited");
                response.setSuccess(true);
                response.setMessageType(TrayIcon.MessageType.INFO);
            } else {
                response.setMessage("Admin not found");
                response.setSuccess(false);
                response.setMessageType(TrayIcon.MessageType.WARNING);
            }
        } catch (Exception e) {
            response.setMessage("Error");
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
        }
        return response;
    }

    public ApiResponse deleteAdmin(Long id) {
        Optional<Users> admin = userRepository.findById(id);
        Role role = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new ResourceNotFoundException("role", "name", RoleName.ROLE_ADMIN));
        admin.ifPresent(user -> user.getRoles().remove(role));
        return new ApiResponse("Admin is deleted", true);
    }


    public ApiResponseModel blockOrUnblockAdmin(Long id) {
        Optional<Users> user = userRepository.findById(id);
        Optional<Role> roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN);
        Optional<Role> roleBlocked = roleRepository.findByName(RoleName.ROLE_BLOCKED);
        if (user.isPresent() && roleAdmin.isPresent() && roleBlocked.isPresent()) {
            if (user.get().getRoles().contains(roleAdmin.get()) && user.get().getRoles().contains(roleBlocked.get())) {
                List<Role> roles = new ArrayList<>(user.get().getRoles());
                roles.remove(roleBlocked.get());
                user.get().setRoles(roles);
                userRepository.save(user.get());
                return new ApiResponseModel(true, "Admin is unblocked", user.get());
            } else if (user.get().getRoles().contains(roleAdmin.get()) && !user.get().getRoles().contains(roleBlocked.get())) {
                List<Role> roles = new ArrayList<>(user.get().getRoles());
                roles.add(roleBlocked.get());
                user.get().setRoles(roles);
                userRepository.save(user.get());
                return new ApiResponseModel(true, "Admin is blocked", user.get());
            }
            return new ApiResponseModel(true, "User is not admin", user.get());
        }
        return new ApiResponseModel(true, "User not found");
    }


    public ApiResponseModel getAllAdmins() {
        List<Users> admins = userRepository.findAllByRoles(roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new ResourceNotFoundException("roleName", "roleName", RoleName.ROLE_ADMIN)));
        return new ApiResponseModel(true, "Found", admins);
    }

    public ApiResponseModel getAdmin(Long id) {
        Users admin = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return new ApiResponseModel(true, "Found", admin);
    }


    public ApiResponse addValues(ReqValues reqValues) {
        ApiResponse response = new ApiResponse();
        try {
            SuperAdminValue superAdminValues = new SuperAdminValue();
            superAdminValues.setName(reqValues.getName());
            superAdminValues.setValue(reqValues.getValue());
            superAdminValuesRepository.save(superAdminValues);
            response.setMessage("Value is added.");
            response.setSuccess(true);
            response.setMessageType(TrayIcon.MessageType.INFO);
        } catch (Exception e) {
            response.setMessage("Error");
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
        }
        return response;
    }

//    public ApiResponseModel editValues(Map<String, Object> changes){
//        ApiResponseModel apiResponseModel = new ApiResponseModel();
//        SuperAdminValues values = superAdminValuesRepository.findById((long) 1).orElseThrow(() -> new ResourceNotFoundException("SuperAdminValues"));
//        changes.forEach(
//                (change, value) -> {
//                    switch (change){
//                        case "profitPercent": values.setProfitPercent((Double) value); break;
//                        case "costForPerKM": values.setCostForPerKM((Double) value); break;
//                        case "costForMinDistance": values.setCostForMinDistance((Double) value); break;
//                        case "minDistance": values.setMinDistance((Double) value); break;
//                        case "maxDistance": values.setMaxDistance((Double) value); break;
//                        case "pointsPerOrderLike": values.setPointsPerOrderLike((Double) value); break;
//                        case "pointsPerOrderDislike": values.setPointsPerOrderDislike((Double) value); break;
//                    }
//                }
//        );
//        superAdminValuesRepository.save(values);
//        apiResponseModel.setMessage("Edited");
//        apiResponseModel.setSuccess(true);
//        apiResponseModel.setObject(values);
//        return apiResponseModel;
//    }

    public ApiResponseModel editValues(Long valueId, ReqValues reqValues) {
        try {
            SuperAdminValue value = superAdminValuesRepository.findById(valueId).orElseThrow(() -> new ResourceNotFoundException("SuperAdminValues", "value", valueId));
            value.setValue(reqValues.getValue());
            superAdminValuesRepository.save(value);
            return new ApiResponseModel(true, "Edited", value);
        } catch (Exception e) {
            return new ApiResponseModel(false, "Error");
        }

    }

    public ReqValues getValue(Long valueId) {
        SuperAdminValue value = superAdminValuesRepository.findById(valueId).orElseThrow(() -> new ResourceNotFoundException("SuperAdminValues", "value", valueId));
        return new ReqValues(value.getName(), value.getValue());
    }

    public ApiResponseModel getValues() {
        List<ReqValues> values = superAdminValuesRepository.findAll().stream().map(value -> getValue(value.getId())).collect(Collectors.toList());
        return new ApiResponseModel(true, "Found", values);
    }

    public ApiResponse deleteValue(Long valueId) {
        if (superAdminValuesRepository.existsById(valueId)) {
            superAdminValuesRepository.deleteById(valueId);
            return new ApiResponse("Deleted", true);
        }
        return new ApiResponse("Not found", false);
    }


    public ApiResponseModel addOrEditVehicleType(ReqVehicleType reqVehicleType) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            VehicleType vehicleType = new VehicleType();
            if (reqVehicleType.getId() != null) {
                vehicleType = vehicleTypeRepository.findById(reqVehicleType.getId()).orElseThrow(() -> new ResourceNotFoundException("VehicleType", "id", reqVehicleType.getId()));
                responseModel.setMessage("Vehicle type edited");
            }
            vehicleType.setName(reqVehicleType.getType());
            vehicleTypeRepository.save(vehicleType);
            responseModel.setSuccess(true);
            responseModel.setMessage("Vehicle type added");
            responseModel.setData(vehicleType);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage("Error");
        }
        return responseModel;
    }

    public ReqVehicleType getVehicleType(Long valueId) {
        VehicleType vehicleType = vehicleTypeRepository.findById(valueId).orElseThrow(() -> new ResourceNotFoundException("VehicleType", "valueId", valueId));
        return new ReqVehicleType(vehicleType.getId(), vehicleType.getName());
    }

    public ApiResponseModel getVehicleTypes() {
        List<ReqVehicleType> vehicleTypes = vehicleTypeRepository.findAll().stream().map(vehicleType -> getVehicleType(vehicleType.getId())).collect(Collectors.toList());
        return new ApiResponseModel(true, "Found", vehicleTypes);
    }

    public ApiResponse deleteVehicleType(Long valueId) {
        if (vehicleTypeRepository.existsById(valueId)) {
            vehicleTypeRepository.deleteById(valueId);
            return new ApiResponse("Deleted", true);
        }
        return new ApiResponse("Not found", false);
    }

    /*******************Reports*******************/

    // jami foydalanuvchilar soni(vaqt oraligida)
    public ApiResponseModel getAllUsersCountInTimeInterval(String startTime, String endTime) {
        ApiResponseModel response = new ApiResponseModel();
        try {
            Long workerOrderCount = userRepository.countAllUsersCountInTimeInterval(startTime, endTime);
            response.setData(workerOrderCount);
            response.setSuccess(true);
            response.setMessageType(TrayIcon.MessageType.INFO);
            response.setMessage("Found");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
            response.setMessage("Not found");
        }
        return response;
    }

    // Vaqt oraligida sistemaga qoshilgan userlani role boyicha koriw
    public ApiResponseModel getUsers(String startTime, String endTime, RoleName roleName, Integer page, Integer size, String search) {
        ApiResponseModel response = new ApiResponseModel();
        try {
            Optional<Role> role = roleRepository.findByName(roleName);
            if (role.isPresent()) {
                Integer roleId = role.get().getId();
                Page<Users> users = userRepository.findAllByCreatedAtBetweenAndRolesQuery(
                        startTime,
                        endTime,
                        roleId,
                        CommonUtils.getPageableById(page, size));
                if (!search.equals("all")) {
                    users = userRepository.findAllByCreatedAtBetweenAndRolesContainsQuery(
                            startTime,
                            endTime,
                            roleId,
                            search,
                            CommonUtils.getPageableById(page, size));
                }
                response.setMessage("Found");
                response.setSuccess(true);
                response.setMessageType(TrayIcon.MessageType.INFO);
                response.setData(users);
            }
        } catch (Exception e) {
            response.setMessage("Not found");
            response.setMessageType(TrayIcon.MessageType.ERROR);
            response.setSuccess(false);
        }
        return response;
    }

    //Vaqt oraligida sistemaga qoshilgan userlani role boyicha sonini koriw
    public ApiResponseModel getUsersCount(String startTime, String endTime, RoleName roleName) {
        ApiResponseModel response = new ApiResponseModel();
        try {
            Optional<Role> role = roleRepository.findByName(roleName);
            if (role.isPresent()) {
                Integer roleId = role.get().getId();
                Long users = userRepository.countByCreatedAtBetweenAndRolesQuery(
                        startTime,
                        endTime,
                        roleId);
                response.setMessage("Found");
                response.setSuccess(true);
                response.setMessageType(TrayIcon.MessageType.INFO);
                response.setData(users);
            }
        } catch (Exception e) {
            response.setMessage("Not found");
            response.setMessageType(TrayIcon.MessageType.ERROR);
            response.setSuccess(false);
        }
        return response;
    }

    // Vaqt oraligida jami zakazlar soni
    public ApiResponseModel getOrdersCount(String startTime, String endTime) {
        ApiResponseModel response = new ApiResponseModel();
        try {
            Long ordersCount = orderRepository.findAllByOrderCount(
                    startTime,
                    endTime);
            response.setData(ordersCount);
            response.setSuccess(true);
            response.setMessageType(TrayIcon.MessageType.INFO);
            response.setMessage("Found");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
            response.setMessage("Not found");
        }
        return response;
    }

    // Vaqt oraligida jami zakazlar soni vehicleType boyicha
    public ApiResponseModel getOrdersCountByVehicleType(String startTime, String endTime, String vehicleType) {
        ApiResponseModel response = new ApiResponseModel();
        try {
            VehicleType vehicleType1 = vehicleTypeRepository.findByName(vehicleType).orElseThrow(() -> new ResourceNotFoundException("vehicleType", "name", vehicleType));
//            List<Map<String, Object>> maps = jdbcTemplate.queryForList("select count(*) from orders o where o.worker_id in (select u.id from users u where u.vehicle_id in (select v.id from vehicle v where v.vehicle_type_id=:vehicleTypeId)) and o.created_at between cast(:startTime as timestamp) and cast(:endTime as timestamp)",
//                    new MapSqlParameterSource()
//                            .addValue("startTime", startTime)
//                            .addValue("endTime", endTime)
//                            .addValue("vehicleTypeId", vehicleTypeId));
            Long ordersCount = orderRepository.countAllByVehicleType(
                    startTime,
                    endTime,
                    vehicleType1.getId());
            response.setData(ordersCount);
            response.setSuccess(true);
            response.setMessageType(TrayIcon.MessageType.INFO);
            response.setMessage("Found");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
            response.setMessage("Not found");
        }
        return response;
    }

    // Vaqt oraligida jami workerla soni vehicleType boyicha
    public ApiResponseModel getWorkersCountByVehicleType(String startTime, String endTime, String vehicleType) {
        ApiResponseModel response = new ApiResponseModel();
        try {
            VehicleType vehicleType1 = vehicleTypeRepository.findByName(vehicleType).orElseThrow(() -> new ResourceNotFoundException("vehicleType", "name", vehicleType));
            Long workersCount = userRepository.countByCreatedAtBetweenAndWorkerAndVehicleType(
                    startTime,
                    endTime,
                    vehicleType1.getId());
            response.setData(workersCount);
            response.setSuccess(true);
            response.setMessageType(TrayIcon.MessageType.INFO);
            response.setMessage("Found");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
            response.setMessage("Found");
        }
        return response;
    }


}
