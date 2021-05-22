package com.example.demoapp.ui.main.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.demoapp.R;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class LocationFragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private SearchView search_bar;
    boolean visible;
    private ArrayList<Marker> nearMarkers;
    private static LatLng location;

    private MapViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

//        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(MapViewModel.class);
//
//        if (getArguments() != null) {
//            viewModel.getActivities(getArguments().getInt("PostID"));
//        }

        mapView = view.findViewById(R.id.location_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
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
        LocationFragment.location = location;
    }

    public static LatLng getLocation() {
        return location;
    }
}