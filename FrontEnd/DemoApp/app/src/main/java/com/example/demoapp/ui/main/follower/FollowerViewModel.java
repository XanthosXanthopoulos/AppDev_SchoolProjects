package com.example.demoapp.ui.main.follower;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.Event;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.repository.UserRepository;

import java.util.LinkedList;
import java.util.List;

public class FollowerViewModel extends ViewModel
{
    private final UserRepository repository;
    private final NotificationHub hub;
    private final LiveData<Event<List<Follow>>> followersList;

    public FollowerViewModel(UserRepository repository, NotificationHub hub)
    {
        this.repository = repository;
        this.hub = hub;

        followersList = Transformations.map(repository.getFollowResult(), input ->
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

    public void getFollowers()
    {
        repository.getFollowers();
    }

    public void declineFollowRequest(String userID)
    {
        hub.declineFollowRequest(userID);
    }

    public void acceptFollowRequest(String userID)
    {
        hub.acceptFollowRequest(userID);
    }

    public void removeFollower(String userID)
    {
        hub.removeFollower(userID);
    }

    public LiveData<Event<List<Follow>>> getFollowersList()
    {
        return followersList;
    }
}