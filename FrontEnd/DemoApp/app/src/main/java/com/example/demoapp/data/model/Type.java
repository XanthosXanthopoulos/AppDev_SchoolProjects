package com.example.demoapp.data.model;

public enum Type
{
    ACTIVITY("Activity", 0),
    POST("Post", 1),
    USER("User", 2);

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
