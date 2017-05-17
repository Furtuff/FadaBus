package tuffery.fr.fadabus.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import tuffery.fr.fadabus.Factory;
import tuffery.fr.fadabus.R;
import tuffery.fr.fadabus.model.BusStopImage;

/**
 * Created by Aurelien on 15/05/2017.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder>{
    public static PopupWindow shareWindow;
    private List<BusStopImage> busStopsImages;
    private String streetName;
    private final static String SHARE_TYPE = "*/*";
    private static final String FILE_DIR = "images";
    private static final String FILE_NAME = "image.png";
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.busStopImage.setImageBitmap(convertByteToBitmap(busStopsImages.get(position).image));
            holder.busStopImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openShareWindow(holder.busStopImage,busStopsImages.get(position));
                }
            });
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
        alertbox.setMessage(v.getContext().getString(R.string.delete_image_message));
        alertbox.setTitle(v.getContext().getString(R.string.delete_image_title));
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
    private void openShareWindow(View v, final BusStopImage busStopImage){
        ConstraintLayout layout = new ConstraintLayout(v.getContext());
        View popupView =  LayoutInflater.from(v.getContext()).inflate(R.layout.image_share, layout);
        final PopupWindow popupWindow = new PopupWindow(popupView, ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT);
        final ImageView shareButton = (ImageView)popupView.findViewById(R.id.shareButton);
        TextView shareText = (TextView)popupView.findViewById(R.id.shareText);
        final Bitmap shareBitmap = convertByteToBitmap(busStopImage.image);
        ImageView shareCancel = (ImageView)popupView.findViewById(R.id.shareCancel);
        shareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                shareWindow = null;
            }
        });
        ImageView shareImage = (ImageView)popupView.findViewById(R.id.shareImage);
        shareImage.setImageBitmap(shareBitmap);
        shareText.setText(busStopImage.title);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(v,shareBitmap,busStopImage);
            }
        });

        popupWindow.setContentView(popupView);
        popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER,0,0);
        shareWindow = popupWindow;
    }
    private void share(View v,Bitmap shareBitmap, BusStopImage busStopImage ){
        Context context = v.getContext();
        saveToInternalStorage(context,shareBitmap);
        File path = new File(context.getCacheDir(), FILE_DIR);
        File newFile = new File(path,FILE_NAME);
        Uri bmpUri = FileProvider.getUriForFile(context,"tuffery.fr.fadabus.fileprovider",newFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, busStopImage.title);
        shareIntent.setDataAndType(bmpUri, context.getContentResolver().getType(bmpUri));
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType(SHARE_TYPE);
        v.getContext().startActivity(shareIntent);
    }
    private void saveToInternalStorage(Context context,Bitmap bitmapImage){
        File cachePath = null;
        try {
            cachePath = new File(context.getCacheDir(), FILE_DIR);
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/" +FILE_NAME);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
