package com.example.demoapp.ui.main.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Radius;
import com.example.demoapp.data.model.Trip;
import com.example.demoapp.data.model.Type;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.adapter.TripAdapter;
import com.example.demoapp.ui.main.profile.ProfileViewModel;
import com.example.demoapp.util.ViewModelFactory;

import java.util.List;


public class DashboardFragment extends Fragment
{

    private DashboardViewModel dashboardViewModel;

    private Spinner countrySpinner;
    private Spinner typeSpinner;
    private Spinner radiusSpinner;

    private SearchView searchBar;
    private RecyclerView searchResultList;
    SearchResultAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dashboardViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(DashboardViewModel.class);

        countrySpinner = view.findViewById(R.id.country_select);
        typeSpinner = view.findViewById(R.id.type_select);
        radiusSpinner = view.findViewById(R.id.radius_select);
        searchBar = view.findViewById(R.id.search_bar);
        searchResultList = view.findViewById(R.id.search_result_list);

        countrySpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, Country.values()));
        typeSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, Type.values()));
        radiusSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, Radius.values()));

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                String country = ((Country)countrySpinner.getSelectedItem()).label;
                String type = ((Type)typeSpinner.getSelectedItem()).label;
                int radius = ((Radius)radiusSpinner.getSelectedItem()).radius;

                dashboardViewModel.search(query, country, type, radius);
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

        dashboardViewModel.getSearchResult().observe(getViewLifecycleOwner(), new Observer<List<Item>>()
        {
            @Override
            public void onChanged(List<Item> items)
            {
                adapter.setItems(items);
            }
        });

        return view;
    }
}