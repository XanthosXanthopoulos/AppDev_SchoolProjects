package com.example.demoapp.data.model.api.request;

public class SearchQueryModel
{
    String query;

    String country;

    String type;

    int radius;

    public SearchQueryModel(String query, String country, String type, int radius)
    {
        this.query = query;
        this.country = country;
        this.type = type;
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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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
