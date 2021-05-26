package com.example.demoapp.data.model;

public class Follow extends Item
{
    private String profileImageID;
    private String userID;
    private String username;
    private Status status;

    public Follow()
    {
        super(ContentType.USER);
    }

    public Follow(String profileImageID, String userID, String username, Status status)
    {
        super(ContentType.USER);
        this.profileImageID = profileImageID;
        this.userID = userID;
        this.username = username;
        this.status = status;
    }

    public String getProfileImageID()
    {
        return profileImageID;
    }

    public void setProfileImageID(String profileImageID)
    {
        this.profileImageID = profileImageID;
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

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
}
