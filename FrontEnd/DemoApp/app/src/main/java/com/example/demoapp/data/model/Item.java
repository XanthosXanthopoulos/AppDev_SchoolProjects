package com.example.demoapp.data.model;

public abstract class Item
{
    public ContentType contentType;

    //public int contentType;

    public Item(ContentType conteType)
    {
        this.contentType = conteType;
    }


    public ContentType getConteType()
    {
        return contentType;
    }
}
