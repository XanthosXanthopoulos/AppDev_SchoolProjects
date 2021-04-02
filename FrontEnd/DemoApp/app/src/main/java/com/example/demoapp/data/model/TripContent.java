package com.example.demoapp.data.model;

public abstract class TripContent
{
    private final ContentType contentType;

    public TripContent(ContentType contentType)
    {
        this.contentType = contentType;
    }


    public ContentType getContentType()
    {
        return contentType;
    }
}
