package com.example.demoapp.ui.main.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Place;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel
{
    private final ContentRepository repository;

    private final LiveData<List<Activity>> activitiesLiveData;
    private final LiveData<List<Activity>> nearActivitiesLiveData;
    private final LiveData<Event<Place>> searchResult;

    public MapViewModel(ContentRepository repository)
    {
        this.repository = repository;

        activitiesLiveData = Transformations.map(repository.getActivitiesResult(), input ->
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

        nearActivitiesLiveData = Transformations.map(repository.getNearActivitiesResult(), input ->
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

        searchResult = Transformations.map(repository.getPlaceResult(), input ->
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

    public void getActivities(int postID)
    {
        repository.getPostActivities(postID);
    }

    public void getActivities(double latitude, double longitude, double radius)
    {
        repository.searchNearActivities(latitude, longitude, radius);
    }

    public LiveData<List<Activity>> getActivitiesLiveData()
    {
        return activitiesLiveData;
    }

    public LiveData<List<Activity>> getNearActivitiesLiveData()
    {
        return nearActivitiesLiveData;
    }

    public void searchPlace(String query)
    {
        repository.getLocationInfo(query);
    }

    public LiveData<Event<Place>> getSearchResult()
    {
        return searchResult;
    }
}
