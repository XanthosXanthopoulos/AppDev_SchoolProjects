package com.example.demoapp.ui.main.account;

import androidx.annotation.Nullable;

public class AccountFormState
{
    @Nullable
    private final Integer nameError;
    @Nullable
    private final Integer surnameError;
    @Nullable
    private final Integer countryError;

    private final boolean isDataValid;

    AccountFormState(@Nullable Integer nameError, @Nullable Integer surnameError, @Nullable Integer countryError)
    {
        this.nameError = nameError;
        this.surnameError = surnameError;
        this.countryError = countryError;
        this.isDataValid = false;
    }

    AccountFormState(boolean isDataValid)
    {
        this.nameError = null;
        this.surnameError = null;
        this.countryError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getNameError()
    {
        return nameError;
    }

    @Nullable
    public Integer getSurnameError()
    {
        return surnameError;
    }

    @Nullable
    public Integer getCountryError()
    {
        return countryError;
    }

    public boolean isDataValid()
    {
        return isDataValid;
    }
}
