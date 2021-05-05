package com.example.demoapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentProvider;
import android.os.Build;

import com.example.demoapp.data.datasource.DiskDataSource;
import com.example.demoapp.util.ApiHandler;

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

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     * <p>Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.</p>
     *
     * <p>If you override this method, be sure to call {@code super.onCreate()}.</p>
     *
     * <p class="note">Be aware that direct boot may also affect callback order on
     * Android {@link Build.VERSION_CODES#N} and later devices.
     * Until the user unlocks the device, only direct boot aware components are
     * allowed to run. You should consider that all direct boot unaware
     * components, including such {@link ContentProvider}, are
     * disabled until user unlock happens, especially when component callback
     * order matters.</p>
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = App.this;

        ApiHandler.initialize(this);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        DiskDataSource.init(this, executorService);
    }

    public static Application getInstance()
    {
        return instance;
    }
}