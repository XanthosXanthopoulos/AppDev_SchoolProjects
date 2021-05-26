package com.example.demoapp.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.R;
import com.example.demoapp.ui.main.map.LocationFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView mTextView;
    private MapView mapView;
    private GoogleMap map;
    private SearchView search_bar;
    boolean visible;
    private ArrayList<Marker> nearMarkers;
    private static LatLng location;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

//        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(MapViewModel.class);
//
//        if (getArguments() != null) {
//            viewModel.getActivities(getArguments().getInt("PostID"));
//        }

        mapView = requireViewById(R.id.location_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
//        mMap.setOnCameraMoveListener(requireContext());
        LatLng center = map.getCameraPosition().target;
        Marker marker = map.addMarker(new MarkerOptions().position(center));

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng center = map.getCameraPosition().target;
                marker.setPosition(center);
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                LatLng markerPosition = marker.getPosition();
                setLocation(markerPosition);
            }
        });

    }

    public static void setLocation(LatLng location) {
        LocationActivity.location = location;
    }

    public static LatLng getLocation() {
        return location;
    }

}