package com.example.demoapp.data.datasource;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.datasource.DataSourceResponse;

import java.util.HashMap;

public class DiskDataSource
{
    private HashMap<String, String> files;

    public LiveData<DataSourceResponse<Bitmap>> loadImage(String imageID)
    {
        MutableLiveData<DataSourceResponse<Bitmap>> result = new MutableLiveData<>();

        return result;
    }
}
