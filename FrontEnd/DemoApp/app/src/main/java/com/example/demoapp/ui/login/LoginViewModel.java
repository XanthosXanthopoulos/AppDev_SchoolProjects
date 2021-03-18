package com.example.demoapp.ui.login;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.demoapp.data.UserRepository;
import com.example.demoapp.data.Result;
import com.example.demoapp.data.model.LoggedInUser;
import com.example.demoapp.R;

public class LoginViewModel extends ViewModel
{

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LiveData<LoginResult> loginResult;
    private UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository)
    {
        this.userRepository = userRepository;
        loginResult = Transformations.map(userRepository.getResult(), new Function<Result<LoggedInUser>, LoginResult>()
        {
            @Override
            public LoginResult apply(Result<LoggedInUser> input)
            {
                if (input instanceof Result.Success)
                {
                    LoggedInUser data = ((Result.Success<LoggedInUser>) input).getData();
                    return new LoginResult(new LoggedInUserView(data.getUsername()));
                }
                else
                {
                    return new LoginResult(R.string.login_failed);
                }
            }
        });
    }

    LiveData<LoginFormState> getLoginFormState()
    {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult()
    {
        return loginResult;
    }

    public void login(String username, String password)
    {
        // can be launched in a separate asynchronous job
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