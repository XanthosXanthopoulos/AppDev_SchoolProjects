package com.example.demoapp.data.model;

import java.util.Date;

public class Trip extends Item
{
    private Image image;
    private String username;
    private String description;
    private Country country;
    private Date date;

    public Trip(Image img, String description, Country country, Date date)
    {
        image = img;
        this.description = description;
        this.country = country;
        this.date = date;
    }

    public Trip(String description, Country country, Date date)
    {
        super(ContentType.TRIP);
        this.description = description;
        this.country = country;
        this.date = date;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Country getCountry() {
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
}
