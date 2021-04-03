package com.example.demoapp.ui.main.map;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.demoapp.CustomMarkerInfoWindowView;
import com.example.demoapp.R;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


import org.jetbrains.annotations.NotNull;

public class MapFragment extends Fragment implements OnMapReadyCallback
{
    private MapView mapView;
    private GoogleMap map;
    private SearchView search_bar;
    boolean visible;

    private MapViewModel mapViewModel;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(MapViewModel.class);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        search_bar = (SearchView) getView().findViewById(R.id.search_bar);
        Drawable temp = search_bar.getBackground();

        search_bar.setOnSearchClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                search_bar.setBackground(getResources().getDrawable(R.drawable.search_bar));
            }
        });

        search_bar.setOnCloseListener(new SearchView.OnCloseListener(){

            @Override
            public boolean onClose() {
                search_bar.setBackground(temp);
                return false;
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMapClick(LatLng latLng) {

                if(!search_bar.hasFocus()) {

                    if(search_bar.isIconified()) {
                        map.clear();
                        map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(" Marker on the point I clicked "));
                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }

                    search_bar.setIconified(true);
                    search_bar.setBackground(temp);

                }else{
                    search_bar.clearFocus();
                    search_bar.setBackground(temp);
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CustomMarkerInfoWindowView info = new CustomMarkerInfoWindowView(getContext());;
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

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}