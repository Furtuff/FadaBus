package tuffery.fr.fadabus.activity;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import tuffery.fr.fadabus.R;

/**
 * Created by Aurelien on 17/05/2017.
 */

public abstract class BaseMenuActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected tuffery.fr.fadabus.model.BusStopList busStops;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this instanceof BusStopList) {
            getMenuInflater().inflate(R.menu.bus_stop_list_menu, menu);
        }else if (this instanceof ImageList){
            getMenuInflater().inflate(R.menu.image_list_menu,menu);
        }else if(this instanceof BusStopMaps){
            getMenuInflater().inflate(R.menu.bus_stop_maps_menu,menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                dispatchTakePictureIntent();
                return true;
            case R.id.map:
                startActivity(BusStopMaps.BusStopMapsIntentBuilder(this,busStops));
                return true;
            case R.id.list:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
