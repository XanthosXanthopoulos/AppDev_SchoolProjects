package com.example.demoapp.ui.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.repository.ContentRepository;

public class HomeViewModel extends ViewModel
{
    private ContentRepository repository;

    public HomeViewModel(ContentRepository repository)
    {
        this.repository = repository;
    }
}