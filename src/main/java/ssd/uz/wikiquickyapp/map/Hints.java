package ssd.uz.wikiquickyapp.map;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class Hints implements Serializable {

    @SerializedName("visited_nodes.average")
    public double visitedNodesAverage;

    @SerializedName("visited_nodes.sum")
    public int visitedNodesSum;
}