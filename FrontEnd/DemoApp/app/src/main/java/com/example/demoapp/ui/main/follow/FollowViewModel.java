package com.example.demoapp.ui.main.follow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.repository.UserRepository;

import java.util.List;

public class FollowViewModel extends ViewModel
{
    private UserRepository repository;
    private LiveData<List<Follow>> followList;

    public FollowViewModel(UserRepository repository)
    {
        this.repository = repository;
    }

    public void sendFollowRequest()
    {

    }

    public void acceptFollowRequest()
    {

    }

    public void removeFollowRequest(String userID)
    {

    }
}