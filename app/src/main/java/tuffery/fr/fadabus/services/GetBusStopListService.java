package tuffery.fr.fadabus.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.Serializable;
import java.util.List;

import tuffery.fr.fadabus.Factory;
import tuffery.fr.fadabus.manager.Communication;

public class GetBusStopListService extends IntentService {
    public GetBusStopListService() {
        super("GetBusStopService");
    }

    public static Intent intentBuildGetBusStopListService(Context context) {
        Intent intent = new Intent(context, GetBusStopListService.class);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null){
            Communication.instance.getBusStopList(this);
        }
    }
}
