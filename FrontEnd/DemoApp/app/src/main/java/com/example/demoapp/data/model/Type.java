package com.example.demoapp.data.model;

public enum Type
{
    INDOOR("Activity", 0),
    OUTDOOR("Post", 1),
    MUSEUM("User", 2);

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
