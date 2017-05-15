package tuffery.fr.fadabus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;

import tuffery.fr.fadabus.R;
import tuffery.fr.fadabus.model.BusStop;
import tuffery.fr.fadabus.model.BusStopImage;

/**
 * Created by Aurelien on 15/05/2017.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder>{
    private List<BusStopImage> busStopsImages;
    public ImageListAdapter(List<BusStopImage> busStopsImages){
        this.busStopsImages = busStopsImages;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            holder.dateText.setText(busStopsImages.get(position).date);
    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView busStopImage;
        TextView dateText;

        public ViewHolder(View itemView) {
            super(itemView);
            busStopImage = (ImageView)itemView.findViewById(R.id.busStopImage);
            dateText = (TextView)itemView.findViewById(R.id.dateText);
        }
    }
}
