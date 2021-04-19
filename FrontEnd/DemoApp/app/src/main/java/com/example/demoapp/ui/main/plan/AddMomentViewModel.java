package com.example.demoapp.ui.main.plan;

import androidx.lifecycle.ViewModel;
import com.example.demoapp.data.repository.UserRepository;

public class AddMomentViewModel extends ViewModel{

    private UserRepository userRepository;

    // TODO: Implement the ViewModel
    public AddMomentViewModel(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

}
