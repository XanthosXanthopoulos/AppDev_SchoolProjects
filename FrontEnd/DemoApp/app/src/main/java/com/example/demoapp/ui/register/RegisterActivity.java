package com.example.demoapp.ui.register;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;
import com.example.demoapp.ui.login.LoginActivity;
import com.example.demoapp.ui.login.LoginViewModel;
import com.example.demoapp.util.ViewModelFactory;

public class RegisterActivity extends AppCompatActivity
{
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(RegisterViewModel.class);

        final EditText usernameEditText = findViewById(R.id.Register_Username);
        final EditText emailEditText = findViewById(R.id.Register_Email);
        final EditText passwordEditText = findViewById(R.id.Register_Password);
        final EditText confirmPasswordEditText = findViewById(R.id.Register_ConfirmPassword);
        final Button registerButton = findViewById(R.id.Register_Button);
        final Button cancelButton = findViewById(R.id.Cancel_Button);
        final ProgressBar loadingProgressBar = findViewById(R.id.Register_Loading);

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>()
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

        registerViewModel.getRegistersResult().observe(this, new Observer<AuthenticationResult>()
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

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void updateUiWithUser(AuthenticatedUserView model)
    {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(@StringRes Integer errorString)
    {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}