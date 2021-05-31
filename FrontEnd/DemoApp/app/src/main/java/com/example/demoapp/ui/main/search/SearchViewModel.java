package com.example.demoapp.ui.main.search;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.Event;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Place;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchViewModel extends ViewModel
{
    private final LiveData<Event<List<Item>>> searchResult;
    private final LiveData<List<String>> citiesResult;
    private final LiveData<Event<Place>> locationInfo;
    private final ContentRepository repository;

    public SearchViewModel(ContentRepository repository)
    {
        this.repository = repository;

        searchResult = Transformations.map(repository.getSearchResult(), input ->
        {
            if (input.isSuccessful())
            {
                return input.getResponse();
            }
            else
            {
                Event<List<Item>> event = new Event<>(new LinkedList<>());
                event.setHandled(true);
                return event;
            }
        });

        citiesResult = Transformations.map(repository.getCitiesResult(), new Function<RepositoryResponse<List<String>>, List<String>>()
        {
            @Override
            public List<String> apply(RepositoryResponse<List<String>> input)
            {
                if (input.isSuccessful())
                {
                    return input.getResponse();
                }
                else
                {
                    return new ArrayList<>();
                }
            }
        });

        locationInfo = Transformations.map(repository.getPlaceResult(), new Function<RepositoryResponse<Event<Place>>, Event<Place>>()
        {
            @Override
            public Event<Place> apply(RepositoryResponse<Event<Place>> input)
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
            }
        });
    }

    public void search(String query, String country, String city, String type, int radius, double latitude, double longitude)
    {
        repository.search(query, country, city, type, radius, latitude, longitude);
    }

    public LiveData<Event<List<Item>>> getSearchResult()
    {
        return searchResult;
    }

    public LiveData<List<String>> getCitiesResult()
    {
        return citiesResult;
    }

    public void getCities(String country)
    {
        repository.getCities(country);
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
