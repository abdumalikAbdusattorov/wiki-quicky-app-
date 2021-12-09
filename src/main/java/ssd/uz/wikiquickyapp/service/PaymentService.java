package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import ssd.uz.wikiquickyapp.collection.PaymentCol;
//import ssd.uz.wikiquickyapp.collection.WorkerBalanceChangeCol;
import ssd.uz.wikiquickyapp.collection.PaymentCol;
import ssd.uz.wikiquickyapp.collection.WorkerBalanceChangeCol;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ApiResponseModel;
import ssd.uz.wikiquickyapp.payload.ReqBalanceChange;
import ssd.uz.wikiquickyapp.payload.ReqCompanyInterest;
import ssd.uz.wikiquickyapp.repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    SuperAdminValuesRepository superAdminValuesRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PayTypeRepository payTypeRepository;
    @Autowired
    WorkerBalanceChangeRepository workerBalanceChangeRepository;

    //order bajarilganda avtomatik tarzda iwga tuwadi, wuning uchun edit, delete funksiyalari bo'lmaydi
    public ApiResponse getCompanyInterestFromOrder(ReqCompanyInterest reqCompanyInterest) {
        ApiResponse response = new ApiResponse();
        try {
            Optional<Order> order = orderRepository.findById(reqCompanyInterest.getOrderId());
            if (order.isPresent()) {
                if (workerBalanceChangeRepository.existsByOrder(order.get().getId())) {
                    return new ApiResponse("Company interest from this order already obtained", false);
                }
                SuperAdminValue superAdminValue = superAdminValuesRepository.findByName("profitPercent").orElseThrow(() -> new ResourceNotFoundException("value", "name", "profitPercent"));
                Users worker = userRepository.findById(order.get().getWorker().getId()).orElseThrow(() -> new ResourceNotFoundException("worker", "id", order.get().getWorker().getId()));
                double profit = order.get().getOrderCost() * superAdminValue.getValue() / 100;
                double leftBalance = order.get().getOrderCost() * (1 - superAdminValue.getValue() / 100);

                WorkerBalanceChange balanceChange = new WorkerBalanceChange();
                balanceChange.setBalance(leftBalance);
                balanceChange.setDiff("-" + profit);
                balanceChange.setOrderId(order.get().getId());
                balanceChange.setWorkerId(worker.getId());
                workerBalanceChangeRepository.save(balanceChange);

                worker.setBalance(leftBalance);
                userRepository.save(worker);
                response.setSuccess(true);
                response.setMessage("Company interest obtained");
            }
        } catch (Exception e) {
            response.setMessage("Error");
            response.setSuccess(false);
        }
        return response;
    }

    public PaymentCol getPayment(Long id) {
        return paymentRepository.findByIdNative(id).orElseThrow(() -> new ResourceNotFoundException("payment", "id", id));
    }

    //statistika uchun
    public ApiResponseModel getAllPaymentByPageable(Integer page, Integer size, String search) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            List<PaymentCol> paymentList = paymentRepository.getAllByPageable(page > 0 ? page * size : 0, size);
            responseModel.setSuccess(true);
            responseModel.setMessage("Found");
            responseModel.setData(paymentList);
        } catch (Exception e) {
            responseModel.setMessage("Error");
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

    public ApiResponse replenishTheBalance(ReqBalanceChange reqBalanceChange) {
        ApiResponse response = new ApiResponse();
        try {
//            PayType payType = payTypeRepository.findById(reqBalanceChange.getPayTypeId()).orElseThrow(() -> new ResourceNotFoundException("paytype", "id", reqBalanceChange.getPayTypeId()));
            Users worker = userRepository.findById(reqBalanceChange.getWorkerId()).orElseThrow(() -> new ResourceNotFoundException("worker", "workerId", reqBalanceChange.getWorkerId()));
            SuperAdminValue superAdminValue = superAdminValuesRepository.findByName("bonus").orElseThrow(() -> new ResourceNotFoundException("value", "name", "bonus"));
            double resultBalance;
            if (worker.getBalance() == null) {
                resultBalance = reqBalanceChange.getDifference() * (100 + superAdminValue.getValue()) / 100;
            } else {
                resultBalance = worker.getBalance() + reqBalanceChange.getDifference() * (100 + superAdminValue.getValue()) / 100;
            }
            worker.setBalance(resultBalance);
            userRepository.save(worker);

            WorkerBalanceChange balanceChange = new WorkerBalanceChange();
            balanceChange.setBalance(resultBalance);
            balanceChange.setDiff("+" + reqBalanceChange.getDifference());
            balanceChange.setWorkerId(worker.getId());
            workerBalanceChangeRepository.save(balanceChange);

            Payment payment = new Payment();
            payment.setSum(reqBalanceChange.getDifference());
            payment.setWorker(worker);
            paymentRepository.save(payment);

            response.setSuccess(true);
            response.setMessage("Balance replenished to difference: " + reqBalanceChange.getDifference());
        } catch (Exception e) {
            response.setMessage("Unknown error⁉");
            response.setSuccess(false);
        }
        return response;
    }

    public ApiResponseModel editBalanceChange(ReqBalanceChange reqBalanceChange) {
        // admin paneldan userlar listidan bitta userni search qilib olinadi,
        // kn owa userni balance changelari korinib tursin, har birida edit bn delete funksiyalari bosin
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            WorkerBalanceChange balanceChange = getBalanceChange(reqBalanceChange.getId());

            Users worker = userRepository.findById(reqBalanceChange.getWorkerId()).orElseThrow(() -> new ResourceNotFoundException("worker", "workerId", reqBalanceChange.getWorkerId()));
            double resultBalance;
            resultBalance = worker.getBalance() + reqBalanceChange.getDifference() - Double.parseDouble(balanceChange.getDiff().substring(1));
            worker.setBalance(resultBalance);
            userRepository.save(worker);

            balanceChange.setDiff(balanceChange.getDiff().substring(0, 1) + reqBalanceChange.getDifference());
            workerBalanceChangeRepository.save(balanceChange);

            responseModel.setSuccess(true);
            responseModel.setMessage("Balance replenished amount changed to : " + reqBalanceChange.getDifference());
        } catch (Exception e) {
            responseModel.setMessage("Error");
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

    public ApiResponseModel deleteBalanceChange(Long id) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            WorkerBalanceChange balanceChange = getBalanceChange(id);

            Users worker = userRepository.findById(balanceChange.getWorkerId()).orElseThrow(() -> new ResourceNotFoundException("worker", "workerId", balanceChange.getWorkerId()));
            double resultBalance;
            resultBalance = worker.getBalance() - Double.parseDouble(balanceChange.getDiff().substring(1));
            worker.setBalance(resultBalance);
            userRepository.save(worker);

            workerBalanceChangeRepository.deleteById(id);

            responseModel.setSuccess(true);
            responseModel.setMessage("Balance change deleted");
        } catch (Exception e) {
            responseModel.setMessage("Error");
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

    public WorkerBalanceChange getBalanceChange(Long id) {
        return workerBalanceChangeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("workerBalanceChange", "id", id));
    }

    public WorkerBalanceChangeCol getBalanceChangeCol(Long id) {
        return workerBalanceChangeRepository.getWorkerBalanceChangeCol(id).orElseThrow(() -> new ResourceNotFoundException("workerBalanceChange", "id", id));
    }


    public ApiResponseModel getAllBalanceChanges(Integer page, Integer size, String search) {
        ApiResponseModel responseModel = new ApiResponseModel();
        try {
            List<WorkerBalanceChangeCol> balanceChangeList = workerBalanceChangeRepository.getAllByWorkerBalanceChange(page > 0 ? page * size : 0, size);
            responseModel.setSuccess(true);
            responseModel.setMessage("Found");
            responseModel.setData(balanceChangeList);
        } catch (Exception e) {
            responseModel.setMessage("Error");
            responseModel.setSuccess(false);
        }
        return responseModel;
    }
}
