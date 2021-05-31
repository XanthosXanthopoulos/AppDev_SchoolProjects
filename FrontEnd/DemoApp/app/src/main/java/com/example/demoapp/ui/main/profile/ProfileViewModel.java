package com.example.demoapp.ui.main.profile;

import androidx.annotation.WorkerThread;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.R;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;
import com.example.demoapp.data.repository.UserRepository;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProfileViewModel extends ViewModel
{
    private UserRepository userRepository;
    private ContentRepository repository;

    private final LiveData<Event<List<Item>>> feedResult;

    public ProfileViewModel(UserRepository userRepository, ContentRepository repository)
    {
        this.userRepository = userRepository;
        this.repository = repository;

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

    public String getProfileImage()
    {
        return userRepository.getUser().getProfileImageID();
    }

    public void deletePost(int postID)
    {
        repository.deletePost(postID);
    }

    public void getFeed()
    {
        repository.updateFeed(true);
    }

    public LiveData<Event<List<Item>>> getFeedResult()
    {
        return feedResult;
    }
}