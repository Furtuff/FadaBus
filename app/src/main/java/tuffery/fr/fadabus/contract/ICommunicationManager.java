package tuffery.fr.fadabus.contract;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Aurelien on 15/05/2017.
 */

public interface ICommunicationManager {
    void askBusStopList(Context context);
    void addBusListListener(AppCompatActivity activity);
}
