package com.example.demoapp.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.demoapp.App;

import static com.example.demoapp.App.SHARED_PREFS;

public abstract class Repository
{
    protected void saveToPrefs(String key, String value)
    {
        SharedPreferences sharedPref = App.getInstance().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    protected String loadFromPrefs(String key)
    {
        SharedPreferences sharedPref = App.getInstance().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }
}
