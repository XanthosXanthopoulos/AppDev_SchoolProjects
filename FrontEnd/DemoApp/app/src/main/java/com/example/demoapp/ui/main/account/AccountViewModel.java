package com.example.demoapp.ui.main.account;

import android.net.Uri;

import androidx.annotation.WorkerThread;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.R;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.AccountType;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.UserRepository;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;

import java.util.Calendar;
import java.util.Date;

public class AccountViewModel extends ViewModel
{
    private final UserRepository userRepository;

    private final LiveData<AuthenticationResult> profileData;
    private final LiveData<Event<Boolean>> saveResult;
    private final MutableLiveData<AccountFormState> accountFormState;

    public AccountViewModel(UserRepository userRepository)
    {
        this.userRepository = userRepository;

        accountFormState = new MutableLiveData<>();

        profileData = Transformations.map(userRepository.getResult(), new Function<RepositoryResponse<User>, AuthenticationResult>()
        {
            @WorkerThread
            @Override
            public AuthenticationResult apply(RepositoryResponse<User> input)
            {
                if (input.getResponse() != null && input.getResponse().getName() != null)
                {
                    AuthenticatedUserView view = new AuthenticatedUserView("");
                    view.setProfileImageID(input.getResponse().getProfileImageID());
                    view.setName(input.getResponse().getName());
                    view.setCountry(input.getResponse().getCountry());
                    view.setAccountType(input.getResponse().getAccountType());
                    view.setDescription(input.getResponse().getDescription());
                    view.setSurname(input.getResponse().getSurname());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(input.getResponse().getBirthday());
                    view.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                    view.setMonth(calendar.get(Calendar.MONTH));
                    view.setYear(calendar.get(Calendar.YEAR));
                    return new AuthenticationResult(view);
                }
                else
                {
                    return new AuthenticationResult(input.getErrorMessage());
                }
            }
        });

        saveResult = Transformations.map(userRepository.getActionResult(), input -> input.getResponse());
    }

    public void loadUserProfile()
    {
        userRepository.getProfileInfo();
    }

    public void updateProfile(Uri profileImage, String name, String surname, String description, Date birthday, Country country, AccountType accountType)
    {
        userRepository.updateProfile(profileImage, name, surname, description, birthday, country, accountType);
    }

    public LiveData<AuthenticationResult> getProfileData()
    {
        return profileData;
    }

    public LiveData<AccountFormState> getAccountFormState()
    {
        return accountFormState;
    }

    public void accountDataChanged(String name, String surname, Country country)
    {
        if (!isNameValid(name))
        {
            accountFormState.setValue(new AccountFormState(R.string.name_error, null, null));
        }
        else if (!isNameValid(surname))
        {
            accountFormState.setValue(new AccountFormState(null, R.string.surname_error, null));
        }
        else if (!isCountryValid(country))
        {
            accountFormState.setValue(new AccountFormState(null, null, R.string.country_error));
        }
        else
        {
            accountFormState.setValue(new AccountFormState(true));
        }
    }

    private boolean isNameValid(String name)
    {
        if (name == null)
        {
            return false;
        }

        return !name.isEmpty();
    }

    private boolean isCountryValid(Country country)
    {
        if (country == null) return false;

        return country.code != 0;
    }

    public LiveData<Event<Boolean>> getSaveResult()
    {
        return saveResult;
    }
}