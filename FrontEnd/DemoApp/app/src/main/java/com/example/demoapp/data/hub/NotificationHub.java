package com.example.demoapp.data.hub;


import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.data.model.Notification;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.util.LinkedList;
import java.util.List;

public class NotificationHub
{
    private static volatile NotificationHub instance;

    private static HubConnection hubConnection;

    private static List<Notification> notifications;
    private static MutableLiveData<List<Notification>> notificationsLiveData;

    public MutableLiveData<List<Notification>> getNotificationsLiveData()
    {
        return notificationsLiveData;
    }

    public NotificationHub()
    {
        notifications = new LinkedList<>();
        notificationsLiveData = new MutableLiveData<>(notifications);
    }

    public static void init(String JWToken)
    {
        hubConnection = HubConnectionBuilder.create("http://192.168.1.109:5000/notifications").withHeader("Authorization", JWToken).build();

        hubConnection.on("followRequest", (followerID, username, followerImageID) ->
        {
            Notification notification = new Notification(followerID, username, followerImageID, "wants to follow you.");
            if (notifications.size() > 9) notifications.remove(9);

            notifications.add(0, notification);
            notificationsLiveData.setValue(notifications);
        }, String.class, String.class, String.class);

        hubConnection.on("followAccepted", (followeeID, username, followeeImageID) ->
        {
            Notification notification = new Notification(followeeID, username, followeeImageID, "accepted your request.");
            if (notifications.size() > 9) notifications.remove(9);

            notifications.add(0, notification);
            notificationsLiveData.setValue(notifications);
        }, String.class, String.class, String.class);

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
