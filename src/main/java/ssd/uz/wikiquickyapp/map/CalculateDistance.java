package ssd.uz.wikiquickyapp.map;

import lombok.Data;
import ssd.uz.wikiquickyapp.entity.Location;
import ssd.uz.wikiquickyapp.entity.WorkerActive;
import ssd.uz.wikiquickyapp.payload.ReqLocation;

import java.util.ArrayList;
import java.util.List;

@Data
public class CalculateDistance {

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static List<WorkerActive> searchInRadius(List<WorkerActive> workerActiveList, Location location, Double minDistance) {
        CalculateDistance calDis = new CalculateDistance();
        List<WorkerActive> workerActiveList1 = new ArrayList<>();
        for (WorkerActive workerActive : workerActiveList) {
            Double dis = calDis.distance(workerActive.getLan(), workerActive.getLat(), location.getLan(), location.getLat(), 'K');
            if (dis < minDistance) {
                workerActiveList1.add(workerActive);
            }
        }
        return workerActiveList1;
    }

}
