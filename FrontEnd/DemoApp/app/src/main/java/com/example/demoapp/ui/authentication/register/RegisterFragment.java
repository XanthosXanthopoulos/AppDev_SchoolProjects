package com.example.demoapp.ui.authentication.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;
import com.example.demoapp.util.ViewModelFactory;

import static com.example.demoapp.App.SHARED_PREFS;

public class RegisterFragment extends Fragment
{
    private RegisterViewModel registerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        registerViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(RegisterViewModel.class);

        final EditText usernameEditText = view.findViewById(R.id.Register_Username);
        final EditText emailEditText = view.findViewById(R.id.Register_Email);
        final EditText passwordEditText = view.findViewById(R.id.Register_Password);
        final EditText confirmPasswordEditText = view.findViewById(R.id.Register_ConfirmPassword);
        final Button registerButton = view.findViewById(R.id.Register_Button);
        final Button cancelButton = view.findViewById(R.id.Cancel_Button);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.Register_Loading);

        registerViewModel.getRegisterFormState().observe(getViewLifecycleOwner(), new Observer<RegisterFormState>()
        {
            @Override
            public void onChanged(RegisterFormState registerFormState)
            {
                if (registerFormState == null) return;

                registerButton.setEnabled(registerFormState.isDataValid());

                if (registerFormState.getUsernameError() != null)
                {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getEmailError() != null)
                {
                    emailEditText.setError(getString(registerFormState.getEmailError()));
                }
                if (registerFormState.getPasswordError() != null)
                {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getConfirmPasswordError() != null)
                {
                    confirmPasswordEditText.setError(getString(registerFormState.getConfirmPasswordError()));
                }
            }
        });

        registerViewModel.getRegistersResult().observe(getViewLifecycleOwner(), new Observer<AuthenticationResult>()
        {
            @Override
            public void onChanged(AuthenticationResult registerResult)
            {
                if (registerResult == null) return;

                loadingProgressBar.setVisibility(View.GONE);
                registerButton.setEnabled(true);

                if (registerResult.getError() != null)
                {
                    showRegisterFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null)
                {
                    updateUiWithUser(registerResult.getSuccess());
                    Navigation.findNavController(view).navigate(R.id.navigation_account);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                registerViewModel.registerDataChanged(usernameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener);

        confirmPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    registerViewModel.register(usernameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
                }

                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerButton.setEnabled(false);
                registerViewModel.register(usernameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
            }
        });

        cancelButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_login));

        return view;
    }

    private void updateUiWithUser(AuthenticatedUserView model)
    {
        SharedPreferences sharedPref = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("JWToken", model.getUserID());
        editor.apply();
    }

    private void showRegisterFailed(String errorString)
    {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}