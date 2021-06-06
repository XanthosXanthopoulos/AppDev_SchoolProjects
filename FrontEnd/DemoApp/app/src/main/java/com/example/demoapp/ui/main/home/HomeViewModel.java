package com.example.demoapp.ui.main.home;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.Event;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;
import com.example.demoapp.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HomeViewModel extends ViewModel
{
    private UserRepository userRepository;
    private ContentRepository repository;
    private NotificationHub hub;
    private final LiveData<Event<List<Item>>> feedResult;

    public HomeViewModel(UserRepository userRepository, ContentRepository repository, NotificationHub hub)
    {
        this.userRepository = userRepository;
        this.repository = repository;
        this.hub = hub;

        feedResult = Transformations.map(repository.getFeedResult(), input ->
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
    }

    public void getFeed()
    {
        repository.updateFeed(false);
    }

    public LiveData<Event<List<Item>>> getFeedResult()
    {
        return feedResult;
    }

    public LiveData<Event<Notification>> getNotification()
    {
        return hub.getNotificationsLiveData();
    }

    public LinkedList<Notification> getNotifications()
    {
        return hub.getNotifications();
    }

    public void sendLike(int postID)
    {
        hub.sendLike(postID);
    }

    public void sendComment(int postID, String comment)
    {
        hub.sendComment(postID, comment);
    }

    public void logout()
    {
        hub.logout();
        userRepository.logout();
    }
}