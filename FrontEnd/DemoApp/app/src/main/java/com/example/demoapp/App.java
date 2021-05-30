package com.example.demoapp;

import android.app.Application;

import com.example.demoapp.util.ApiHandler;
import com.example.demoapp.util.ImageLoader;

public class App extends Application
{
    private static App instance;

    public static final String SHARED_PREFS = "MeMoPrefs";

    public void onCreate()
    {
        super.onCreate();
        instance = App.this;

        ApiHandler.initialize(this);
        ImageLoader.initialize(this);

    }

    public static Application getInstance()
    {
        return instance;
    }
}