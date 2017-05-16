package tuffery.fr.fadabus.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import tuffery.fr.fadabus.contract.IDatabaseManager;
import tuffery.fr.fadabus.model.BusStopImage;

/**
 * Created by Aurelien on 15/05/2017.
 */

public enum  Database implements IDatabaseManager{
    instance;
    private final static String PREF_FILE_NAME = "FADASBUS";
    private SharedPreferences prefs;
    private Gson gson;
    private void init(Context context){
        if (prefs == null){
            prefs = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        }
        if (gson == null){
            gson = new Gson();
        }
    }
    private void storeImages(Context context, List<BusStopImage> busStopImages, String key){
        init(context);
        Type listType = new TypeToken<List<BusStopImage>>(){}.getType();
        String jsonImages = gson.toJson(busStopImages,listType);
        prefs.edit().putString(key, jsonImages).apply();
    }

    private List<BusStopImage> getImages(Context context, String key){
        init(context);
        String jsonImages = prefs.getString(key,"");
        Type listType = new TypeToken<List<BusStopImage>>(){}.getType();
        return gson.fromJson(jsonImages,listType);
    }


    @Override
    public List<BusStopImage> getBusStopImages(Context context, String id) {
        return getImages(context,id);
    }

    @Override
    public void saveImage(Context context, BusStopImage busStopImage, String id) {
        List<BusStopImage> busStopImages = getImages(context,id);
        if (busStopImages == null){
            busStopImages = new ArrayList<>();
        }
        busStopImages.add(busStopImage);
        storeImages(context,busStopImages, id);
    }

    @Override
    public void deleteImage(Context context, int position, String id) {
        List<BusStopImage> busStopImages = getImages(context,id);
        if (busStopImages != null) {
            busStopImages.remove(position);
            storeImages(context, busStopImages, id);
        }
    }
}
