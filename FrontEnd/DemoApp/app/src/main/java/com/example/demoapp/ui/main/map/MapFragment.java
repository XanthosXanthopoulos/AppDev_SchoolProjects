package com.example.demoapp.ui.main.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.CustomMarkerInfoWindowView;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener
{
    private static final float MAPS_PATH_WIDTH = 10;
    private MapView mapView;
    private GoogleMap map;
    private SearchView search_bar;
    PolylineOptions polylineOptions;
    private HashMap<String, Marker> nearMarkers;

    private MapViewModel viewModel;

    private LocationManager locationManager;

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
    public void onMapReady(@NotNull GoogleMap googleMap)
    {
        map = googleMap;

        search_bar = requireView().findViewById(R.id.search_bar);
        Drawable temp = search_bar.getBackground();

        map.clear();
        map.setInfoWindowAdapter(new CustomMarkerInfoWindowView(getContext()));

        search_bar.setOnSearchClickListener(v -> search_bar.setBackground(getResources().getDrawable(R.drawable.search_bar)));

        search_bar.setOnCloseListener(() ->
        {
            search_bar.setBackground(temp);
            return false;
        });

        map.setOnMarkerClickListener(marker ->
        {
            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            marker.showInfoWindow();

            return true;
        });

        map.setOnCameraIdleListener(() ->
        {
            if (map.getCameraPosition().zoom >= 14.0f)
            {
                LatLng center = map.getCameraPosition().target;
                viewModel.getActivities(center.latitude, center.longitude, getMaxRadiusAfterZoom());
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                viewModel.searchPlace(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        viewModel.getActivitiesLiveData().observe(getViewLifecycleOwner(), event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);
            addPath(event.getData());
        });

        viewModel.getNearActivitiesLiveData().observe(getViewLifecycleOwner(), event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);

            Map<String, Activity> newActivities = event.getData().stream().collect(Collectors.toMap(Activity::getId, Function.identity()));

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

            for (Activity activity : event.getData())
            {
                if (!nearMarkers.containsKey(activity.getId())) nearMarkers.put(activity.getId(), addMarker(activity));
            }
        });

        viewModel.getSearchResult().observe(getViewLifecycleOwner(), event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event.getData().getLatitude(), event.getData().getLongitude()), 13));
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (getArguments() == null && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000)
            {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
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

        if (path.size() > 0) map.moveCamera(CameraUpdateFactory.newLatLngZoom(path.get(0), 12));
    }

    public Marker addMarker(Activity activity)
    {
        LatLng point = new LatLng(activity.getLatitude(), activity.getLongtitude());

        Marker marker = map.addMarker(new MarkerOptions().position(point).title(activity.getTitle()));
        marker.setTag(activity);

        return marker;
    }

    private double getMaxRadiusAfterZoom()
    {
        LatLng center = map.getCameraPosition().target;
        VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
        LatLng farLeft = visibleRegion.farLeft;

        return SphericalUtil.computeDistanceBetween(center, farLeft);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        locationManager.removeUpdates(this);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}