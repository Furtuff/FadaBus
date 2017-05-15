package tuffery.fr.fadabus.contract;

import android.content.Context;

import java.util.HashSet;

import tuffery.fr.fadabus.model.BusStopImage;

/**
 * Created by Aurelien on 15/05/2017.
 */

public interface IDatabaseManager {
    HashSet<BusStopImage> getBusStopImages(Context context, String id);
    void saveImage(Context context, BusStopImage busStopImage, String id);
    void deleteImage(Context context,BusStopImage busStopImage, String id);
}
