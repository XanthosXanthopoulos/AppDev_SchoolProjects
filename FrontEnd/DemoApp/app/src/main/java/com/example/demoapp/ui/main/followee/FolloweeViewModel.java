package com.example.demoapp.ui.main.followee;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FolloweeViewModel extends ViewModel
{
    private UserRepository repository;
    private final LiveData<List<Follow>> followList;
    private NotificationHub hub;

    public FolloweeViewModel(UserRepository repository, NotificationHub hub)
    {
        this.repository = repository;
        this.hub = hub;

        followList = Transformations.map(repository.getFollowResult(), new Function<RepositoryResponse<List<Follow>>, List<Follow>>()
        {
            @Override
            public List<Follow> apply(RepositoryResponse<List<Follow>> input)
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

    public LiveData<List<Follow>> getFollowList()
    {
        return followList;
    }
}