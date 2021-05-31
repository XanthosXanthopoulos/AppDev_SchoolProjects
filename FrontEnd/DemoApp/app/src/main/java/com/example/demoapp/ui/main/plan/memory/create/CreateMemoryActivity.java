package com.example.demoapp.ui.main.plan.memory.create;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.ui.location_picker.LocationActivity;
import com.example.demoapp.util.ViewModelFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

public class CreateMemoryActivity extends AppCompatActivity
{
    private static final int LOCATION_SELECT = 0;
    private CreateMemoryViewModel viewModel;

    private EditText titleEditText;
    private AutoCompleteTextView countryEditText;
    private AutoCompleteTextView cityEditText;
    private EditText addressEditText;
    private EditText descriptionEditText;
    private EditText tagsEditText;

    private ImageButton locateButton;

    private Button submitButton;
    private Button cancelButton;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_memory);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(CreateMemoryViewModel.class);

        titleEditText = findViewById(R.id.activity_title);
        countryEditText = findViewById(R.id.country_select);
        cityEditText = findViewById(R.id.city_select);
        addressEditText = findViewById(R.id.activity_address);
        descriptionEditText = findViewById(R.id.activity_description);
        tagsEditText = findViewById(R.id.activity_tags);
        locateButton = findViewById(R.id.locate_button);
        submitButton = findViewById(R.id.add_activity);
        cancelButton = findViewById(R.id.cancel_activity);

        countryEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Country.values()));
        countryEditText.setValidator(new AutoCompleteTextView.Validator()
        {
            @Override
            public boolean isValid(CharSequence text)
            {
                int index = Arrays.binarySearch(Country.values(), text.toString(), (Comparator<Serializable>) (o1, o2) ->
                {
                    if (o1 instanceof Country && o2 instanceof String)
                    {
                        return  ((Country) o1).label.compareTo((String)o2);
                    }
                    else
                    {
                        return 1;
                    }
                });

                if (index > 0)viewModel.getCities(Country.values()[index].label);
                cityEditText.setEnabled(index > 0);

                return index > 0;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText)
            {
                return Country.ANY.label;
            }
        });

        viewModel.getCitiesResult().observe(this, strings -> cityEditText.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strings)));

        viewModel.getStoredActivity().observe(this, activity ->
        {
            if (activity != null)
            {
                titleEditText.setText(activity.getTitle());
                countryEditText.setText(activity.getCountry().toString());
                addressEditText.setText(activity.getAddress());
                descriptionEditText.setText(activity.getDescription());
                tagsEditText.setText(activity.getTags());
            }
        });

        viewModel.getMemoryFormStateFormState().observe(this, createMemoryFormState ->
        {
            if (createMemoryFormState.isDataValid())
            {
                titleEditText.setError(null);
                countryEditText.setError(null);

                submitButton.setEnabled(true);

                return;
            }

            if (createMemoryFormState.getTitleError() != null)
            {
                titleEditText.setError(getString(createMemoryFormState.getTitleError()));
            }

            if (createMemoryFormState.getCountryError() != null)
            {
                countryEditText.setError(getString(createMemoryFormState.getCountryError()));
            }

            submitButton.setEnabled(false);
        });

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                viewModel.memoryDataChanged(titleEditText.getText().toString(), Country.lookupByLabel(countryEditText.getText().toString()));
            }
        };

        titleEditText.addTextChangedListener(afterTextChangedListener);
        countryEditText.addTextChangedListener(afterTextChangedListener);

        locateButton.setOnClickListener(v -> startActivityForResult(new Intent(getApplicationContext(), LocationActivity.class), LOCATION_SELECT));

        submitButton.setOnClickListener(v ->
        {
            if (countryEditText.getText().toString().trim().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Please select a country", Toast.LENGTH_LONG).show();
                return;
            }

            String title = titleEditText.getText().toString();
            Country country = Country.lookupByLabel(countryEditText.getText().toString());
            String city = cityEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String tags = tagsEditText.getText().toString();

            Intent data = new Intent();
            data.putExtra("title", title);
            data.putExtra("country", country);
            data.putExtra("city", city);
            data.putExtra("address", address);
            data.putExtra("description", description);
            data.putExtra("tags", tags);
            data.putExtra("latitude", latitude);
            data.putExtra("longitude", longitude);
            setResult(RESULT_OK, data);
            finish();
        });

        cancelButton.setOnClickListener(v ->
        {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
        });

        viewModel.getLocationInfo().observe(this, event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);

            countryEditText.setText(event.getData().getCountry());
            cityEditText.setText(event.getData().getCity());
            addressEditText.setText(event.getData().getAddress());
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