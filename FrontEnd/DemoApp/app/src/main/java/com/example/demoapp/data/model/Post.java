package com.example.demoapp.data.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Post extends Item
{
    private String profileImageID;
    private String thumbnailImageID;
    private String username;
    private String description;
    private Country country;
    private Date date;

    public Post()
    {
        super(ContentType.POST);
    }

    public Post(Bitmap accountImage, String username, Bitmap planImage, String description, Country country, Date date)
    {
        super(ContentType.POST);

        this.username = username;
        this.description = description;
        this.country = country;
        this.date = date;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
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
}
