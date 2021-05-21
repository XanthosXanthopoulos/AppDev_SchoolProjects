package com.example.demoapp.ui.main.follower;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FollowerViewModel extends ViewModel
{
    private UserRepository repository;
    private NotificationHub hub;
    private final LiveData<List<Follow>> followersList;

    public FollowerViewModel(UserRepository repository, NotificationHub hub)
    {
        this.repository = repository;
        this.hub = hub;

        followersList = Transformations.map(repository.getFollowResult(), new Function<RepositoryResponse<List<Follow>>, List<Follow>>()
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

    public LiveData<List<Follow>> getFollowersList()
    {
        return followersList;
    }
}