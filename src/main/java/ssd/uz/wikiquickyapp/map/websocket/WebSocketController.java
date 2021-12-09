package ssd.uz.wikiquickyapp.map.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import ssd.uz.wikiquickyapp.entity.WorkerActive;
import ssd.uz.wikiquickyapp.repository.UserRepository;
import ssd.uz.wikiquickyapp.repository.WorkerActiveRepository;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.Timestamp;

@Controller
@CrossOrigin
public class WebSocketController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionsAndUsersRepository sessionsAndUsersRepository;

    @Autowired
    WorkerActiveRepository workerActiveRepository;

    @Autowired
    NotificationService notificationService;

    @MessageMapping("/edit")
    public void editUserLocations(@Payload Object register,Principal principal) {
        String locations = new String((byte[]) register, StandardCharsets.UTF_8);
        int[] indexs = new int[3];
        int a = 0;
        for (int i = 0; i < locations.length(); i++) {
            if (locations.charAt(i) == '/') {
                indexs[a] = i;
                a++;
            }
        }
        System.out.println("user edit bulyapdi");
        Double lan = Double.parseDouble(locations.substring(indexs[0] + 5, indexs[1]));
        Double lat = Double.parseDouble(locations.substring(indexs[1] + 5, indexs[2]));
        PrincipleAndUsers pu = sessionsAndUsersRepository.selectByPrinciple(principal.getName());
        System.out.println(pu);
        WorkerActive workerActive = workerActiveRepository.findWorkerActive(pu.getUserId());
        if (workerActive.getOrderId()!=null){

        }
        workerActive.setLat(lat);
        workerActive.setLan(lan);
        Timestamp vaqt = new Timestamp(System.currentTimeMillis());
        System.out.println(lan+" - "+lat+" : "+vaqt);
        workerActiveRepository.save(workerActive);
    }

    @MessageMapping("/register")
    public void register(@Payload Object object,Principal principal) {
        String locations = new String((byte[]) object, StandardCharsets.UTF_8);
        System.out.println(locations);
        System.out.println("user registerdan utdi");
        int[] indexs = new int[2];
        int a = 0;
        for (int i = 0; i < locations.length(); i++) {
            if (locations.charAt(i) == '/') {
                indexs[a] = i;
                a++;
            }
        }
        Long id = Long.parseLong(locations.substring(indexs[0] + 8, indexs[1]));
        System.out.println(id);
        WorkerActive workerActive = workerActiveRepository.findWorkerActive(id);
        sessionsAndUsersRepository.save(new PrincipleAndUsers(id, principal.getName(),workerActive.getId()));

    }

    @MessageMapping("/register-client")
    public void registerClient(@Payload Object object,Principal principal) {
        String locations = new String((byte[]) object, StandardCharsets.UTF_8);
        System.out.println(locations);

        int[] indexs = new int[2];
        int a = 0;
        for (int i = 0; i < locations.length(); i++) {
            if (locations.charAt(i) == '/') {
                indexs[a] = i;
                a++;
            }
        }

        Long id = Long.parseLong(locations.substring(indexs[0] + 8, indexs[1]));
        System.out.println(id);
//        WorkerActive workerActive = workerActiveRepository.findWorkerActive(id);
        sessionsAndUsersRepository.save(new PrincipleAndUsers(id, principal.getName(),null));

    }

    @MessageMapping("/unRegister")
    public void unRegister(Principal principal) {
        sessionsAndUsersRepository.deleteSessionsAndUsersByPrinciple(principal.getName());
    }

    public static void main(String[] args) {
        String locations = "/userId:1/lan:21.212122/lat:32.2515151/";
        int[] indexs = new int[4];
        int a = 0;
        for (int i = 0; i < locations.length(); i++) {
            if (locations.charAt(i) == '/') {
                indexs[a] = i;
                a++;
            }
        }
        System.out.println( locations.substring(indexs[0] + 8, indexs[1]));
        System.out.println(locations.substring(indexs[1]+5,indexs[2]));
        System.out.println(locations.substring(indexs[2]+5,indexs[3]));
//        Double lat = Double.parseDouble(locations.substring(indexs[1] + 5, indexs[2]));
    }
}
