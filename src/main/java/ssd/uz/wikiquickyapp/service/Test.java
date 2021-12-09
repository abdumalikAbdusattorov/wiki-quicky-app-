package ssd.uz.wikiquickyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.WorkerActiveInRadius;
import ssd.uz.wikiquickyapp.map.websocket.NotificationService;
import ssd.uz.wikiquickyapp.repository.WorkerActiveInRadiusRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class Test {
    @Autowired
    WorkerActiveInRadiusRepository w;
    @Autowired
    WorkerActiveInRadiusRepository wair;
    @Autowired
    NotificationService notificationService;
    @Autowired
    WorkerActiveInRadiusService workerActiveInRadiusService;

    @Scheduled(fixedRateString = "5000")
    public void checkOrderActive() {
        List<WorkerActiveInRadius> www = wair.findAll();
        if (www.size() > 0) {
            int first_order_number = 0;
            ArrayList<List<WorkerActiveInRadius>> lists = new ArrayList<>();
            for (int i = 0; i < www.size(); i++) {
                if (www.get(first_order_number).isUsed() == true) {
                    Long order_active_id = www.get(first_order_number).getOrderActive().getId();
                    List<WorkerActiveInRadius> workerActiveInRadiusList = w.findWAIRByIsUsedAndOrderActiveId2(order_active_id);
                    lists.add(workerActiveInRadiusList);
                    www = w.findAll();
                } else {
                    first_order_number++;
                }
            }
            System.out.println("list list size : ____" + lists.size());
            checkAndDelete(lists);
        }
    }

    private void checkAndDelete(ArrayList<List<WorkerActiveInRadius>> lists){
        for (int i = 0; i < lists.size(); i++) {
            List<WorkerActiveInRadius> workerActiveInRadiusList = lists.get(i);
            for (int j = 0; j < workerActiveInRadiusList.size(); j++) {
                WorkerActiveInRadius wair = workerActiveInRadiusList.get(j);
                if (System.currentTimeMillis()-wair.getCreatedAt().getTime()>12000){
                    notificationService.rejectedOrderActive("false", wair.getOrderActive().getClientId().getId().toString());
                    workerActiveInRadiusService.delete(wair.getOrderActive().getId());
                    workerActiveInRadiusService.deleteOrderActive(wair.getOrderActive().getId());
                }
            }
        }
    }
}
