package com.example.demoapp.ui.main.plan.memory;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
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

        activitiesLiveData = Transformations.map(repository.getCurrentPost(), post ->
        {
            if (post == null)
            {
                return new LinkedList<>();
            }
            else
            {
                return post.getActivities();
            }
        });
    }

    public LiveData<Iterable<Activity>> getActivitiesLiveData()
    {
        return activitiesLiveData;
    }

    public void addMemory(String id, String title, Country country, String city, String address, String description, String tags)
    {
        repository.addActivity(new Activity(id, title, description, tags, country, city, address));
    }

    public void addMemory(String id, String title, Country country, String city, String address, String description, String tags, double latitude, double longitude)
    {
        repository.addActivity(new Activity(id, title, description, tags, country, city, address, latitude, longitude));
    }

    public void removeMemory(int index)
    {
        repository.removeActivity(index);
    }
}