package com.example.demoapp.ui.main.account;

import androidx.annotation.WorkerThread;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.R;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.UserRepository;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;

public class AccountViewModel extends ViewModel
{
    private final UserRepository userRepository;

    private final LiveData<AuthenticationResult> profile;

    public AccountViewModel(UserRepository userRepository)
    {
        this.userRepository = userRepository;

        profile = Transformations.map(userRepository.getResult(), new Function<RepositoryResponse<User>, AuthenticationResult>()
        {
            @WorkerThread
            @Override
            public AuthenticationResult apply(RepositoryResponse<User> input)
            {
                if (input.isSuccessful())
                {
                    return new AuthenticationResult(new AuthenticatedUserView(input.getResponse().getUsername()));
                }
                else
                {
                    return new AuthenticationResult(R.string.login_failed);
                }
            }
        });
    }
}