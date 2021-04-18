package com.example.demoapp.data.model;

public enum Type
{
    INDOOR("Indoor", 1),
    OUTDOOR("Outdoor", 2),
    MUSEUM("Museum", 3);

    public final String label;
    public final int code;

    Type(String label, int code)
    {
        this.label = label;
        this.code = code;
    }

    @Override
    public String toString()
    {
        return label;
    }
}
