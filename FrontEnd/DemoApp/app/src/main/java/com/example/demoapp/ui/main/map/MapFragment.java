package com.example.demoapp.ui.main.map;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.demoapp.CustomMarkerInfoWindowView;
import com.example.demoapp.R;
import com.example.demoapp.util.Place;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        search_bar = (SearchView) requireView().findViewById(R.id.search_bar);
        Drawable temp = search_bar.getBackground();
        List<Place> PlacesList = getListItemData();

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

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMapClick(LatLng latLng)
            {

                if (!search_bar.hasFocus())
                {

                    if (search_bar.isIconified())
                    {
                        addMarker(latLng, null);
                    }

                    search_bar.setIconified(true);
                    search_bar.setBackground(temp);

                }
                else
                {
                    search_bar.clearFocus();
                    search_bar.setBackground(temp);
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                CustomMarkerInfoWindowView info = new CustomMarkerInfoWindowView(getContext());
                ;
                if (!visible)
                {
                    visible = true;
                    info.getInfoWindow(marker);
                    return false;
                }
                else
                {
                    visible = false;
                    info.closeInfoWindow(marker);
                    return true;
                }
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Place p = getPlaceData(PlacesList, query);
                addMarker(Objects.requireNonNull(p).getCoordinates(), p.getDescription());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
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

    public void addMarker(LatLng lng, String desc)
    {
        String title;
        if (desc != null || desc.equals(""))
        {
            title = desc;
        }
        else
        {
            title = " Marker on the point I clicked ";
        }
        map.clear();
        map.addMarker(new MarkerOptions()
                .position(lng)
                .title(title));
        map.moveCamera(CameraUpdateFactory.newLatLng(lng));
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

}