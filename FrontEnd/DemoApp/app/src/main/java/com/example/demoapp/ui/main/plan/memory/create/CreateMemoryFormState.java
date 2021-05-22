package com.example.demoapp.ui.main.plan.memory.create;

import androidx.annotation.Nullable;

public class CreateMemoryFormState
{
    @Nullable
    private final Integer titleError;

    @Nullable
    private final Integer countryError;

    private final boolean isDataValid;

    CreateMemoryFormState(@Nullable Integer titleError, @Nullable Integer countryError)
    {
        this.titleError = titleError;
        this.countryError = countryError;
        this.isDataValid = false;
    }

    CreateMemoryFormState(boolean isDataValid)
    {
        this.titleError = null;
        this.countryError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getTitleError()
    {
        return titleError;
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
