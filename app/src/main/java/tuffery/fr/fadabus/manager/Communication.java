package tuffery.fr.fadabus.manager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuffery.fr.fadabus.contract.BusListListener;
import tuffery.fr.fadabus.contract.ICommunicationManager;
import tuffery.fr.fadabus.contract.ServicesInterface;
import tuffery.fr.fadabus.model.BusStopList;
import tuffery.fr.fadabus.model.Data;
import tuffery.fr.fadabus.services.GetBusStopListService;

/**
 * Created by Aurelien on 15/05/2017.
 */

public enum  Communication implements ICommunicationManager {
    instance;
    private static ServicesInterface servicesInterface;
    private static BusListListener busListListener;

    private void init(Context context) {
        if (servicesInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            servicesInterface = retrofit.create(ServicesInterface.class);
        }
    }

    @Override
    public void askBusStopList(Context context) {
        context.startService(GetBusStopListService.intentBuildGetBusStopListService(context));

    }

    @Override
    public void addBusListListener(AppCompatActivity activity) {
        busListListener = (BusListListener) activity;
    }

    public void getBusStopList(Context context){
        init(context);
        Response<Data> response = null;
        Call<Data> call = servicesInterface.getBusStopList();
        try {
            response = call.execute();
        }catch (IOException e){}

        if (response != null){

            if (response.body() != null){
               if (busListListener !=null){
                   busListListener.bustopList(response.body().busStopList);
               }
            }
        }
    }
}
