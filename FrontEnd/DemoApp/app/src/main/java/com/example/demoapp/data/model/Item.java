package com.example.demoapp.data.model;

public abstract class Item
{
    public ContentType contentType;

    //public int contentType;

    public Item(ContentType contentType)
    {
        this.contentType = contentType;
    }


    public ContentType getContentType()
    {
        return contentType;
    }
}
