package com.example.demoapp.data.model;

public class Notification
{
    private String userID;
    private String username;
    private String userImageID;
    private String message;

    public Notification() { }

    public Notification(String userID, String username, String userImageID, String message)
    {
        this.userID = userID;
        this.username = username;
        this.userImageID = userImageID;
        this.message = message;
    }


    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUserImageID()
    {
        return userImageID;
    }

    public void setUserImageID(String userImageID)
    {
        this.userImageID = userImageID;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return username.trim() + " " + message.trim();
    }
}
