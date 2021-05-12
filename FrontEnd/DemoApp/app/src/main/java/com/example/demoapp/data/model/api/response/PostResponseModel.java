package com.example.demoapp.data.model.api.response;

import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;

import java.util.Date;
import java.util.List;

public class PostResponseModel
{
    private int postID;
    private String title;
    private String description;
    private Date date;
    private String username;
    private String profileImageID;
    private List<String> images;
    private List<Activity> activities;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

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

    public List<String> getImages()
    {
        return images;
    }

    public void setImages(List<String> images)
    {
        this.images = images;
    }

    public List<Activity> getActivities()
    {
        return activities;
    }

    public void setActivities(List<Activity> activities)
    {
        this.activities = activities;
    }
}
