package com.example.demoapp.ui.location_picker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.demoapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private static LatLng location;
    private View view;
    private GoogleMap map;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        view = findViewById(android.R.id.content).getRootView();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        MapView view = findViewById(R.id.map_view2);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
//        mMap.setOnCameraMoveListener(requireContext());
        LatLng center = map.getCameraPosition().target;
        Marker marker = map.addMarker(new MarkerOptions().position(center));
        ImageButton confirm = findViewById(R.id.confirm_btn);

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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng markerPosition = marker.getPosition();
                setLocation(markerPosition);
                Intent intent = new Intent(LocationActivity.this, LocationActivity.class);
//                intent.putExtras(getLocation());
            }
        });

    }

    public static void setLocation(LatLng location) {
        com.example.demoapp.ui.location_picker.LocationActivity.location = location;
    }

    public static LatLng getLocation() {
        return location;
    }

}