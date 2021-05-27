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
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.demoapp.CustomMarkerInfoWindowView;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.directionhelper.FetchURL;
import com.example.demoapp.data.model.directionhelper.TaskLoadedCallback;
import com.example.demoapp.util.Place;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.SphericalUtil;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback
{
    private static final float MAPS_PATH_WIDTH = 10;
    private MapView mapView;
    private GoogleMap map;
    private SearchView search_bar;
    private ImageButton route;
    boolean visible;
    private Polyline currentPolyline;
    private PolylineOptions polylineOptions;
    private HashMap<String, Marker> nearMarkers;

    private MapViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(MapViewModel.class);

        nearMarkers = new HashMap<>();

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

        route = requireActivity().findViewById(R.id.RouteBtn);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new FetchURL(MapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                new FetchURL(MapFragment.this,requireContext()).execute(returnURL());

            }
        });

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
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                marker.showInfoWindow();

                return true;
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
                Map<String, Activity> newActivities = activities.stream().collect(Collectors.toMap(Activity::getId, Function.identity()));

                Iterator<Map.Entry<String, Marker>> iterator = nearMarkers.entrySet().iterator();
                while (iterator.hasNext())
                {
                    Map.Entry<String, Marker> entry = iterator.next();

                    if (!newActivities.containsKey(entry.getKey()))
                    {
                        entry.getValue().remove();
                        iterator.remove();
                    }
                }

                for (Activity activity : activities)
                {
                    if (!nearMarkers.containsKey(activity.getId())) nearMarkers.put(activity.getId(), addMarker(activity));
                }
            }
        });
    }

    @Override
    public void onTaskDone(Object... values){
        if(currentPolyline != null){
            currentPolyline.remove();
        }
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
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

    private String returnURL(){
        String url = null;

        MarkerOptions point1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
        MarkerOptions point2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");
        url = createUrl(point1.getPosition(), point2.getPosition(),"driving");

        return url;
    }

    private void addPath(List<Activity> activities)
    {
        String url = null;
        map.clear();
        List<LatLng> path = new ArrayList<>(activities.size());

        for (Activity activity : activities)
        {
            LatLng point = new LatLng(activity.getLatitude(), activity.getLongtitude());

            Marker marker = map.addMarker(new MarkerOptions().position(point).title(activity.getTitle()));
            marker.setTag(activity);
            path.add(point);
        }

//        for (int i = 0; i < path.size(); i += 2){
//            MarkerOptions point1 = new MarkerOptions().position(path.get(i)).title("Location" + i);
//            MarkerOptions point2 = new MarkerOptions().position(path.get(i+1)).title("Location" + i+1);
//
//            url = createUrl(point1.getPosition(), point2.getPosition(),"walking");
//
//        }

//        polylineOptions = new PolylineOptions().jointType(JointType.ROUND).width(MAPS_PATH_WIDTH).color(getResources().getColor(R.color.purple_500)).addAll(path);
//        Polyline polyLine = map.addPolyline(polylineOptions);
//        polyLine.setPoints(path);
    }

    private String createUrl(LatLng origin , LatLng destination , String pathMode){
        //Origin's coordinates
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //Destination's coordinates
        String str_destination = "destination=" + destination.latitude + "," + destination.longitude;
        //Mode declaration
        String mode = "mode=" + pathMode;
        //Add all these
        String parameters = str_origin + "&" + str_destination + "&" + mode;
        //Output file type
        String outputType = "json";

        String url = "Https://googleapis.com/maps/api/directions/" + outputType + "?" + parameters + "&key=" + getString(R.string.api_key);

        return url;

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