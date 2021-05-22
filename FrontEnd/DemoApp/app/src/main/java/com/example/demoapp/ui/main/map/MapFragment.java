package com.example.demoapp.ui.main.map;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraManager;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.demoapp.CustomMarkerInfoWindowView;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.util.Place;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.SphericalUtil;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback
{
    private static final float MAPS_PATH_WIDTH = 10;
    private MapView mapView;
    private GoogleMap map;
    private SearchView search_bar;
    boolean visible;
    PolylineOptions polylineOptions;
    private ArrayList<Marker> nearMarkers;

    private MapViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(MapViewModel.class);

        nearMarkers = new ArrayList<>();

        if (getArguments() != null)
        {
            viewModel.getActivities(getArguments().getInt("PostID"));
        }

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        search_bar = (SearchView) requireView().findViewById(R.id.search_bar);
        Drawable temp = search_bar.getBackground();
        List<Place> PlacesList = getListItemData();

        map.clear();
        map.setInfoWindowAdapter(new CustomMarkerInfoWindowView(getContext()));

        search_bar.setOnSearchClickListener(new View.OnClickListener()
        {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v)
            {
                search_bar.setBackground(getResources().getDrawable(R.drawable.search_bar));
            }
        });

        search_bar.setOnCloseListener(new SearchView.OnCloseListener()
        {

            @Override
            public boolean onClose()
            {
                search_bar.setBackground(temp);
                return false;
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(@NonNull @NotNull Marker marker)
            {
                return false;
            }
        });

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
        {
            @Override
            public void onCameraIdle()
            {
                if (map.getCameraPosition().zoom >= 8.0f)
                {
                    LatLng center = map.getCameraPosition().target;
                    viewModel.getActivities(center.latitude, center.longitude, getMaxRadiusAfterZoom());
                }
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Place p = getPlaceData(PlacesList, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        viewModel.getActivitiesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Activity>>()
        {
                @Override
            public void onChanged(List<Activity> activities)
            {
                addPath(activities);
            }
        });

        viewModel.getNearActivitiesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Activity>>()
        {
            @Override
            public void onChanged(List<Activity> activities)
            {
                for (Marker marker : nearMarkers)
                {
                    marker.remove();
                }

                nearMarkers.clear();

                for (Activity activity : activities)
                {
                    nearMarkers.add(addMarker(activity));
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

    private void addPath(List<Activity> activities)
    {
        map.clear();
        List<LatLng> path = new ArrayList<>(activities.size());

        for (Activity activity : activities)
        {
            LatLng point = new LatLng(activity.getLatitude(), activity.getLongtitude());

            Marker marker = map.addMarker(new MarkerOptions().position(point).title(activity.getTitle()));
            marker.setTag(activity);
            path.add(point);
        }

        polylineOptions = new PolylineOptions().jointType(JointType.ROUND).width(MAPS_PATH_WIDTH).color(getResources().getColor(R.color.purple_500)).addAll(path);
        Polyline polyLine = map.addPolyline(polylineOptions);
        polyLine.setPoints(path);
    }

    public Marker addMarker(Activity activity)
    {
        LatLng point = new LatLng(activity.getLatitude(), activity.getLongtitude());

        Marker marker = map.addMarker(new MarkerOptions().position(point).title(activity.getTitle()));
        marker.setTag(activity);

        return marker;
    }

    private List<Place> getListItemData()
    {
        List<Place> listViewItems = new ArrayList<>();
        String description = "The capital of Australia";

        final Place MELBOURNE = new Place("MELBOURNE", description, new LatLng(-37.813628, 144.963058));
        final Place ADELAIDE = new Place("ADELAIDE", new LatLng(-34.928499, 138.600746));
        final Place BRISBANE = new Place("BRISBANE", new LatLng(-27.469771, 153.025124));
        final Place SYDNEY = new Place("SYDNEY", new LatLng(-33.86882, 151.209296));
        final Place PERTH = new Place("PERTH", new LatLng(-31.952312, 115.861309));

        listViewItems.add(MELBOURNE);
        listViewItems.add(ADELAIDE);
        listViewItems.add(BRISBANE);
        listViewItems.add(SYDNEY);
        listViewItems.add(PERTH);

        return listViewItems;
    }

    private Place getPlaceData(List<Place> placeList, String s)
    {
        for (int i = 0; i < placeList.size(); i++)
        {
            if (placeList.get(i).getPlace().equals(s.toUpperCase()))
            {
                return placeList.get(i);
            }
        }
        return null;
    }

    private double getMaxRadiusAfterZoom()
    {
        LatLng center = map.getCameraPosition().target;
        VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
        LatLng farLeft = visibleRegion.farLeft;

        return SphericalUtil.computeDistanceBetween(center, farLeft);
    }

}