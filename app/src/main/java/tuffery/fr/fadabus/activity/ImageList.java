package tuffery.fr.fadabus.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

import tuffery.fr.fadabus.Factory;
import tuffery.fr.fadabus.R;
import tuffery.fr.fadabus.adapter.ImageListAdapter;
import tuffery.fr.fadabus.model.BusStopImage;

public class ImageList extends BaseMenuActivity {
    private final static String EXTRA_BUS_STOP_NAME = "BUS_STOP_NAME";
    RecyclerView imageRecycler;
    ImageListAdapter imageListAdapter;
    ConstraintLayout popupLayout, parentImageList;
    PopupWindow popupWindow;
    String streetName;
    TextView popupDate;
    Button popupValidate;
    ImageView popupPreview;
    EditText popUpTitle;

    public static Intent BuildImageListIntent(Context context, String busStopName){
        Intent intent = new Intent(context, ImageList.class);
        intent.putExtra(EXTRA_BUS_STOP_NAME, busStopName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        imageRecycler = (RecyclerView)findViewById(R.id.imageRecycler);
        parentImageList = (ConstraintLayout)findViewById(R.id.parentImageList);
        if (getIntent().getExtras().get(EXTRA_BUS_STOP_NAME) != null){
            this.streetName = (String) getIntent().getExtras().get(EXTRA_BUS_STOP_NAME);
            getSupportActionBar().setTitle(this.streetName);
            List<BusStopImage> busStopImages = Factory.instance.getIDatabaseManager().getBusStopImages(this,streetName);
            if (busStopImages != null){
                imageListAdapter = new ImageListAdapter(busStopImages, streetName);
                imageRecycler.setAdapter(imageListAdapter);
            }
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            init();
            initiatePopupWindows(imageBitmap);
        }
    }
    private void init(){
        setPopupLayout();
        popUpTitle = (EditText)popupLayout.findViewById(R.id.title);
        popupDate = (TextView)popupLayout.findViewById(R.id.datePreview);
        popupValidate =(Button)popupLayout.findViewById(R.id.validate);
        popupPreview = (ImageView)popupLayout.findViewById(R.id.preview);
    }
    private void initiatePopupWindows(Bitmap image){
        popupWindow =new PopupWindow(this.popupLayout, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        popupPreview.setImageBitmap(image);
        popUpTitle.setText("");
        Calendar calendar = Calendar.getInstance();
        popupDate.setText(Factory.DateFormat.format(calendar.getTimeInMillis()));
        popupValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewImage();
                refreshList();
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(popupLayout);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(parentImageList, Gravity.CENTER,0,0);
    }
    private void setPopupLayout(){
        if (popupLayout == null){
            popupLayout = new ConstraintLayout(this);
            getLayoutInflater().inflate(R.layout.image_list_popup_window,popupLayout);
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private byte[] generateByteArrayImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private void saveNewImage(){
        BusStopImage busStopImage = new BusStopImage();
        busStopImage.date = popupDate.getText().toString();
        busStopImage.title = popUpTitle.getText().toString();
        busStopImage.image = generateByteArrayImage(((BitmapDrawable)popupPreview.getDrawable()).getBitmap());
        Factory.instance.getIDatabaseManager().saveImage(getApplicationContext(),busStopImage,streetName);
    }
    public void refreshList(){
        if (imageListAdapter != null){
            imageListAdapter.swap(Factory.instance.getIDatabaseManager().getBusStopImages(getApplicationContext(),streetName));
        }else {
            imageListAdapter = new ImageListAdapter(Factory.instance.getIDatabaseManager().getBusStopImages(getApplicationContext(),streetName),streetName);
            imageRecycler.setAdapter(imageListAdapter);
        }

    }

    @Override
    protected void onPause() {
        if (ImageListAdapter.shareWindow != null) {
            ImageListAdapter.shareWindow.dismiss();
        }
        super.onPause();
    }
}
