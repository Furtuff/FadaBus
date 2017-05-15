package tuffery.fr.fadabus.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aurelien on 15/05/2017.
 */

public class BusStopList implements Serializable {
    @SerializedName("nearstations")
    public List<BusStop> busStops;
}
