package tuffery.fr.fadabus.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import tuffery.fr.fadabus.R;
import tuffery.fr.fadabus.model.*;
import tuffery.fr.fadabus.model.BusStopList;

public class BusStopMaps extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {
    public static final String EXTRA_BUS_STOP_LIST = "BUS_STOP_LIST";
    private GoogleMap mMap;
    private List<BusStop> busStops;
    public static Intent BusStopMapsIntentBuilder(Context context, tuffery.fr.fadabus.model.BusStopList extraList){
        Intent intent = new Intent(context,BusStopMaps.class);
        intent.putExtra(EXTRA_BUS_STOP_LIST, extraList);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busStops = ((BusStopList)getIntent().getExtras().get(EXTRA_BUS_STOP_LIST)).busStops;
        setContentView(R.layout.activity_bus_stop_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void initGeoloc(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location myLocation = locationManager.getLastKnownLocation(provider);
            if(myLocation!=null) {
                LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            View contentView = getLayoutInflater().inflate(R.layout.bus_stop_map_marker,null);
            @Override
            public View getInfoWindow(final Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                TextView title = (TextView)contentView.findViewById(R.id.streetName);
                title.setText(marker.getTitle());
                ImageButton nextButton = (ImageButton)contentView.findViewById(R.id.nextButton);
                nextButton.setImageResource(R.drawable.bus_stop_map_marker_right_arrow);
                return contentView;
            }
        });
        initGeoloc();
        if (busStops != null){
            for (BusStop b : busStops){
                mMap.addMarker(new MarkerOptions()
                .position(new LatLng(b.lat,b.lon))
                .snippet("d")
                .title(b.streetName)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop_maps_pin)));
            }
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        startActivity(ImageList.BuildImageListIntent(this,marker.getTitle()));

    }
}
