package com.example.demoapp.data.hub;


import android.util.Log;

import com.example.demoapp.util.ApiRoutes;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import io.reactivex.functions.Action;

public class NotificationHub
{
    private static volatile NotificationHub instance;

    private static HubConnection hubConnection;

    public static void init(String JWToken)
    {
        hubConnection = HubConnectionBuilder.create("http://192.168.1.109:5000/notifications").withHeader("Authorization", JWToken).build();

        hubConnection.on("followRequest", (followerID) ->
        {
            Log.i("SIGNALR", "Request sent from followerID");
        }, String.class);

        hubConnection.start();
    }

    public static NotificationHub getInstance()
    {
        if (instance == null)
        {
            instance = new NotificationHub();
        }

        return instance;
    }


}
