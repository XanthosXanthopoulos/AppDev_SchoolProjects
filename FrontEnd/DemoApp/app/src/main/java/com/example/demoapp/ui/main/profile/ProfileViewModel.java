package com.example.demoapp.ui.main.profile;

import androidx.annotation.WorkerThread;
import androidx.arch.core.util.Function;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.R;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.UserRepository;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;

public class ProfileViewModel extends ViewModel
{
    private UserRepository userRepository;

    public ProfileViewModel(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public String getProfileImage()
    {
        return userRepository.getUser().getProfileImageID();
    }
}