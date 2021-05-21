package com.example.demoapp.ui.main.plan.memory.create;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.List;

public class CreateMemoryViewModel extends ViewModel
{
    private ContentRepository repository;
    private final LiveData<List<String>> citiesResult;

    public CreateMemoryViewModel(ContentRepository repository)
    {
        this.repository = repository;

        citiesResult = Transformations.map(repository.getCitiesResult(), new Function<RepositoryResponse<List<String>>, List<String>>()
        {
            @Override
            public List<String> apply(RepositoryResponse<List<String>> input)
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

    public void addMemory(String title, Country country, String address, String description, String tags)
    {
        repository.addActivity(new Activity("", title, description, tags, country, address));
    }

    public void storeActivity(String title, Country country, String address, String description, String tags)
    {
        repository.storeActivity(new Activity("", title, description, tags, country, address));
    }

    public LiveData<Activity> getStoredActivity()
    {
        return repository.getCurrentActivity();
    }

    public void clear()
    {
        repository.storeActivity(null);
    }

    public LiveData<List<String>> getCitiesResult()
    {
        return citiesResult;
    }

    public void getCities(String country)
    {
        repository.getCities(country);
    }
}