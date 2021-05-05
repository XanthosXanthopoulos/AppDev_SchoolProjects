package com.example.demoapp.actions;

public interface FollowActions
{
    void follow(String userID);

    void accept(String userID);

    void decline(String userID);

    void unfollow(String userID);

    void remove(String userID);

    void cancel(String userID);
}
