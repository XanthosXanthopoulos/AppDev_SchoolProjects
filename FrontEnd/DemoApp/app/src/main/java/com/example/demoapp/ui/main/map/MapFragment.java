package com.example.demoapp.ui.main.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.CustomMarkerInfoWindowView;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.util.Place;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback
{
    private static final float MAPS_PATH_WIDTH = 10;
    private MapView mapView;
    private GoogleMap map;
    private SearchView search_bar;
    boolean visible;
    Polyline currentPolyline;
    PolylineOptions polylineOptions;
    LocationManager locationManager;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationProviderClient = null;
    private MapViewModel mapViewModel;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(MapViewModel.class);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        if(map.isMyLocationEnabled()) {
            LocationUpdates();
        }
//
//        System.out.println("edw1 " + CameraUpdateFactory.zoomIn());
//        System.out.println("edw2 " + CameraUpdateFactory.zoomTo(5));

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener(){
            @Override
            public void onCameraMove() {
                if(map.getCameraPosition().zoom >= 11.0f){
                    Log.i("SUCCESS","Im ur guy ");
                }
            }

        });

        search_bar = (SearchView) requireView().findViewById(R.id.search_bar);
        Drawable temp = search_bar.getBackground();
        List<Place> PlacesList = getListItemData();

        search_bar.setOnSearchClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                search_bar.setBackground(getResources().getDrawable(R.drawable.search_bar));
            }
        });

        search_bar.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                search_bar.setBackground(temp);
                return false;
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @SuppressLint("ShowToast")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMapClick(LatLng latLng) {

                if (!search_bar.hasFocus()) {

                    if (search_bar.isIconified()) {
                        Toast.makeText(requireContext(),"For adding a marker, you need to long press on the map.",Toast.LENGTH_LONG);
                    }

                    search_bar.setIconified(true);
                    search_bar.setBackground(temp);

                } else {
                    search_bar.clearFocus();
                    search_bar.setBackground(temp);
                }
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                if (search_bar.isIconified()) {
                    addMarker(point, null);
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CustomMarkerInfoWindowView info = new CustomMarkerInfoWindowView(getContext());
                List<LatLng> route = new ArrayList<>();

                route.add(new LatLng(-35.016, 143.321));
                route.add(new LatLng(-34.747, 145.592));
                route.add(new LatLng(-34.364, 147.891));
                route.add(new LatLng(-33.501, 150.217));
                route.add(new LatLng(-32.306, 149.248));
                route.add(new LatLng(-32.491, 147.309));
                if(!visible){
                    visible = true;
                    info.getInfoWindow(marker);
                    drawRoute(route);
                    return false;
                }else {
                    visible = false;
                    info.closeInfoWindow(marker);
                    return true;
                }
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Place p = getPlaceData(PlacesList, query);
                addMarker(Objects.requireNonNull(p).getCoordinates(),p.getDescription());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

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

    public void addMarker(LatLng lng,String desc){
        String title;
        if(desc != null){
            title = desc;
        }else{
            title = " Marker on the point I clicked ";
        }

        map.clear();
        map.addMarker(new MarkerOptions()
                .position(lng)
                .title(title));
        map.moveCamera(CameraUpdateFactory.newLatLng(lng));

        for (int i = 0; i < path.size(); i += 2){
            MarkerOptions point1 = new MarkerOptions().position(path.get(i)).title("Location" + i);
            MarkerOptions point2 = new MarkerOptions().position(path.get(i+1)).title("Location" + i+1);

            String url = createUrl(point1.getPosition(), point2.getPosition(),"walking");
            new FetchURL();

        }

//        polylineOptions = new PolylineOptions().jointType(JointType.ROUND).width(MAPS_PATH_WIDTH).color(getResources().getColor(R.color.purple_500)).addAll(path);
//        Polyline polyLine = map.addPolyline(polylineOptions);
//        polyLine.setPoints(path);
    }

    private String createUrl(LatLng origin , LatLng destination , String pathMode){
        //Origin coordinates
        String str_origin = "origin" + origin.latitude + "," + origin.longitude;
        //Destination coordinates
        String str_destination = "origin" + origin.latitude + "," + origin.longitude;
        //Mode declaration
        String mode = "mode=" + pathMode;
        //Add all these
        String parameters = str_origin + "&" + str_destination + "&" + mode;
        //Output file type
        String outputType = "json";

        String url = "Https://googleapis.com/maps/api/directions/" + outputType + "?" + parameters + "&key" + "${MAPS_API_KEY}";

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

        final Place MELBOURNE = new Place("MELBOURNE", description,new LatLng(-37.813628,144.963058));
        final Place ADELAIDE = new Place("ADELAIDE",new LatLng(-34.928499,138.600746));
        final Place BRISBANE = new Place("BRISBANE",new LatLng(-27.469771,153.025124));
        final Place SYDNEY = new Place("SYDNEY",new LatLng(-33.86882,151.209296));
        final Place PERTH = new Place("PERTH",new LatLng(-31.952312,115.861309));

        listViewItems.add(MELBOURNE);
        listViewItems.add(ADELAIDE);
        listViewItems.add(BRISBANE);
        listViewItems.add(SYDNEY);
        listViewItems.add(PERTH);

        return listViewItems;
    }

    private  Place getPlaceData(List<Place> placeList,String s){
        for (int i = 0; i < placeList.size();i++){
            if(placeList.get(i).getPlace().equals(s.toUpperCase())){
                return placeList.get(i);
            }
        }
        return null;
    }

    public void drawRoute(List<LatLng> location) {
        polylineOptions = new PolylineOptions().width(MAPS_PATH_WIDTH).color(getResources().getColor(R.color.purple_500)).addAll(location);
        Polyline polyLine = map.addPolyline(polylineOptions);
        polyLine.setPoints(location);
    }

    @SuppressLint("MissingPermission")
    private void LocationUpdates(){
        // Create the location request to start receiving updates
        com.google.android.gms.location.FusedLocationProviderClient locationProviderClient = getFusedLocationProviderClient();
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        /* 10 secs */
        long UPDATE_INTERVAL = 10 * 1000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        /* 2 sec */
        long FASTEST_INTERVAL = 2000;
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        locationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    private com.google.android.gms.location.FusedLocationProviderClient getFusedLocationProviderClient() {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        }
        return fusedLocationProviderClient;
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NotNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            onLocationChanged(locationResult.getLastLocation());
        }
    };

    @Override
    public void onLocationChanged(@NonNull Location location) {

        Location mLastLocation = location;
        Marker mCurrLocationMarker = null;

        //Place current location marker
        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()); // the coordinates of the last location
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

    }

    private int getMaxRadiusAfterZoom(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}