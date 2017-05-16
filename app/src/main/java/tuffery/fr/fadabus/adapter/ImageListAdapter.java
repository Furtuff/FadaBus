package tuffery.fr.fadabus.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;

import tuffery.fr.fadabus.Factory;
import tuffery.fr.fadabus.R;
import tuffery.fr.fadabus.model.BusStop;
import tuffery.fr.fadabus.model.BusStopImage;

/**
 * Created by Aurelien on 15/05/2017.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder>{
    private List<BusStopImage> busStopsImages;
    private String streetName;
    public ImageListAdapter(List<BusStopImage> busStopsImages, String streetName){
        this.streetName =streetName;
        this.busStopsImages = busStopsImages;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.busStopImage.setImageBitmap(convertByteToBitmap(busStopsImages.get(position).image));
            holder.dateText.setText(busStopsImages.get(position).date);
            holder.titleText.setText(busStopsImages.get(position).title);
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(v,busStopsImages.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return busStopsImages.size();
    }

    private Bitmap convertByteToBitmap(byte[] image){
        BitmapFactory.Options option = null;
        option = new BitmapFactory.Options();
        option.inSampleSize = 2;
        return BitmapFactory.decodeByteArray(image, 0, image.length, option);
    }
    public void swap(List<BusStopImage> newList){
        busStopsImages.clear();
        busStopsImages.addAll(newList);
        notifyDataSetChanged();
    }
    private void showAlertDialog(View v, final BusStopImage busStopImage, final int position){
        final AlertDialog alertbox = new AlertDialog.Builder(v.getRootView().getContext()).create();
        alertbox.setMessage("Are you Sure ?");
        alertbox.setTitle("Delete image");
        RelativeLayout relativeLayout = new RelativeLayout(v.getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
        View alertView = layoutInflater.inflate(R.layout.alert_box_delete, relativeLayout);
        alertView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertbox.dismiss();
            }
        });
        alertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Factory.instance.getIDatabaseManager().deleteImage(v.getContext(),position,streetName);
                busStopsImages.remove(busStopImage);
                notifyDataSetChanged();
                alertbox.dismiss();
            }
        });
        alertbox.setView( alertView);
        alertbox.show();
    }
     class ViewHolder extends RecyclerView.ViewHolder{
        ImageView busStopImage, trash;
        TextView dateText,titleText;

         ViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView)itemView.findViewById(R.id.titleElement);
            busStopImage = (ImageView)itemView.findViewById(R.id.busStopImage);
            dateText = (TextView)itemView.findViewById(R.id.dateText);
             trash = (ImageView)itemView.findViewById(R.id.trash);
        }
    }
}
