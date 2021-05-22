package com.example.demoapp.ui.main.map;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel
{
    private final ContentRepository repository;

    private final LiveData<List<Activity>> activitiesLiveData;

    private final LiveData<List<Activity>> nearActivitiesLiveData;

    public MapViewModel(ContentRepository repository)
    {
        this.repository = repository;

        activitiesLiveData = Transformations.map(repository.getActivitiesResult(), new Function<RepositoryResponse<List<Activity>>, List<Activity>>()
        {
            @Override
            public List<Activity> apply(RepositoryResponse<List<Activity>> input)
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
        nearActivitiesLiveData = Transformations.map(repository.getNearActivitiesResult(), new Function<RepositoryResponse<List<Activity>>, List<Activity>>()
        {
            @Override
            public List<Activity> apply(RepositoryResponse<List<Activity>> input)
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

    public void getActivities(int postID)
    {
        repository.getPostActivities(postID);
    }

    public void getActivities(double latitude, double longtitude, double radius)
    {
        repository.searchNearActivities(latitude, longtitude, radius);
    }

    public LiveData<List<Activity>> getActivitiesLiveData()
    {
        return activitiesLiveData;
    }

    public LiveData<List<Activity>> getNearActivitiesLiveData()
    {
        return nearActivitiesLiveData;
    }
}
