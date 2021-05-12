package com.example.demoapp.data.model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Post extends Item
{
    private String profileImageID;
    private String thumbnailImageID;
    private String username;
    private String title;
    private String description;
    private Date date;

    private Deque<Activity> activities;
    private Deque<Uri> images;

    public Post()
    {
        super(ContentType.POST);

        this.activities = new LinkedList<>();
        this.images = new LinkedList<>();
    }

    public Post(String username, String description, Date date)
    {
        super(ContentType.POST);

        this.username = username;
        this.description = description;
        this.date = date;
        this.activities = new LinkedList<>();
        this.images = new LinkedList<>();
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

    public String getThumbnailImageID()
    {
        return thumbnailImageID;
    }

    public void setThumbnailImageID(String thumbnailImageID)
    {
        this.thumbnailImageID = thumbnailImageID;
    }

    public Deque<Activity> getActivities()
    {
        return activities;
    }

    public void setActivities(Deque<Activity> activities)
    {
        this.activities = activities;
    }

    public Deque<Uri> getImages()
    {
        return images;
    }

    public void setImages(Deque<Uri> images)
    {
        this.images = images;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
