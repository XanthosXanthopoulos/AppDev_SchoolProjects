package com.example.demoapp.ui.main.plan.memory.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.List;

public class CreateMemoryViewModel extends ViewModel
{
    private ContentRepository repository;

    public CreateMemoryViewModel(ContentRepository repository)
    {
        this.repository = repository;
    }

    public void addMemory(String title, Country country, String address, String description, String tags)
    {
        Activity ac = new Activity("", title, description, tags, country, address);
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
}