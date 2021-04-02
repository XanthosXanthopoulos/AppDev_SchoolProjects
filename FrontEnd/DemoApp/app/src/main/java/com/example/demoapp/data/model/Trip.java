package com.example.demoapp.data.model;

import com.android.volley.toolbox.StringRequest;

import java.util.Date;

public class Trip
{
    private String username;
    private String description;
    private Country Country;
    private Date date;

    public Trip(String description, com.example.demoapp.data.model.Country country, Date date)
    {
        this.description = description;
        Country = country;
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
