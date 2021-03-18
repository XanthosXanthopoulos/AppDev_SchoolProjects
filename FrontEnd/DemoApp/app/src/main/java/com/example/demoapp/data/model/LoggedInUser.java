package com.example.demoapp.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser
{

    private String username;
    private String jwToken;

    public LoggedInUser(String username, String jwToken)
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

    public String getJWToken()
    {
        return jwToken;
    }

    public void setJWToken(String jwToken)
    {
        this.jwToken = jwToken;
    }
}