package tuffery.fr.fadabus.contract;

import retrofit2.Call;
import retrofit2.http.GET;
import tuffery.fr.fadabus.model.Data;

/**
 * Created by Aurelien on 15/05/2017.
 */

public interface ServicesInterface {
    @GET("1.json")
    Call<Data> getBusStopList();
}
