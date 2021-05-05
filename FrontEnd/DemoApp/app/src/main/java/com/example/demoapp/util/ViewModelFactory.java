package com.example.demoapp.util;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.repository.ContentRepository;
import com.example.demoapp.data.repository.UserRepository;
import com.example.demoapp.ui.authentication.login.LoginViewModel;
import com.example.demoapp.ui.main.account.AccountViewModel;
import com.example.demoapp.ui.main.follow.FollowViewModel;
import com.example.demoapp.ui.main.home.HomeViewModel;
import com.example.demoapp.ui.main.map.MapViewModel;
import com.example.demoapp.ui.main.plan.moment.AddMomentViewModel;
import com.example.demoapp.ui.main.profile.ProfileViewModel;
import com.example.demoapp.ui.authentication.register.RegisterViewModel;
import com.example.demoapp.ui.main.dashboard.DashboardViewModel;

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
            return (T) new LoginViewModel(UserRepository.getInstance(new ApiDataSource(), NotificationHub.getInstance()));
        }
        else if(modelClass.isAssignableFrom(RegisterViewModel.class))
        {
            return (T) new RegisterViewModel(UserRepository.getInstance(new ApiDataSource(), NotificationHub.getInstance()));
        }
        else if(modelClass.isAssignableFrom(MapViewModel.class))
        {
            return (T) new MapViewModel();
        }
        else if(modelClass.isAssignableFrom(ProfileViewModel.class))
        {
            return (T) new ProfileViewModel(UserRepository.getInstance(new ApiDataSource(), NotificationHub.getInstance()));
        }
        else if(modelClass.isAssignableFrom(AccountViewModel.class))
        {
            return (T) new AccountViewModel(UserRepository.getInstance(new ApiDataSource(), NotificationHub.getInstance()));
        }
        else if(modelClass.isAssignableFrom(DashboardViewModel.class))
        {
            return (T) new DashboardViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if(modelClass.isAssignableFrom(HomeViewModel.class))
        {
            return (T) new HomeViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if(modelClass.isAssignableFrom(AddMomentViewModel.class))
        {
            return (T) new AddMomentViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if (modelClass.isAssignableFrom(FollowViewModel.class))
        {
            return (T) new FollowViewModel(UserRepository.getInstance(new ApiDataSource(), NotificationHub.getInstance()));
        }
        else
        {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}