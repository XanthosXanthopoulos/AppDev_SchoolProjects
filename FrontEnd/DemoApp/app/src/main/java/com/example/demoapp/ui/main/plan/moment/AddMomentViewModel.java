package com.example.demoapp.ui.main.plan.moment;

import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.repository.ContentRepository;
import com.example.demoapp.data.repository.UserRepository;

public class AddMomentViewModel extends ViewModel{

    private ContentRepository repository;

    // TODO: Implement the ViewModel
    public AddMomentViewModel(ContentRepository repository)
    {
        this.repository = repository;
    }

}
