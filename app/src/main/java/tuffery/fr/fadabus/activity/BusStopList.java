package tuffery.fr.fadabus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import tuffery.fr.fadabus.Factory;
import tuffery.fr.fadabus.R;
import tuffery.fr.fadabus.adapter.BusStopAdapter;
import tuffery.fr.fadabus.contract.BusListListener;
import tuffery.fr.fadabus.model.BusStop;

/**
 * Created by Aurelien on 15/05/2017.
 */

public class BusStopList extends AppCompatActivity implements BusListListener {
RecyclerView busStopRecycler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_list);
        Factory.instance.getICommunicationManager().addBusListListener(this);
        Factory.instance.getICommunicationManager().askBusStopList(this);
        busStopRecycler = (RecyclerView)findViewById(R.id.busStopRecycler);

    }

    @Override
    public void bustopList(final tuffery.fr.fadabus.model.BusStopList busStopList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusStopAdapter busStopAdapter = new BusStopAdapter(busStopList.busStops);
                busStopRecycler.setAdapter(busStopAdapter);
            }
        });

    }
}
