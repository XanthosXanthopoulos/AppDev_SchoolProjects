package com.example.demoapp.data.model.api.request;

public class SearchQueryModel
{
    String query;

    String country;

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    String city;

    int radius;

    public SearchQueryModel(String query, String country, String city, int radius)
    {
        this.query = query;
        this.country = country;
        this.city = city;
        this.radius = radius;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }
}
