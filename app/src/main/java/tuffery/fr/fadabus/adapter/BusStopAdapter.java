package tuffery.fr.fadabus.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tuffery.fr.fadabus.R;
import tuffery.fr.fadabus.activity.ImageList;
import tuffery.fr.fadabus.model.BusStop;

/**
 * Created by Aurelien on 15/05/2017.
 */

public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.ViewHolder>{
    private List<BusStop> busStops;
    private Context context;
    public BusStopAdapter(Context context,List<BusStop> busStops){
        this.context =context;
        this.busStops = busStops;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bus_stop,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.busStopName.setText(busStops.get(position).streetName);
        holder.busStopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(ImageList.BuildImageListIntent(context,busStops.get(position).streetName));
            }
        });
    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView busStopName;

        public ViewHolder(View itemView) {
            super(itemView);
            busStopName = (TextView)itemView.findViewById(R.id.busStopName);
        }
    }
}
