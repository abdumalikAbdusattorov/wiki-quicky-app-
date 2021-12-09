package ssd.uz.wikiquickyapp.map.fcm;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;

@Service
public class FcmService {

    private static final String key = "AAAANT4adts:APA91bHuwJm1KjWn3Gf30zOftlHiU6XNCipVGUelcmXO2ANIawymRgScjvPVxigb7vpMLk3HIqrgCxnIL4PNNk9sWEiKxrb7O3qnic_iBDsiVXRqD65bHRGc7EJFGSFz8XJGE1j6fyD-";

    public void sendMessage(String deviceToken,String title,String message,String role) {

        ReqFcm2 reqFcm2 = new ReqFcm2(new ReqFcm(title,message,role),"/"+deviceToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key="+key+"");
        String url = "https://fcm.googleapis.com/fcm/send";
        RestTemplate template = new RestTemplate();
        HttpEntity<ReqFcm2> entity = new HttpEntity<>(reqFcm2, headers);
        template.postForLocation(url, entity);
        String sss = reqFcm2.toString();
        StringBuilder s = new StringBuilder(sss);
        System.out.println(s);

    }

    public void sendMessage2(String deviceToken) {

        ReqFcm2 reqFcm2 = new ReqFcm2(new ReqFcm("sdfg","sdfg", RoleName.ROLE_WORKER.name()),"/"+deviceToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key="+key+"");
        String url = "https://fcm.googleapis.com/fcm/send";
        RestTemplate template = new RestTemplate();
        HttpEntity<ReqFcm2> entity = new HttpEntity<>(reqFcm2, headers);
        template.postForLocation(url, entity);
        String sss = reqFcm2.toString();
        StringBuilder s = new StringBuilder(sss);
        System.out.println(s);

    }

}
