package com.example.demoapp.data.model;

public class Comment
{
    private String username;
    private String profileImageID;
    private String content;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getProfileImageID()
    {
        return profileImageID;
    }

    public void setProfileImageID(String profileImageID)
    {
        this.profileImageID = profileImageID;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
