package com.example.demoapp.data.viewmodel;

import androidx.annotation.Nullable;

import com.example.demoapp.data.view.AuthenticatedUserView;

public class AuthenticationResult
{
    @Nullable
    private AuthenticatedUserView success;
    @Nullable
    private String error;

    public AuthenticationResult(@Nullable String error)
    {
        this.error = error;
    }

    public AuthenticationResult(@Nullable AuthenticatedUserView success)
    {
        this.success = success;
    }

    @Nullable
    public AuthenticatedUserView getSuccess()
    {
        return success;
    }

    @Nullable
    public String getError()
    {
        return error;
    }
}
