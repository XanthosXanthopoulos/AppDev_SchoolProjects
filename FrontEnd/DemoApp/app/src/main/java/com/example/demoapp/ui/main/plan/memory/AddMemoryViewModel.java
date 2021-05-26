package com.example.demoapp.ui.main.plan.memory;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddMemoryViewModel extends ViewModel
{
    private ContentRepository repository;

    private final LiveData<Iterable<Activity>> activitiesLiveData;

    public AddMemoryViewModel(ContentRepository repository)
    {
        this.repository = repository;

        activitiesLiveData = Transformations.map(repository.getCurrentPost(), new Function<Post, Iterable<Activity>>()
        {
            @Override
            public Iterable<Activity> apply(Post post)
            {
                if (post == null)
                {
                    return new LinkedList<>();
                }
                else
                {
                    return post.getActivities();
                }
            }
        });
    }

    public LiveData<Iterable<Activity>> getActivitiesLiveData()
    {
        return activitiesLiveData;
    }
}