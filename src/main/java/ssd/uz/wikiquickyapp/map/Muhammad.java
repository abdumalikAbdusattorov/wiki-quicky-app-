package ssd.uz.wikiquickyapp.map;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.List;

@Generated("com.asif.gsonpojogenerator")
public class Muhammad implements Serializable {

    @SerializedName("hints")
    public Hints hints;

    @SerializedName("paths")
    public List<PathsItem> paths;

    @SerializedName("info")
    public Info info;

}