package com.example.demoapp.ui.authentication.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the register form.
 */
class RegisterFormState
{
    @Nullable
    private final Integer usernameError;
    @Nullable
    private final Integer emailError;
    @Nullable
    private final Integer passwordError;
    @Nullable
    private final Integer  confirmPasswordError;
    private final boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer confirmPasswordError)
    {
        this.usernameError = usernameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid)
    {
        this.usernameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.confirmPasswordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError()
    {
        return usernameError;
    }

    @Nullable
    Integer getEmailError()
    {
        return emailError;
    }

    @Nullable
    Integer getPasswordError()
    {
        return passwordError;
    }

    @Nullable
    Integer getConfirmPasswordError()
    {
        return confirmPasswordError;
    }

    boolean isDataValid()
    {
        return isDataValid;
    }

}
