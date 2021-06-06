package com.example.demoapp.ui.main.followee;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.Event;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.repository.UserRepository;

import java.util.LinkedList;
import java.util.List;

public class FolloweeViewModel extends ViewModel
{
    private final UserRepository repository;
    private final LiveData<Event<List<Follow>>> followList;
    private final NotificationHub hub;

    public FolloweeViewModel(UserRepository repository, NotificationHub hub)
    {
        this.repository = repository;
        this.hub = hub;

        followList = Transformations.map(repository.getFollowResult(), input ->
        {
            if (input.isSuccessful())
            {
                return input.getResponse();
            }
            else
            {
                Event<List<Follow>> event = new Event<>(new LinkedList<>());
                event.setHandled(true);
                return event;
            }
        });
    }

    public void getFollows()
    {
        repository.getFollows();
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

    public LiveData<Event<List<Follow>>> getFollowList()
    {
        return followList;
    }
}