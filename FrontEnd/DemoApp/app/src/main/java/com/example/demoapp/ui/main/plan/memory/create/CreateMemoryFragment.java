package com.example.demoapp.ui.main.plan.memory.create;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.ui.location_picker.LocationActivity;
import com.example.demoapp.util.ViewModelFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CreateMemoryFragment extends Fragment
{

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_memory, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(CreateMemoryViewModel.class);

        titleEditText = view.findViewById(R.id.activity_title);
        countryEditText = view.findViewById(R.id.country_select);
        cityEditText = view.findViewById(R.id.city_select);
        addressEditText = view.findViewById(R.id.activity_address);
        descriptionEditText = view.findViewById(R.id.activity_description);
        tagsEditText = view.findViewById(R.id.activity_tags);
        locateButton = view.findViewById(R.id.locate_button);
        submitButton = view.findViewById(R.id.add_activity);
        cancelButton = view.findViewById(R.id.cancel_activity);

        countryEditText.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Country.values()));
        countryEditText.setValidator(new AutoCompleteTextView.Validator()
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

        viewModel.getCitiesResult().observe(getViewLifecycleOwner(), new Observer<List<String>>()
        {
            @Override
            public void onChanged(List<String> strings)
            {
                cityEditText.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, strings));
            }
        });

        viewModel.getStoredActivity().observe(getViewLifecycleOwner(), activity ->
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

        viewModel.getMemoryFormStateFormState().observe(getViewLifecycleOwner(), createMemoryFormState ->
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

        locateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                String title = titleEditText.getText().toString();
//                Country country = Country.lookupByLabel(countryEditText.getText().toString());
//                String address = addressEditText.getText().toString();
//                String description = descriptionEditText.getText().toString();
//                String tags = tagsEditText.getText().toString();
//
//                viewModel.storeActivity(title, country, address, description, tags);
//
//                //TODO: Create location getter
//                Navigation.findNavController(view).navigate(R.id.navigation_CreatePlan);

                startActivity(new Intent(getActivity(), LocationActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (countryEditText.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getContext(), "Please select a country", Toast.LENGTH_LONG).show();
                    return;
                }

                String title = titleEditText.getText().toString();
                Country country = Country.lookupByLabel(countryEditText.getText().toString());
                String city = cityEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String tags = tagsEditText.getText().toString();

                viewModel.addMemory(title, country, address + ", " + city, description, tags);

                Navigation.findNavController(view).navigate(R.id.navigation_CreatePlan);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewModel.clear();

                Navigation.findNavController(view).navigate(R.id.navigation_CreatePlan);
            }
        });

        return view;
    }
}