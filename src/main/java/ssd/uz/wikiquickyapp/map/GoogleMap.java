package ssd.uz.wikiquickyapp.map;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import ssd.uz.wikiquickyapp.entity.Location;
import ssd.uz.wikiquickyapp.payload.ReqAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GoogleMap {
    public static Double getDistance(ReqAddress reqAddress) throws IOException {
        Double distance;
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://graphhopper.com/api/1/route?point=" + reqAddress.getReqLocations().get(0).getLan() + "," + reqAddress.getReqLocations().get(0).getLat() + "&point=" + reqAddress.getReqLocations().get(1).getLan() + "," + reqAddress.getReqLocations().get(1).getLat() + "&vehicle=car&locale=uz&calc_points=true&key=ff3be0ad-d1d2-4672-8957-e3da3784bf5e");
        HttpResponse response = client.execute(httpGet);
        InputStream inputStream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        String javob = " ";
        while (line != null) {
            javob += line;
            line = reader.readLine();
        }
        Gson gson = new Gson();
        Muhammad m = gson.fromJson(javob, Muhammad.class);
        distance = m.paths.get(0).distance;
        return distance;
    }

    public static MapObject getDistanceDouble(Location location1, Location location2) throws IOException {
        Double distance;
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://graphhopper.com/api/1/route?point=" + location1.getLan() + "," + location1.getLat() + "&point=" + location2.getLan() + "," + location2.getLat() + "&vehicle=car&locale=uz&calc_points=false&key=ff3be0ad-d1d2-4672-8957-e3da3784bf5e");
        HttpResponse response = client.execute(httpGet);
        InputStream inputStream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        String javob = " ";
        while (line != null) {
            javob += line;
            line = reader.readLine();
        }
        Gson gson = new Gson();
        Muhammad m = gson.fromJson(javob, Muhammad.class);
        distance = m.paths.get(0).distance;
        MapObject mapObject = new MapObject(distance, m.paths.get(0).time);
        return mapObject;
    }
}

