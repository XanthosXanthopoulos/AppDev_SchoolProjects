package com.example.demoapp.data.model.api.response;

import com.example.demoapp.data.model.Country;

public class PostSummaryResponseModel
{
    private String username;
    private String profileImageID;
    private String thumbnailImageID;
    private Country country;
    private String description;

    public PostSummaryResponseModel() { }

    public PostSummaryResponseModel(String username, String profileImageID, String thumbnailImageID, Country country, String description)
    {
        this.username = username;
        this.profileImageID = profileImageID;
        this.thumbnailImageID = thumbnailImageID;
        this.country = country;
        this.description = description;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getProfileImageID()
    {
        return profileImageID;
    }

    public void setProfileImageID(String profileImageID)
    {
        this.profileImageID = profileImageID;
    }

    public String getThumbnailImageID()
    {
        return thumbnailImageID;
    }

    public void setThumbnailImageID(String thumbnailImageID)
    {
        this.thumbnailImageID = thumbnailImageID;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
