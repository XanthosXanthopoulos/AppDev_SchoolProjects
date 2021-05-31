package com.example.demoapp.ui.main.search;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.demoapp.R;
import com.example.demoapp.data.ActivityItemClickListener;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Place;
import com.example.demoapp.data.model.Radius;
import com.example.demoapp.data.model.Type;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.location_picker.LocationActivity;
import com.example.demoapp.util.ViewModelFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity
{
    private static final int LOCATION_SELECT = 0;
    private SearchViewModel viewModel;

    private AutoCompleteTextView countrySpinner;
    private AutoCompleteTextView citySpinner;
    private Spinner typeSpinner;
    private Spinner radiusSpinner;

    private ImageButton locationButton;

    private TextView listMessage;

    private SearchView searchBar;
    private RecyclerView searchResultList;
    private SearchResultAdapter adapter;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(SearchViewModel.class);

        countrySpinner = findViewById(R.id.country_select);
        citySpinner = findViewById(R.id.city_select);
        typeSpinner = findViewById(R.id.type_select);
        radiusSpinner = findViewById(R.id.radius_select);
        listMessage = findViewById(R.id.list_message);
        searchBar = findViewById(R.id.search_bar);
        searchResultList = findViewById(R.id.search_result_list);
        locationButton = findViewById(R.id.locate_button);

        typeSpinner.setVisibility(View.GONE);

        countrySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Country.values()));
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
                viewModel.getCities(Country.values()[index].label);

                return index >= 0;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText)
            {
                return null;
            }
        });

        citySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Country.values()));
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

        radiusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Radius.values()));

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                String country = countrySpinner.getEditableText().toString();
                String city = citySpinner.getEditableText().toString();
                int radius = ((Radius)radiusSpinner.getSelectedItem()).radius;

                viewModel.search(query, country, city, Type.ACTIVITY.label, radius, latitude, longitude);
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

        adapter.setItemClickListener(activity ->
        {
            Intent data = new Intent();
            data.putExtra("id", activity.getId());
            data.putExtra("title", activity.getTitle());
            data.putExtra("country", activity.getCountry());
            data.putExtra("city", activity.getCity());
            data.putExtra("address", activity.getAddress());
            data.putExtra("description", activity.getDescription());
            data.putExtra("tags", activity.getTags());
            setResult(RESULT_OK, data);
            finish();
        });

        searchResultList.setAdapter(adapter);

        viewModel.getSearchResult().observe(this, event ->
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

        viewModel.getCitiesResult().observe(this, strings -> citySpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strings)));

        locationButton.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(), LocationActivity.class), 0));

        viewModel.getLocationInfo().observe(this, event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);

            countrySpinner.setText(event.getData().getCountry());
            citySpinner.setText(event.getData().getCity());
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_SELECT && resultCode == RESULT_OK && data != null)
        {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);

            viewModel.getLocationInfo(latitude, longitude);
        }
    }
}