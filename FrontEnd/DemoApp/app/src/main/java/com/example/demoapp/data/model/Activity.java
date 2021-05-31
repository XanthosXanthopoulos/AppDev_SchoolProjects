package com.example.demoapp.data.model;

public class Activity extends Item
{
    private String id;
    private String title;
    private String description;
    private String tags;
    private String address;
    private String city;
    private Country country;
    private double latitude;
    private double longtitude;

    public Activity()
    {
        super(ContentType.ACTIVITY);
    }

    public Activity(String id, String title, String description, String tags, Country country, String city, String address)
    {
        super(ContentType.ACTIVITY);

        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.city = city;
        this.address = address;
        this.country = country;
    }

    public Activity(String id, String title, String description, String tags, Country country, String city, String address, double latitude, double longitude)
    {
        super(ContentType.ACTIVITY);

        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.city = city;
        this.address = address;
        this.country = country;
        this.latitude = latitude;
        this.longtitude = longitude;
    }

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

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongtitude()
    {
        return longtitude;
    }

    public void setLongtitude(double longtitude)
    {
        this.longtitude = longtitude;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }
}
