package com.example.demoapp.data.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Post extends Item
{
    private Bitmap accountImage;
    private String username;
    private Bitmap planImage;
    private String description;
    private Country Country;
    private Date date;

    public Post()
    {
        super(ContentType.POST);
    }

    public Post(Bitmap accountImage, String username, Bitmap planImage, String description, Country country, Date date)
    {
        super(ContentType.POST);

        this.accountImage = accountImage;
        this.username = username;
        this.planImage = planImage;
        this.description = description;
        Country = country;
        this.date = date;
    }

    public Bitmap getAccountImage()
    {
        return accountImage;
    }

    public void setAccountImage(Bitmap accountImage)
    {
        this.accountImage = accountImage;
    }

    public Bitmap getPlanImage()
    {
        return planImage;
    }

    public void setPlanImage(Bitmap planImage)
    {
        this.planImage = planImage;
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
        return Country;
    }

    public void setCountry(Country country)
    {
        Country = country;
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
}
