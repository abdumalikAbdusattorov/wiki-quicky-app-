package ssd.uz.wikiquickyapp.map.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ResOrderActive;

@Component
public class NotificationService {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public ApiResponse sendToUser(ResOrderActive orderActiveId, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/notify/"+userId+"",orderActiveId , createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }

    // ** bu method bilan hamma clientlarga workerlarni qayerda ekanligini kursatish uchun ishlatiladi :
    public ApiResponse sendToClient(String lan_lat, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/for-client/"+userId+"", lan_lat, createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }

    // ** bu method bilan clientni order-activini worker qabul qilsa clientga usha workerning malumotlarini kursatadi :
    public ApiResponse sendToClientForTime(String arrivalTime, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/for-time/"+userId+"", arrivalTime, createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    // ******************************************************************************************************************** //
//
    public ApiResponse sendToClientArrivalA(String message, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/arrival-a/"+userId+"", message, createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }

    public ApiResponse sendToClientArrivalB(String message, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/arrival-b/"+userId+"", message, createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }

    public ApiResponse sendToClientOrderFinished(String message, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/order-finish/"+userId+"", message, createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }

    public ApiResponse cencelOrder(String message, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/order-cancel/"+userId+"", message, createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }

//    ******************** delete notification ********************************  //

    public ApiResponse deleteNotification(String message, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/order-notify-cancel/"+userId+"", message, createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }

    public ApiResponse rejectedOrderActive(String message, String userId) {
        try {
            simpMessagingTemplate.convertAndSend( "/user/reject-order-active/"+userId+"", message, createHeaders(userId));
            return new ApiResponse("send order active to user successfully", true);
        } catch (Exception e) {
            return new ApiResponse("failed on sending order active to user", false);
        }
    }


    //  *********************************************************************************************************************//
//    public ApiResponse sendToAdmin(List<ResWorkerActive> resWorkerActives, String userId) {
//        try {
//            simpMessagingTemplate.convertAndSend( "/user/arrival-b/"+userId+"", resWorkerActives, createHeaders(userId));
//            return new ApiResponse("send order active to user successfully", true);
//        } catch (Exception e) {
//            return new ApiResponse("failed on sending order active to user", false);
//        }
//    }
}
