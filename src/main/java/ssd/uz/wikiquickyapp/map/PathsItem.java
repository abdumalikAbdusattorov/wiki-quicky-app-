package ssd.uz.wikiquickyapp.map;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class PathsItem implements Serializable {

    @SerializedName("distance")
    public Double distance;

    @SerializedName("transfers")
    public int transfers;

    @SerializedName("weight")
    public double weight;

    @SerializedName("time")
    public int time;

    @SerializedName("snapped_waypoints")
    public String snappedWaypoints;

    @Override
    public String toString() {
        return "PathsItem{" +
                "distance=" + distance +
                ", transfers=" + transfers +
                ", weight=" + weight +
                ", time=" + time +
                ", snappedWaypoints='" + snappedWaypoints + '\'' +
                '}';
    }
}