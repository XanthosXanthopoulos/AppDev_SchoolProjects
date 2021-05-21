package com.example.demoapp.ui.main.map;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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
    }

    public void getActivities(int postID)
    {
        repository.getPostActivities(postID);
    }

    public LiveData<List<Activity>> getActivitiesLiveData()
    {
        return activitiesLiveData;
    }
}
