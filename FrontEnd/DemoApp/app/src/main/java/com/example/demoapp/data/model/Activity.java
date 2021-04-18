package com.example.demoapp.data.model;

public class Activity extends Item
{
    private String title;
    private String description;
    private String tags;
    private String address;
    private String type;

    public Activity()
    {
        super(ContentType.ACTIVITY);
    }

    public Activity(String title, String description, String tags, String address, String type)
    {
        super(ContentType.ACTIVITY);

        this.title = title;
        this.description = description;
        this.tags = tags;
        this.address = address;
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
