package com.example.demoapp.ui.main.dashboard;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel
{

    private final LiveData<List<Item>> searchResult;
    private final LiveData<List<String>> citiesResult;
    private final ContentRepository repository;
    private final NotificationHub hub;

    public DashboardViewModel(ContentRepository repository, NotificationHub hub)
    {
        this.repository = repository;
        this.hub = hub;

        searchResult = Transformations.map(repository.getSearchResult(), new Function<RepositoryResponse<List<Item>>, List<Item>>()
        {
            @Override
            public List<Item> apply(RepositoryResponse<List<Item>> input)
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
    }

    public void search(String query, String country, String city, String type, int radius)
    {
        repository.search(query, country, city, type, radius);
    }

    public LiveData<List<Item>> getSearchResult()
    {
        return searchResult;
    }

    public void sendFollowRequest(String userID)
    {
        hub.sendFollowRequest(userID);
    }

    public void cancelFollowRequest(String userID)
    {

    }

    public void unfollow(String userID)
    {

    }

    public LiveData<List<String>> getCitiesResult()
    {
        return citiesResult;
    }

    public void getCities(String country)
    {
        repository.getCities(country);
    }
}