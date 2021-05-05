package com.example.demoapp.data.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.toolbox.StringRequest;
import com.example.demoapp.App;
import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.hub.NotificationHub;
import com.example.demoapp.data.model.AccountType;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.FragmentContent;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.api.request.RegisterCredentialsModel;
import com.example.demoapp.data.model.api.request.SingInCredentialsModel;
import com.example.demoapp.data.model.api.response.ProfileInfoResponseModel;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.data.model.repository.RepositoryResponse;

import java.util.Date;
import java.util.List;

import static com.example.demoapp.App.SHARED_PREFS;

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
    private final MediatorLiveData<RepositoryResponse<List<Follow>>> followResult;

    private UserRepository(ApiDataSource dataSource, NotificationHub hub)
    {
        this.dataSource = dataSource;

        result = new MediatorLiveData<>();
        followResult = new MediatorLiveData<>();
    }

    public static UserRepository getInstance(ApiDataSource dataSource, NotificationHub hub)
    {
        if (instance == null)
        {
            instance = new UserRepository(dataSource, hub);
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
        //dataSource.logout();
    }

    private void setUser(User user)
    {
        this.user = user;
    }

    private User getUser() { return user; }

    public void login(String username, String password)
    {
        LiveData<DataSourceResponse<User>> dataSourceResult = dataSource.login(new SingInCredentialsModel(username, password));
        result.addSource(dataSourceResult, new Observer<DataSourceResponse<User>>()
        {
            @Override
            public void onChanged(@Nullable DataSourceResponse<User> user)
            {
                if (user.isSuccessful())
                {
                    setUser(user.getResponse());
                    saveToPrefs("JWToken" ,user.getResponse().getJwToken());
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
        LiveData<DataSourceResponse<User>> dataSourceResult = dataSource.getProfileInfo(user.getJwToken());
        result.addSource(dataSourceResult, new Observer<DataSourceResponse<User>>()
        {
            @Override
            public void onChanged(@Nullable DataSourceResponse<User> user)
            {
                if (user.isSuccessful())
                {
                    getUser().setName(user.getResponse().getName());
                    getUser().setSurname(user.getResponse().getSurname());
                    getUser().setDescription(user.getResponse().getDescription());

                    result.setValue(new RepositoryResponse<>(getUser()));
                }
                else
                {
                    result.setValue(new RepositoryResponse<>(user.getErrorMessage()));
                }

                result.removeSource(dataSourceResult);
            }
        });
    }

    public void updateProfile(String username, String email, String name, String surname, String description, Date birthday, Country country, AccountType accountType)
    {
        String birthdayString = birthday.toString();

        LiveData<DataSourceResponse<Boolean>> dataSourceResult = dataSource.updateProfile(new ProfileInfoResponseModel(username, email, name, surname, description, birthdayString, country, accountType), user.getJwToken());
        result.addSource(dataSourceResult, new Observer<DataSourceResponse<Boolean>>()
        {
            @Override
            public void onChanged(@Nullable DataSourceResponse<Boolean> user)
            {
                if (user.isSuccessful())
                {
                    getUser().setName(name);
                    getUser().setSurname(surname);
                    getUser().setDescription(description);

                    result.setValue(new RepositoryResponse<>(getUser()));
                }
                else
                {
                    result.setValue(new RepositoryResponse<>(user.getErrorMessage()));
                }

                result.removeSource(dataSourceResult);
            }
        });
    }

    public void getFollows(FragmentContent content)
    {
        LiveData<DataSourceResponse<List<Follow>>> result;
    }

//    public void updateActivityList(String title, String type, String place, String description){
//        LiveData<DataSourceResponse<Boolean>> dataSourceResult = dataSource.updateProfile(new ProfileInfoResponseModel(title, type, place, description), user.getJwToken());
//        result.addSource(dataSourceResult, new Observer<DataSourceResponse<Boolean>>()
//        {
//            @Override
//            public void onChanged(@Nullable DataSourceResponse<Boolean> user)
//            {
//                if (user.isSuccessful())
//                {
//                    getUser().setName(name);
//                    getUser().setSurname(surname);
//                    getUser().setDescription(description);
//
//                    result.setValue(new RepositoryResponse<>(getUser()));
//                }
//                else
//                {
//                    result.setValue(new RepositoryResponse<>(user.getErrorMessage()));
//                }
//
//                result.removeSource(dataSourceResult);
//            }
//        });
//    }

    public LiveData<RepositoryResponse<User>> getResult()
    {
        return result;
    }

    public MediatorLiveData<RepositoryResponse<List<Follow>>> getFollowResult()
    {
        return followResult;
    }
}