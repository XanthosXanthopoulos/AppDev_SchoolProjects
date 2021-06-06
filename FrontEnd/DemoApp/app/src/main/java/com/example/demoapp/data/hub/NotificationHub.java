package com.example.demoapp.data.hub;


import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.Notification;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Single;

public class NotificationHub
{
    private static volatile NotificationHub instance;

    private static HubConnection hubConnection;

    private static LinkedList<Notification> notifications;
    private static MutableLiveData<Event<Notification>> notificationsLiveData;

    public MutableLiveData<Event<Notification>> getNotificationsLiveData()
    {
        return notificationsLiveData;
    }

    public NotificationHub()
    {
        notifications = new LinkedList<>();
        notificationsLiveData = new MutableLiveData<>();
    }

    public static void init(String JWToken)
    {
        hubConnection = HubConnectionBuilder.create("http://192.168.1.109:5000/notifications").withAccessTokenProvider(Single.defer(() -> Single.just(JWToken))).build();

        hubConnection.on("followRequest", (followerID, username, followerImageID) ->
        {
            Notification notification = new Notification(followerID, username, followerImageID, "wants to follow you.");
            notifications.addFirst(notification);
            notificationsLiveData.postValue(new Event<>(notification));
        }, String.class, String.class, String.class);

        hubConnection.on("acceptRequest", (followeeID, username, followeeImageID) ->
        {
            Notification notification = new Notification(followeeID, username, followeeImageID, "accepted your request.");
            notifications.addFirst(notification);
            notificationsLiveData.postValue(new Event<>(notification));
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

    public LinkedList<Notification> getNotifications()
    {
        return notifications;
    }

    public void declineFollowRequest(String userID)
    {
        hubConnection.send("Decline", userID);
    }

    public void acceptFollowRequest(String userID)
    {
        hubConnection.send("Accept", userID);
    }

    public void removeFollower(String userID)
    {
        hubConnection.send("Remove", userID);
    }

    public void sendFollowRequest(String userID)
    {
        hubConnection.send("Follow", userID);
    }

    public void unfollow(String userID)
    {
        hubConnection.send("Unfollow", userID);
    }

    public void cancelFollowRequest(String userID)
    {
        hubConnection.send("Cancel", userID);
    }

    public void sendComment(int postID, String content)
    {
        hubConnection.send("SendComment", postID, content);
    }

    public void sendLike(int postID)
    {
        hubConnection.send("SendLike", postID);
    }

    public void logout()
    {
        notifications.clear();
        Event<Notification> event = new Event<>(null);
        event.setHandled(true);
        notificationsLiveData.postValue(event);

        hubConnection.stop();
    }
}
