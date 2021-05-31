package com.example.demoapp.ui.main.dashboard;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.actions.FollowActions;
import com.example.demoapp.data.CommentLikeClickListener;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Place;
import com.example.demoapp.data.model.Radius;
import com.example.demoapp.data.model.Type;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.location_picker.LocationActivity;
import com.example.demoapp.util.ViewModelFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class DashboardFragment extends Fragment
{
    private static final int LOCATION_SELECT = 0;

    private DashboardViewModel dashboardViewModel;

    private AutoCompleteTextView countrySpinner;
    private AutoCompleteTextView citySpinner;
    private Spinner typeSpinner;
    private Spinner radiusSpinner;

    private ImageButton locationButton;

    private TextView listMessage;

    private SearchView searchBar;
    private RecyclerView searchResultList;
    private SearchResultAdapter adapter;

    private double latitude;
    private double longitude;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dashboardViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(DashboardViewModel.class);

        countrySpinner = view.findViewById(R.id.country_select);
        citySpinner = view.findViewById(R.id.city_select);
        typeSpinner = view.findViewById(R.id.type_select);
        radiusSpinner = view.findViewById(R.id.radius_select);
        listMessage = view.findViewById(R.id.list_message);
        searchBar = view.findViewById(R.id.search_bar);
        searchResultList = view.findViewById(R.id.search_result_list);
        locationButton = view.findViewById(R.id.locate_button);

        countrySpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Country.values()));
        countrySpinner.setValidator(new AutoCompleteTextView.Validator()
        {
            @Override
            public boolean isValid(CharSequence text)
            {
                int index = Arrays.binarySearch(Country.values(), text.toString(), new Comparator<Serializable>()
                {
                    @Override
                    public int compare(Serializable o1, Serializable o2)
                    {
                        if (o1 instanceof Country && o2 instanceof String)
                        {
                            return  ((Country) o1).label.compareTo((String)o2);
                        }
                        else
                        {
                            return 1;
                        }
                    }
                });

                citySpinner.setEnabled(index >= 0);
                dashboardViewModel.getCities(Country.values()[index].label);

                return index >= 0;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText)
            {
                return null;
            }
        });

        citySpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Country.values()));
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                radiusSpinner.setEnabled(position != 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                radiusSpinner.setEnabled(false);
            }
        });

        typeSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Type.values()));
        radiusSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Radius.values()));

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                String country = countrySpinner.getEditableText().toString();
                String city = citySpinner.getEditableText().toString();
                String type = ((Type)typeSpinner.getSelectedItem()).label;
                int radius = ((Radius)radiusSpinner.getSelectedItem()).radius;

                dashboardViewModel.search(query, country, city, type, radius, latitude, longitude);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        searchResultList.setLayoutManager(_sGridLayoutManager);
        adapter = new SearchResultAdapter();
        searchResultList.setAdapter(adapter);

        adapter.setCommentLikeClickListener(new CommentLikeClickListener()
        {
            @Override
            public void sendComment(int postID, String content)
            {
                dashboardViewModel.sendComment(postID, content);
            }

            @Override
            public void sendLike(int postID)
            {
                dashboardViewModel.sendLike(postID);
            }
        });

        dashboardViewModel.getSearchResult().observe(getViewLifecycleOwner(), event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);
            adapter.setItems(event.getData());

            if (event.getData().size() == 0)
            {
                listMessage.setVisibility(View.VISIBLE);
            }
            else
            {
                listMessage.setVisibility(View.GONE);
            }
        });

        dashboardViewModel.getCitiesResult().observe(getViewLifecycleOwner(), strings -> citySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, strings)));

        adapter.setActions(new FollowActions()
        {
            @Override
            public void follow(String userID)
            {
                dashboardViewModel.sendFollowRequest(userID);
            }

            @Override
            public void accept(String userID) { }

            @Override
            public void decline(String userID) { }

            @Override
            public void unfollow(String userID)
            {
                dashboardViewModel.unfollow(userID);
            }

            @Override
            public void remove(String userID) { }

            @Override
            public void cancel(String userID)
            {
                dashboardViewModel.cancelFollowRequest(userID);
            }
        });

        locationButton.setOnClickListener(v -> startActivityForResult(new Intent(getActivity(), LocationActivity.class), 0));

        dashboardViewModel.getLocationInfo().observe(getViewLifecycleOwner(), event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);

            countrySpinner.setText(event.getData().getCountry());
            citySpinner.setText(event.getData().getCity());
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_SELECT && resultCode == RESULT_OK && data != null)
        {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);

            dashboardViewModel.getLocationInfo(latitude, longitude);
        }
    }
}