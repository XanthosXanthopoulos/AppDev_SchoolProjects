package com.example.demoapp.ui.main.account;

import androidx.annotation.Nullable;

public class AccountFormState
{
    @Nullable
    private final Integer nameError;
    @Nullable
    private final Integer surnameError;

    private final boolean isDataValid;

    AccountFormState(@Nullable Integer usernameError, @Nullable Integer passwordError)
    {
        this.nameError = usernameError;
        this.surnameError = passwordError;
        this.isDataValid = false;
    }

    AccountFormState(boolean isDataValid)
    {
        this.nameError = null;
        this.surnameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getNameError()
    {
        return nameError;
    }
}
