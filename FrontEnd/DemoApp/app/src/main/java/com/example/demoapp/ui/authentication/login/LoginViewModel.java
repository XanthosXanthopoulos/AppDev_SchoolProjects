package com.example.demoapp.ui.authentication.login;

import androidx.annotation.WorkerThread;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.repository.UserRepository;
import com.example.demoapp.R;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;
import com.example.demoapp.ui.authentication.login.LoginFormState;

public class LoginViewModel extends ViewModel
{

    private final MutableLiveData<LoginFormState> loginFormState;
    private final LiveData<AuthenticationResult> loginResult;
    private final UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository)
    {
        this.userRepository = userRepository;
        loginFormState = new MutableLiveData<>();

        loginResult = Transformations.map(userRepository.getResult(), new Function<RepositoryResponse<User>, AuthenticationResult>()
        {
            @WorkerThread
            @Override
            public AuthenticationResult apply(RepositoryResponse<User> input)
            {
                if (input.isSuccessful())
                {
                    return new AuthenticationResult(new AuthenticatedUserView(input.getResponse().getJwToken()));
                }
                else
                {
                    return new AuthenticationResult(input.getErrorMessage());
                }
            }
        });
    }

    LiveData<LoginFormState> getLoginFormState()
    {
        return loginFormState;
    }

    LiveData<AuthenticationResult> getLoginResult()
    {
        return loginResult;
    }

    public void login(String username, String password)
    {
        userRepository.login(username, password);
    }

    public void loginDataChanged(String username, String password)
    {
        if (!isUserNameValid(username))
        {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        }
        else if (!isPasswordValid(password))
        {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
        else
        {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username)
    {
        if (username == null)
        {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(username).matches();

    }

    private boolean isPasswordValid(String password)
    {
        return password != null && password.trim().length() > 5;
    }
}