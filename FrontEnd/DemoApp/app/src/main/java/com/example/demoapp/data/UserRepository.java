package com.example.demoapp.data;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.demoapp.data.model.LoggedInUser;
import com.example.demoapp.data.model.api.ApiResponse;
import com.example.demoapp.data.model.api.AuthenticationResponseModel;

import java.nio.file.FileAlreadyExistsException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository
{

    private static volatile UserRepository instance;
    private final UserDataSource dataSource;

    private LoggedInUser user = null;

    private LiveData<Result<LoggedInUser>> result;

    private UserRepository(UserDataSource dataSource)
    {
        this.dataSource = dataSource;

        result = Transformations.map(dataSource.getResult(), new Function<AuthenticationResponseModel, Result<LoggedInUser>>()
        {
            @Override
            public Result<LoggedInUser> apply(AuthenticationResponseModel input)
            {
                if (input != null)
                {
                    return new Result.Success<LoggedInUser>(new LoggedInUser(input.getUsername(), input.getJwToken()));
                }
                else
                {
                    return new Result.Error(new Exception());
                }
            }
        });
    }

    public static UserRepository getInstance(UserDataSource dataSource)
    {
        if (instance == null)
        {
            instance = new UserRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn()
    {
        return user != null;
    }

    public void logout()
    {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user)
    {
        this.user = user;
    }

    public void login(String username, String password)
    {
        dataSource.login(username, password);
    }

    public LiveData<Result<LoggedInUser>> getResult()
    {
        return result;
    }
}