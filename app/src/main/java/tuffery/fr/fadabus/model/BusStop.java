package tuffery.fr.fadabus.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Aurelien on 15/05/2017.
 */

public class BusStop implements Serializable{
    @SerializedName("id")
    public String id;
    @SerializedName("street_name")
    public String streetName;
    @SerializedName("lat")
    public double lat;
    @SerializedName("lon")
    public double lon;

}
