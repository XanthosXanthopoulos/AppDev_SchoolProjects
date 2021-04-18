package com.example.demoapp.data.model;

public abstract class Item
{
    private final ContentType contentType;

    public Item(ContentType contentType)
    {
        this.contentType = contentType;
    }


    public ContentType getContentType()
    {
        return contentType;
    }
}
