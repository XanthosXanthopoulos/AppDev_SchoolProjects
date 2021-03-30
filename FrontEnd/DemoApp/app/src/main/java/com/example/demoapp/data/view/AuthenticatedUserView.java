package com.example.demoapp.data.view;

import android.security.keystore.UserNotAuthenticatedException;

/**
 * Class exposing authenticated user details to the UI.
 */
public class AuthenticatedUserView
{
    private final String displayName;
    //... other data fields that may be accessible to the UI

    public AuthenticatedUserView(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}