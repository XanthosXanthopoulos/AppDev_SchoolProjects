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
import com.example.demoapp.ui.main.followee.FolloweeViewModel;
import com.example.demoapp.ui.main.follower.FollowerViewModel;
import com.example.demoapp.ui.main.home.HomeViewModel;
import com.example.demoapp.ui.main.map.MapViewModel;
import com.example.demoapp.ui.main.plan.CreatePlanViewModel;
import com.example.demoapp.ui.main.plan.view.ViewPlanViewModel;
import com.example.demoapp.ui.main.plan.memory.AddMemoryViewModel;
import com.example.demoapp.ui.main.plan.memory.create.CreateMemoryViewModel;
import com.example.demoapp.ui.main.plan.moment.AddMomentViewModel;
import com.example.demoapp.ui.main.plan.view.activities.ActivityViewModel;
import com.example.demoapp.ui.main.plan.view.comments.CommentViewModel;
import com.example.demoapp.ui.main.profile.ProfileViewModel;
import com.example.demoapp.ui.authentication.register.RegisterViewModel;
import com.example.demoapp.ui.main.dashboard.DashboardViewModel;
import com.example.demoapp.ui.main.search.SearchViewModel;

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
        else if(modelClass.isAssignableFrom(MapViewModel.class))
        {
            return (T) new MapViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if(modelClass.isAssignableFrom(ProfileViewModel.class))
        {
            return (T) new ProfileViewModel(UserRepository.getInstance(new ApiDataSource()), ContentRepository.getInstance(new ApiDataSource()));
        }
        else if(modelClass.isAssignableFrom(AccountViewModel.class))
        {
            return (T) new AccountViewModel(UserRepository.getInstance(new ApiDataSource()));
        }
        else if(modelClass.isAssignableFrom(DashboardViewModel.class))
        {
            return (T) new DashboardViewModel(ContentRepository.getInstance(new ApiDataSource()), NotificationHub.getInstance());
        }
        else if(modelClass.isAssignableFrom(HomeViewModel.class))
        {
            return (T) new HomeViewModel(UserRepository.getInstance(new ApiDataSource()), ContentRepository.getInstance(new ApiDataSource()), NotificationHub.getInstance());
        }
        else if(modelClass.isAssignableFrom(AddMomentViewModel.class))
        {
            return (T) new AddMomentViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if (modelClass.isAssignableFrom(FolloweeViewModel.class))
        {
            return (T) new FolloweeViewModel(UserRepository.getInstance(new ApiDataSource()), NotificationHub.getInstance());
        }
        else if (modelClass.isAssignableFrom(FollowerViewModel.class))
        {
            return (T) new FollowerViewModel(UserRepository.getInstance(new ApiDataSource()), NotificationHub.getInstance());
        }
        else if (modelClass.isAssignableFrom(CreateMemoryViewModel.class))
        {
            return (T) new CreateMemoryViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if (modelClass.isAssignableFrom(AddMemoryViewModel.class))
        {
            return (T) new AddMemoryViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if (modelClass.isAssignableFrom(CreatePlanViewModel.class))
        {
            return (T) new CreatePlanViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if (modelClass.isAssignableFrom(ViewPlanViewModel.class))
        {
            return (T) new ViewPlanViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if (modelClass.isAssignableFrom(SearchViewModel.class))
        {
            return (T) new SearchViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if (modelClass.isAssignableFrom(ActivityViewModel.class))
        {
            return (T) new ActivityViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else if (modelClass.isAssignableFrom(CommentViewModel.class))
        {
            return (T) new CommentViewModel(ContentRepository.getInstance(new ApiDataSource()));
        }
        else
        {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}