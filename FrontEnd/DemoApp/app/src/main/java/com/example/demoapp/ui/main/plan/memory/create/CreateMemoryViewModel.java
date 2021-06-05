package com.example.demoapp.ui.main.plan.memory.create;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.R;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Place;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.List;

public class CreateMemoryViewModel extends ViewModel
{
    private final ContentRepository repository;
    private final LiveData<List<String>> citiesResult;
    private final LiveData<Event<Place>> locationInfo;
    private final MutableLiveData<CreateMemoryFormState> memoryFormState;

    public CreateMemoryViewModel(ContentRepository repository)
    {
        this.repository = repository;

        memoryFormState = new MutableLiveData<>();

        citiesResult = Transformations.map(repository.getCitiesResult(), input ->
        {
            if (input.isSuccessful())
            {
                return input.getResponse();
            }
            else
            {
                return new ArrayList<>();
            }
        });

        locationInfo = Transformations.map(repository.getPlaceResult(), input ->
        {
            if (input.isSuccessful())
            {
                return input.getResponse();
            }
            else
            {
                Event<Place> event = new Event<>(new Place());
                event.setHandled(true);
                return event;
            }
        });
    }

    public LiveData<Activity> getStoredActivity()
    {
        return repository.getCurrentActivity();
    }

    public void clear()
    {
        repository.storeActivity(null);
    }

    public LiveData<List<String>> getCitiesResult()
    {
        return citiesResult;
    }

    public void getCities(String country)
    {
        repository.getCities(country);
    }

    public LiveData<CreateMemoryFormState> getMemoryFormStateFormState()
    {
        return memoryFormState;
    }

    public void memoryDataChanged(String title, Country country)
    {
        if (!isTitleValid(title))
        {
            memoryFormState.setValue(new CreateMemoryFormState(R.string.name_error, null));
        }
        else if (!isCountryValid(country))
        {
            memoryFormState.setValue(new CreateMemoryFormState(null, R.string.country_error));
        }
        else
        {
            memoryFormState.setValue(new CreateMemoryFormState(true));
        }
    }

    private boolean isTitleValid(String name)
    {
        if (name == null)
        {
            return false;
        }

        return !name.isEmpty();
    }

    private boolean isCountryValid(Country country)
    {
        if (country == null) return false;

        return country.code != 0;
    }

    public void getLocationInfo(double latitude, double longitude)
    {
        repository.getLocationInfo(latitude, longitude);
    }

    public LiveData<Event<Place>> getLocationInfo()
    {
        return locationInfo;
    }
}