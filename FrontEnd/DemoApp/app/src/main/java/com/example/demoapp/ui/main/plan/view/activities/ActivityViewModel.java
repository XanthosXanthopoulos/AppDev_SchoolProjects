package com.example.demoapp.ui.main.plan.view.activities;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.LinkedList;

public class ActivityViewModel extends ViewModel
{
    private final ContentRepository repository;
    private final LiveData<Iterable<Activity>> activitiesResult;

    public ActivityViewModel(ContentRepository repository)
    {
        this.repository = repository;

        activitiesResult = Transformations.map(repository.getPostResult(), input ->
        {
            if (input.isSuccessful())
            {
                return input.getResponse().getActivities();
            }
            else
            {
                return new LinkedList<>();
            }
        });
    }

    public LiveData<Iterable<Activity>> getActivitiesResult()
    {
        return activitiesResult;
    }
}