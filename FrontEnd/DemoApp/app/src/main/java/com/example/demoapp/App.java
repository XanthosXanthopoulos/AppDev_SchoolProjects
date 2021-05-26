package com.example.demoapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentProvider;
import android.os.Build;

import com.example.demoapp.data.datasource.DiskDataSource;
import com.example.demoapp.util.ApiHandler;
import com.example.demoapp.util.ImageLoader;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        DiskDataSource.init(this, executorService);
    }

    public static Application getInstance()
    {
        return instance;
    }
}