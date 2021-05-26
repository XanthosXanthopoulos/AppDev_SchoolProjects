package com.example.demoapp.data.model.api.response;

public class AuthenticationResponseModel
{
    private String username;

    private String jwToken;

    private String profileImageID;

    public AuthenticationResponseModel() {}

    public AuthenticationResponseModel(String username, String jwToken)
    {
        this.username = username;
        this.jwToken = jwToken;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getJwToken()
    {
        return jwToken;
    }

    public void setJwToken(String jwToken)
    {
        this.jwToken = jwToken;
    }

    public String getProfileImageID()
    {
        return profileImageID;
    }

    public void setProfileImageID(String profileImageID)
    {
        this.profileImageID = profileImageID;
    }
}
