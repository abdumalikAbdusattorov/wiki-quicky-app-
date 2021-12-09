package ssd.uz.wikiquickyapp.service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.*;
import ssd.uz.wikiquickyapp.entity.enums.PaysysErrorNotes;
import ssd.uz.wikiquickyapp.payload.*;
import ssd.uz.wikiquickyapp.repository.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class PaysysTransactionService {
    private final int PAYMENT_ID = 17;

    private final int PAYMENT_STATUS_PAID = 2;
    private final int PAYMENT_STATUS_CANCELLED = 3;

    private final int ERROR_SIGN_CHECK_FAILED = -1;
    private final int ERROR_INCORRECT_AMOUNT = -2;
    private final int ERROR_NOT_ENOUGH_PARAMS = -3;
    private final int ERROR_ALREADY_PAID = -4;
    private final int ERROR_USER_NOT_FOUND = -5;
    private final int ERROR_TRANSACTION_NOT_FOUND = -6;
    private final int ERROR_UPDATE_FAILED = -7;
    private final int ERROR_IN_REQUEST = -8;
    private final int ERROR_TRANSACTION_CANCELLED = -9;
    private final int ERROR_VENDOR_NOT_FOUND = -10;

    @Value("${app.paysysSecretKey}")
    private String SECRET_KEY;

    @Value("${app.paysysVendorId}")
    private String VENDOR_ID;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaysysTransactionsRepository paysysTransactionsRepository;

    @Autowired
    WorkerBalanceChangeRepository workerBalanceChangeRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderRepository orderRepository;

    public String getMD5Hash(String SIGN_STRING) {
        MessageDigest md;
        StringBuilder sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance("MD5");
            String str = SECRET_KEY + SIGN_STRING;
            byte[] hashInBytes = md.digest(str.getBytes(StandardCharsets.UTF_8));
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return String.valueOf(sb);
    }

    public ApiResponseModel convertJSON(String jsonString) {
        System.out.println(jsonString);
        try {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> map = gson.fromJson(jsonString, mapType);
            return new ApiResponseModel(true, "success", map);
        } catch (Exception e) {
            Map<String, Object> response1 = new HashMap<>();
            response1.put("ERROR", ERROR_IN_REQUEST);
            response1.put("ERROR_NOTE", PaysysErrorNotes.ERROR_IN_REQUEST.getS());
            System.out.println(response1);
            return new ApiResponseModel(false, "failed", response1);
        }
    }

    public Object errorResponse(Map<String, Object> response, Integer error, String error_note) {
        response.put("ERROR", error);
        response.put("ERROR_NOTE", error_note);
        return response;
    }

    public Object getInfo(String payload) {
        Map<String, Object> objectMap;
        ApiResponseModel responseModel = convertJSON(payload);
        if (responseModel.isSuccess()) {
            objectMap = (Map<String, Object>) responseModel.getData();
        } else {
            System.out.println(responseModel.getData());
            return responseModel.getData();
        }

        Map<String, Object> response = new HashMap<>();
        try {
            if (objectMap.isEmpty()) {
                return errorResponse(response, ERROR_IN_REQUEST, PaysysErrorNotes.ERROR_IN_REQUEST.getS());
            }
            String[] array = {"MERCHANT_TRANS_ID", "SIGN_TIME", "SIGN_STRING"};
            int index = 0;
            for (String s : array) {
                if (objectMap.containsKey(s)) {
                    index++;
                }
            }
            if (index != array.length) {
                return errorResponse(response, ERROR_NOT_ENOUGH_PARAMS, PaysysErrorNotes.ERROR_NOT_ENOUGH_PARAMS.getS());
            }
            if (objectMap.get("MERCHANT_TRANS_ID").equals("")) {
                return errorResponse(response, ERROR_USER_NOT_FOUND, PaysysErrorNotes.ERROR_USER_NOT_FOUND.getS());

            }
            if (!getMD5Hash(String.valueOf(objectMap.get("MERCHANT_TRANS_ID")) + Math.round((Double) objectMap.get("SIGN_TIME"))).equals(objectMap.get("SIGN_STRING"))) {
                return errorResponse(response, ERROR_SIGN_CHECK_FAILED, PaysysErrorNotes.ERROR_SIGN_CHECK_FAILED.getS());
            }
            Optional<Users> user = userRepository.findById(Long.valueOf((String) objectMap.get("MERCHANT_TRANS_ID")));
            if (!user.isPresent()) {
                return errorResponse(response, ERROR_USER_NOT_FOUND, PaysysErrorNotes.ERROR_USER_NOT_FOUND.getS());
            }
            ReqParameters parameters = new ReqParameters(
                    user.get().getFirstName() + " " + user.get().getLastName(),
                    user.get().getBalance() == null ? "0" : user.get().getBalance().toString(),
                    user.get().getEmail() == null ? "" : user.get().getEmail());

            response.put("ERROR", 0);
            response.put("ERROR_NOTE", "Success");
            response.put("PARAMETERS", parameters);
            return response;

        } catch (Exception e) {
            return errorResponse(response, e.hashCode(), e.getMessage());
        }
    }

    public Object pay(String payload) {
        Map<String, Object> objectMap;
        ApiResponseModel responseModel = convertJSON(payload);
        if (responseModel.isSuccess()) {
            objectMap = (Map<String, Object>) responseModel.getData();
        } else {
            return responseModel.getData();
        }

        Map<String, Object> response = new HashMap<>();

        try {
            if (objectMap.isEmpty()) {
                return errorResponse(response, ERROR_IN_REQUEST, PaysysErrorNotes.ERROR_IN_REQUEST.getS());
            }
            String[] array = {"ENVIRONMENT", "VENDOR_ID", "PAYMENT_ID", "PAYMENT_NAME", "AGR_TRANS_ID", "MERCHANT_TRANS_ID", "MERCHANT_TRANS_AMOUNT", "SIGN_TIME", "SIGN_STRING"};
            int index = 0;
            for (String s : array) {
                if (objectMap.containsKey(s)) {
                    index++;
                }
            }
            if (index != array.length) {
                return errorResponse(response, ERROR_NOT_ENOUGH_PARAMS, PaysysErrorNotes.ERROR_NOT_ENOUGH_PARAMS.getS());
            }
            if (objectMap.get("MERCHANT_TRANS_ID").equals("")) {
                return errorResponse(response, ERROR_USER_NOT_FOUND, PaysysErrorNotes.ERROR_USER_NOT_FOUND.getS());

            }
            if (!String.valueOf(objectMap.get("VENDOR_ID")).equals(VENDOR_ID)) {
                return errorResponse(response, ERROR_VENDOR_NOT_FOUND, PaysysErrorNotes.ERROR_VENDOR_NOT_FOUND.getS());
            }
            if (!getMD5Hash(
                    String.valueOf(Math.round((Double) objectMap.get("AGR_TRANS_ID"))) +
                            objectMap.get("VENDOR_ID") +
                            objectMap.get("PAYMENT_ID") +
                            objectMap.get("PAYMENT_NAME") +
                            objectMap.get("MERCHANT_TRANS_ID") +
                            objectMap.get("MERCHANT_TRANS_AMOUNT") +
                            objectMap.get("ENVIRONMENT") +
                            Math.round((Double) objectMap.get("SIGN_TIME"))).equals(objectMap.get("SIGN_STRING"))) {
                return errorResponse(response, ERROR_SIGN_CHECK_FAILED, PaysysErrorNotes.ERROR_SIGN_CHECK_FAILED.getS());

            }
            Optional<Users> user = userRepository.findById(Long.valueOf((String) objectMap.get("MERCHANT_TRANS_ID")));
            if (!user.isPresent()) {
                return errorResponse(response, ERROR_USER_NOT_FOUND, PaysysErrorNotes.ERROR_USER_NOT_FOUND.getS());
            }
            if ((Integer) objectMap.get("MERCHANT_TRANS_AMOUNT") <= 0) {
                return errorResponse(response, ERROR_INCORRECT_AMOUNT, PaysysErrorNotes.ERROR_INCORRECT_AMOUNT.getS());
            }
            Optional<PaysysTransaction> transaction = paysysTransactionsRepository.getTransaction(
                    (int) objectMap.get("VENDOR_ID"),
                    Math.round((Double) objectMap.get("AGR_TRANS_ID")),
                    (int) objectMap.get("PAYMENT_ID"),
                    (String) objectMap.get("MERCHANT_TRANS_ID"));

            long transactionId;
            if (transaction.isPresent()) {
                if (transaction.get().getStatus() == PAYMENT_STATUS_PAID) {
                    return errorResponse(response, ERROR_ALREADY_PAID, PaysysErrorNotes.ERROR_ALREADY_PAID.getS());
                }
                if (transaction.get().getStatus() == PAYMENT_STATUS_CANCELLED) {
                    return errorResponse(response, ERROR_TRANSACTION_CANCELLED, PaysysErrorNotes.ERROR_TRANSACTION_CANCELLED.getS());

                }
                transactionId = transaction.get().getId();
                Optional<PaysysTransaction> updateTransaction = paysysTransactionsRepository.getTransactionByAgrTransId(transactionId);
                updateTransaction.ifPresent(uTransaction -> {
                    uTransaction.setStatus(0);
                    uTransaction.setPayment_name((String) objectMap.get("PAYMENT_NAME"));
                    uTransaction.setEnvironment((String) objectMap.get("ENVIRONMENT"));
                    uTransaction.setMerchant_trans_amount((Float) objectMap.get("MERCHANT_TRANS_AMOUNT"));
                    uTransaction.setSign_time(Math.round((Double) objectMap.get("SIGN_TIME")));
                    paysysTransactionsRepository.save(uTransaction);
                });
            } else {
                PaysysTransaction newTransaction = new PaysysTransaction();
                newTransaction.setAgr_trans_id(Math.round((Double) objectMap.get("AGR_TRANS_ID")));
                newTransaction.setEnvironment((String) objectMap.get("ENVIRONMENT"));
                newTransaction.setStatus(0);
                newTransaction.setMerchant_trans_amount((Float) objectMap.get("MERCHANT_TRANS_AMOUNT"));
                if (objectMap.containsKey("MERCHANT_TRANS_DATA")) {
                    newTransaction.setMerchant_trans_data((String) objectMap.get("MERCHANT_TRANS_DATA"));
                }
                newTransaction.setMerchant_trans_id((String) objectMap.get("MERCHANT_TRANS_ID"));
                newTransaction.setPayment_id((Integer) objectMap.get("PAYMENT_ID"));
                newTransaction.setPayment_name((String) objectMap.get("PAYMENT_NAME"));
                newTransaction.setSign_time(Math.round((Double) objectMap.get("SIGN_TIME")));
                newTransaction.setVendor_id((Integer) objectMap.get("VENDOR_ID"));

                paysysTransactionsRepository.save(newTransaction);
                transactionId = Math.round(paysysTransactionsRepository.getTransaction(
                        newTransaction.getVendor_id(),
                        newTransaction.getAgr_trans_id(),
                        newTransaction.getPayment_id(),
                        newTransaction.getMerchant_trans_id()).get().getId());
            }

            response.put("ERROR", 0);
            response.put("ERROR_NOTE", "Success");
            response.put("PARAMETERS", transactionId);
            return response;
        } catch (Exception e) {
            return errorResponse(response, e.hashCode(), e.getMessage());
        }
    }

    public Object notify(String payload) {
        Map<String, Object> objectMap;
        ApiResponseModel responseModel = convertJSON(payload);
        if (responseModel.isSuccess()) {
            objectMap = (Map<String, Object>) responseModel.getData();
        } else {
            return responseModel.getData();
        }

        Map<String, Object> response = new HashMap<>();

        try {
            if (objectMap.isEmpty()) {
                return errorResponse(response, ERROR_IN_REQUEST, PaysysErrorNotes.ERROR_IN_REQUEST.getS());
            }
            String[] array = {"AGR_TRANS_ID", "VENDOR_TRANS_ID", "STATUS", "SIGN_TIME", "SIGN_STRING"};
            int index = 0;
            for (String s : array) {
                if (objectMap.containsKey(s)) {
                    index++;
                }
            }
            if (index != array.length) {
                return errorResponse(response, ERROR_NOT_ENOUGH_PARAMS, PaysysErrorNotes.ERROR_NOT_ENOUGH_PARAMS.getS());
            }

            if (!getMD5Hash(Math.round((Double) objectMap.get("AGR_TRANS_ID")) +
                    String.valueOf(Math.round((Double) objectMap.get("VENDOR_TRANS_ID"))) +
                    objectMap.get("STATUS") +
                    Math.round((Double) objectMap.get("SIGN_TIME"))).equals(objectMap.get("SIGN_STRING"))) {
                return errorResponse(response, ERROR_SIGN_CHECK_FAILED, PaysysErrorNotes.ERROR_SIGN_CHECK_FAILED.getS());

            }
            Optional<PaysysTransaction> transaction = paysysTransactionsRepository.findById(Math.round((Double) objectMap.get("VENDOR_TRANS_ID")));
            if (!transaction.isPresent()) {
                return errorResponse(response, ERROR_TRANSACTION_NOT_FOUND, PaysysErrorNotes.ERROR_TRANSACTION_NOT_FOUND.getS());
            }
            Optional<Users> user = userRepository.findById(Long.valueOf(transaction.get().getMerchant_trans_id()));
            if (!user.isPresent()) {
                return errorResponse(response, ERROR_USER_NOT_FOUND, PaysysErrorNotes.ERROR_USER_NOT_FOUND.getS());
            }
            if ((int) objectMap.get("SUCCESS") == PAYMENT_STATUS_PAID) {
                double resultBalance;
                if (user.get().getBalance() == null) {
                    resultBalance = transaction.get().getMerchant_trans_amount();
                } else {
                    resultBalance = user.get().getBalance() + transaction.get().getMerchant_trans_amount();
                }
                user.get().setBalance(resultBalance);
                userRepository.save(user.get());

                WorkerBalanceChange balanceChange = new WorkerBalanceChange();
                balanceChange.setBalance(resultBalance);
                balanceChange.setDiff("+" + transaction.get().getMerchant_trans_amount());
                balanceChange.setWorkerId(user.get().getId());
                workerBalanceChangeRepository.save(balanceChange);

                Payment payment = new Payment();
                payment.setSum(transaction.get().getMerchant_trans_amount());
                payment.setWorker(user.get());
                paymentRepository.save(payment);
            }
            boolean updated = paysysTransactionsRepository.updateTransaction(Math.round((Double) objectMap.get("VENDOR_TRANS_ID")), (int) objectMap.get("SUCCESS"));
            if (updated) {
                return errorResponse(response, 0, "Success");
            } else {
                return errorResponse(response, ERROR_UPDATE_FAILED, PaysysErrorNotes.ERROR_UPDATE_FAILED.getS());
            }
        } catch (Exception e) {
            return errorResponse(response, e.hashCode(), e.getMessage());
        }

    }

    public Object cancel(String payload) {
        Map<String, Object> objectMap;
        ApiResponseModel responseModel = convertJSON(payload);
        if (responseModel.isSuccess()) {
            objectMap = (Map<String, Object>) responseModel.getData();
        } else {
            return responseModel.getData();
        }
        Map<String, Object> response = new HashMap<>();

        try {
            if (objectMap.isEmpty()) {
                return errorResponse(response, ERROR_IN_REQUEST, PaysysErrorNotes.ERROR_IN_REQUEST.getS());
            }
            String[] array = {"AGR_TRANS_ID", "VENDOR_TRANS_ID", "SIGN_TIME", "SIGN_STRING"};
            int index = 0;
            for (String s : array) {
                if (objectMap.containsKey(s)) {
                    index++;
                }
            }
            if (index != array.length) {
                return errorResponse(response, ERROR_NOT_ENOUGH_PARAMS, PaysysErrorNotes.ERROR_NOT_ENOUGH_PARAMS.getS());
            }

            if (!getMD5Hash(Math.round((Double) objectMap.get("AGR_TRANS_ID")) +
                    String.valueOf(Math.round((Double) objectMap.get("VENDOR_TRANS_ID"))) +
                    Math.round((Double) objectMap.get("SIGN_TIME"))).equals(objectMap.get("SIGN_STRING"))) {
                return errorResponse(response, ERROR_SIGN_CHECK_FAILED, PaysysErrorNotes.ERROR_SIGN_CHECK_FAILED.getS());
            }

            Optional<PaysysTransaction> transaction = paysysTransactionsRepository.getTransactionByVendorTransId(Math.round((Double) objectMap.get("VENDOR_TRANS_ID")));
            if (!transaction.isPresent()) {
                return errorResponse(response, ERROR_TRANSACTION_NOT_FOUND, PaysysErrorNotes.ERROR_TRANSACTION_NOT_FOUND.getS());
            }

            boolean updated = paysysTransactionsRepository.updateTransaction(Math.round((Double) objectMap.get("VENDOR_TRANS_ID")), ERROR_TRANSACTION_CANCELLED);
            if (!updated) {
                return errorResponse(response, ERROR_UPDATE_FAILED, PaysysErrorNotes.ERROR_UPDATE_FAILED.getS());
            }
            return errorResponse(response, 0, "Success");
        } catch (Exception e) {
            return errorResponse(response, e.hashCode(), e.getMessage());
        }

    }

    public Object statement(String payload) {
        Map<String, Object> objectMap;
        ApiResponseModel responseModel = convertJSON(payload);
        if (responseModel.isSuccess()) {
            objectMap = (Map<String, Object>) responseModel.getData();
        } else {
            return responseModel.getData();
        }

        Map<String, Object> response = new HashMap<>();

        try {
            if (objectMap.isEmpty()) {
                return errorResponse(response, ERROR_IN_REQUEST, PaysysErrorNotes.ERROR_IN_REQUEST.getS());
            }
            String[] array = {"FROM", "TO", "SIGN_TIME", "SIGN_STRING"};
            int index = 0;
            for (String s : array) {
                if (objectMap.containsKey(s)) {
                    index++;
                }
            }
            if (index != array.length) {
                return errorResponse(response, ERROR_NOT_ENOUGH_PARAMS, PaysysErrorNotes.ERROR_NOT_ENOUGH_PARAMS.getS());

            }
            if (!getMD5Hash(Math.round((Double) objectMap.get("FROM")) +
                    String.valueOf(Math.round((Double) objectMap.get("TO"))) +
                    Math.round((Double) objectMap.get("SIGN_TIME"))).equals(objectMap.get("SIGN_STRING"))) {
                return errorResponse(response, ERROR_SIGN_CHECK_FAILED, PaysysErrorNotes.ERROR_SIGN_CHECK_FAILED.getS());
            }
            List<PaysysTransaction> transactions = paysysTransactionsRepository.getTransactions(Math.round((Double) objectMap.get("FROM")), Math.round((Double) objectMap.get("TO")));

            List<ReqTransaction> transactionList = new ArrayList<>();
            transactions.forEach(paysysTransaction -> {
                ReqTransaction reqTransaction = new ReqTransaction();
                reqTransaction.setEnvironment(paysysTransaction.getEnvironment());
                reqTransaction.setAgr_trans_id(String.valueOf(paysysTransaction.getAgr_trans_id()));
                reqTransaction.setVendor_trans_id(String.valueOf(paysysTransaction.getId()));
                reqTransaction.setMerchant_trans_amount(String.valueOf(paysysTransaction.getMerchant_trans_amount()));
                reqTransaction.setMerchant_trans_id(String.valueOf(paysysTransaction.getMerchant_trans_id()));
                reqTransaction.setState(String.valueOf(paysysTransaction.getStatus()));
                reqTransaction.setDate(String.valueOf(paysysTransaction.getSign_time()));
            });
            response.put("ERROR", 0);
            response.put("ERROR_NOTE", "Success");
            response.put("TRANSACTIONS", transactionList);
            return response;
        } catch (Exception e) {
            return errorResponse(response, e.hashCode(), e.getMessage());
        }
    }
}
