package com.example.demoapp.ui.main.dashboard;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.Event;
import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.data.model.Place;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DashboardViewModel extends ViewModel
{
    private final LiveData<Event<List<Item>>> searchResult;
    private final LiveData<List<String>> citiesResult;
    private final LiveData<Event<Place>> locationInfo;
    private final ContentRepository repository;
    private final NotificationHub hub;

    public DashboardViewModel(ContentRepository repository, NotificationHub hub)
    {
        this.repository = repository;
        this.hub = hub;

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

    public void search(String query, String country, String city, String type, int radius, double latitude, double longitude)
    {
        repository.search(query, country, city, type, radius, latitude, longitude);
    }

    public LiveData<Event<List<Item>>> getSearchResult()
    {
        return searchResult;
    }

    public void sendFollowRequest(String userID)
    {
        hub.sendFollowRequest(userID);
    }

    public void cancelFollowRequest(String userID)
    {
        hub.cancelFollowRequest(userID);
    }

    public void unfollow(String userID)
    {
        hub.unfollow(userID);
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

    public void sendLike(int postID)
    {
        hub.sendLike(postID);
    }

    public void sendComment(int postID, String comment)
    {
        hub.sendComment(postID, comment);
    }

    public LiveData<Event<Place>> getLocationInfo()
    {
        return locationInfo;
    }
}