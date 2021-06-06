package com.example.demoapp.data.repository;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.demoapp.data.Event;
import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.AccountType;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.api.request.RegisterCredentialsModel;
import com.example.demoapp.data.model.api.request.SingInCredentialsModel;
import com.example.demoapp.data.model.api.response.AuthenticationResponseModel;
import com.example.demoapp.data.model.api.response.ProfileInfoResponseModel;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.data.model.repository.RepositoryResponse;

import java.util.Date;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository extends Repository
{
    private static volatile UserRepository instance;
    private final ApiDataSource dataSource;

    private User user = null;

    private final MediatorLiveData<RepositoryResponse<User>> result;

    public MediatorLiveData<RepositoryResponse<Event<Boolean>>> getActionResult()
    {
        return actionResult;
    }

    private final MediatorLiveData<RepositoryResponse<Event<Boolean>>> actionResult;
    private final MediatorLiveData<RepositoryResponse<Event<List<Follow>>>> followResult;

    private UserRepository(ApiDataSource dataSource)
    {
        this.dataSource = dataSource;

        result = new MediatorLiveData<>();
        followResult = new MediatorLiveData<>();
        actionResult = new MediatorLiveData<>();
    }

    public static UserRepository getInstance(ApiDataSource dataSource)
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
        result.setValue(new RepositoryResponse<>("Logged out successfully"));
        dataSource.logout(loadFromPrefs("JWToken"));
    }

    private void setUser(User user)
    {
        this.user = user;
    }

    public void login(String username, String password)
    {
        LiveData<DataSourceResponse<User>> dataSourceResult = dataSource.login(new SingInCredentialsModel(username, password));
        result.addSource(dataSourceResult, user ->
        {
            if (user.isSuccessful())
            {
                setUser(user.getResponse());
                saveToPrefs("JWToken" ,user.getResponse().getJwToken());
                NotificationHub.init(user.getResponse().getJwToken());
                result.setValue(new RepositoryResponse<>(user.getResponse()));
            }
            else
            {
                result.setValue(new RepositoryResponse<>(user.getErrorMessage()));
            }

            result.removeSource(dataSourceResult);
        });
    }

    public void register(String username, String email, String password, String confirmPassword)
    {
        LiveData<DataSourceResponse<User>> dataSourceResult = dataSource.register(new RegisterCredentialsModel(username, email, password, confirmPassword));
        result.addSource(dataSourceResult, new Observer<DataSourceResponse<User>>()
        {
            @Override
            public void onChanged(@Nullable DataSourceResponse<User> user)
            {
                if (user.isSuccessful())
                {
                    setUser(user.getResponse());
                    saveToPrefs("JWToken" ,user.getResponse().getJwToken());
                    NotificationHub.init(user.getResponse().getJwToken());
                    result.setValue(new RepositoryResponse<>(user.getResponse()));
                }
                else
                {
                    result.setValue(new RepositoryResponse<>(user.getErrorMessage()));
                }

                result.removeSource(dataSourceResult);
            }
        });
    }

    public void getProfileInfo()
    {
        LiveData<DataSourceResponse<User>> dataSourceResult = dataSource.getProfileInfo(loadFromPrefs("JWToken"));
        result.addSource(dataSourceResult, new Observer<DataSourceResponse<User>>()
        {
            @Override
            public void onChanged(@Nullable DataSourceResponse<User> user)
            {
                if (user.isSuccessful())
                {
                    result.setValue(new RepositoryResponse<>(user.getResponse()));
                }
                else
                {
                    result.setValue(new RepositoryResponse<>(user.getErrorMessage()));
                }

                result.removeSource(dataSourceResult);
            }
        });
    }

    public void updateProfile(Uri profileImage, String name, String surname, String description, Date birthday, Country country, AccountType accountType)
    {
        LiveData<DataSourceResponse<AuthenticationResponseModel>> dataSourceResult = dataSource.updateProfile(new ProfileInfoResponseModel(profileImage, name, surname, description, birthday, country, accountType), loadFromPrefs("JWToken"));
        actionResult.addSource(dataSourceResult, user ->
        {
            if (user.isSuccessful())
            {
                getUser().setProfileImageID(user.getResponse().getProfileImageID());
                actionResult.setValue(new RepositoryResponse<>(new Event<>(true)));
            }
            else
            {
                actionResult.setValue(new RepositoryResponse<>(new Event<>(false)));
            }

            actionResult.removeSource(dataSourceResult);
        });
    }

    public void getFollows()
    {
        LiveData<DataSourceResponse<List<Follow>>> result = dataSource.getFollowees(loadFromPrefs("JWToken"));
        followResult.addSource(result, response ->
        {
            if (response.isSuccessful())
            {
                followResult.setValue(new RepositoryResponse<>(new Event<>(response.getResponse())));
            }
            else
            {
                followResult.setValue(new RepositoryResponse<>(response.getErrorMessage()));
            }

            followResult.removeSource(result);
        });
    }

    public void getFollowers()
    {
        LiveData<DataSourceResponse<List<Follow>>> result = dataSource.getFollowers(loadFromPrefs("JWToken"));
        followResult.addSource(result, response ->
        {
            if (response.isSuccessful())
            {
                followResult.setValue(new RepositoryResponse<>(new Event<>(response.getResponse())));
            }
            else
            {
                followResult.setValue(new RepositoryResponse<>(response.getErrorMessage()));
            }

            followResult.removeSource(result);
        });
    }

    public User getUser()
    {
        return user;
    }

    public LiveData<RepositoryResponse<User>> getResult()
    {
        return result;
    }

    public MediatorLiveData<RepositoryResponse<Event<List<Follow>>>> getFollowResult()
    {
        return followResult;
    }
}