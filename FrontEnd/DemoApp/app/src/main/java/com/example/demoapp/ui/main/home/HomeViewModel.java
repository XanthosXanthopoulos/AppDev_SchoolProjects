package com.example.demoapp.ui.main.home;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel
{
    private ContentRepository repository;
    private NotificationHub hub;
    private final LiveData<List<Item>> feedResult;

    public HomeViewModel(ContentRepository repository, NotificationHub hub)
    {
        this.repository = repository;
        this.hub = hub;

        feedResult = Transformations.map(repository.getFeedResult(), new Function<RepositoryResponse<List<Item>>, List<Item>>()
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
    }

    public void getFeed()
    {
        repository.updateFeed();
    }

    public LiveData<List<Item>> getFeedResult()
    {
        return feedResult;
    }

    public LiveData<List<Notification>> getNotifications()
    {
        return hub.getNotificationsLiveData();
    }
}