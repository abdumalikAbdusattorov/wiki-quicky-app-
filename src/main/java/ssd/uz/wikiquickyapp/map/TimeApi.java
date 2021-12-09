package ssd.uz.wikiquickyapp.map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
//import org.apache.http.HttpResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;

public class TimeApi {
    //    public Timestamp getTime() {
//        HttpResponse<String> timeResponse = Unirest.get("http://api.aladhan.com/v1/currentTimestamp?zone=Asia/Tashkent").asString();
//        JsonParser parser = new JsonParser();
//        JsonObject jsonObject = (JsonObject) parser.parse(timeResponse.getBody());
//        JsonElement jsonElement = jsonObject.getAsJsonObject().get("data");
//        long timeLong = Long.parseLong(jsonElement.getAsString());
//        return new Timestamp(timeLong * 1000L);
//    }
    public long getTime() throws IOException {
        long time = 0;
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://api.aladhan.com/v1/currentTimestamp?zone=Asia/Tashkent");
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
        Times times = gson.fromJson(javob, Times.class);
        time = times.getData();
        return time;
    }
}
