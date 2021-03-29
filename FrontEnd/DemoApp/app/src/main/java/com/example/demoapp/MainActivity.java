
package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Context context = this;
    boolean visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
//        mapFragment.getMapAsync( googleMap -> {
//            CustomMarkerInfoWindowView markerWindowView = new CustomMarkerInfoWindowView(context);
//            googleMap.setInfoWindowAdapter(markerWindowView);
//        });
        
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
                //.setMapStyle(MapStyleOptions.loadRawResourceStyle());

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            private final LatLng MELBOURNE = new LatLng(-37.813628,144.963058);;
            private final LatLng ADELAIDE = new LatLng(-34.928499,138.600746);;
            private final LatLng BRISBANE = new LatLng(-27.469771,153.025124);;
            private final LatLng SYDNEY = new LatLng(-33.86882,151.209296);;
            private final LatLng PERTH = new LatLng(-31.952312,115.861309);;

            @Override
            public void onMapClick(LatLng latLng) {
//                LatLng lp = new LatLng(-25.274398,133.775136);
//                LatLngBounds bounds = new LatLngBounds.Builder()
//                        .include(PERTH)
//                        .include(SYDNEY)
//                        .include(ADELAIDE)
//                        .include(BRISBANE)
//                        .include(MELBOURNE)
//                        .build();
//                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(" Marker on the point I clicked "));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CustomMarkerInfoWindowView info = new CustomMarkerInfoWindowView(context);;
                if(!visible){
                    visible = true;
                    info.getInfoWindow(marker);
                    return false;
                }else {
                    visible = false;
                    info.closeInfoWindow(marker);
                    return true;
                }
            }
        });

    }

}