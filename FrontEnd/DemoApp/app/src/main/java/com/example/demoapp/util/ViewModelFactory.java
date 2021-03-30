package com.example.demoapp.util;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.repository.UserRepository;
import com.example.demoapp.ui.login.LoginViewModel;
import com.example.demoapp.ui.register.RegisterViewModel;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class ViewModelFactory implements ViewModelProvider.Factory
{

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(LoginViewModel.class))
        {
            return (T) new LoginViewModel(UserRepository.getInstance(new ApiDataSource()));
        }
        else if(modelClass.isAssignableFrom(RegisterViewModel.class))
        {
            return (T) new RegisterViewModel(UserRepository.getInstance(new ApiDataSource()));
        }
        else
        {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}