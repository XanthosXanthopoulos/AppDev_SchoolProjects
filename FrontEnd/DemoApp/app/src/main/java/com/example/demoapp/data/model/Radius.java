package com.example.demoapp.data.model;

public enum Radius
{
    M100("100m", 100),
    M500("500m", 500),
    KM1("1km", 1000),
    KM5("5km", 5000);

    public final String label;
    public final int radius;

    Radius(String label, int code)
    {
        this.label = label;
        this.radius = code;
    }

    @Override
    public String toString()
    {
        return label;
    }
}
