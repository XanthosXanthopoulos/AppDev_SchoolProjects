package com.example.demoapp.ui.main.plan;

import androidx.annotation.Nullable;

public class CreatePlanFormState
{
    @Nullable
    private final Integer titleError;

    private final boolean isDataValid;

    CreatePlanFormState(@Nullable Integer titleError)
    {
        this.titleError = titleError;
        this.isDataValid = false;
    }

    CreatePlanFormState(boolean isDataValid)
    {
        this.titleError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getTitleError()
    {
        return titleError;
    }

    public boolean isDataValid()
    {
        return isDataValid;
    }
}
