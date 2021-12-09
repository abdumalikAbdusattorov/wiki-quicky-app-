package ssd.uz.wikiquickyapp.map;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.List;

@Generated("com.asif.gsonpojogenerator")
public class Info implements Serializable {

    @SerializedName("took")
    public int took;

    @SerializedName("copyrights")
    public List<String> copyrights;
}