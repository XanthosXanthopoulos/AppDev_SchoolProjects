package com.example.demoapp.ui.authentication.register;

import android.util.Patterns;

import androidx.annotation.WorkerThread;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
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

public class RegisterViewModel extends ViewModel
{
    private final MutableLiveData<RegisterFormState> registerFormState;
    private final LiveData<AuthenticationResult> registerResult;
    private final UserRepository userRepository;

    public RegisterViewModel(UserRepository userRepository)
    {
        this.userRepository = userRepository;
        registerFormState = new MutableLiveData<>();

        registerResult = Transformations.map(userRepository.getResult(), input ->
        {
            if (input.isSuccessful())
            {
                return new AuthenticationResult(new AuthenticatedUserView(input.getResponse().getJwToken()));
            }
            else
            {
                return new AuthenticationResult(input.getErrorMessage());
            }
        });
    }

    LiveData<RegisterFormState> getRegisterFormState()
    {
        return registerFormState;
    }

    LiveData<AuthenticationResult> getRegistersResult()
    {
        return registerResult;
    }

    public void register(String username, String email, String password, String confirmPassword)
    {
        userRepository.register(username, email, password, confirmPassword);
    }

    public void registerDataChanged(String username, String email, String password, String confirmPassword)
    {
        if (!isUsernameValid(username))
        {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null));
        }
        else if (!isEmailValid(email))
        {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_email, null, null));
        }
        else if (!isPasswordValid(password))
        {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_password, null));
        }
        else if (!isConfirmPasswordValid(password, confirmPassword))
        {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.password_not_match));
        }
        else
        {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isUsernameValid(String username)
    {
        if (username == null)
        {
            return false;
        }

        return !username.isEmpty();
    }

    private boolean isEmailValid(String email)
    {
        if (email == null)
        {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private boolean isPasswordValid(String password)
    {
        return password != null && password.trim().length() > 5;
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword)
    {
        return password.trim().equals(confirmPassword.trim());
    }
}
