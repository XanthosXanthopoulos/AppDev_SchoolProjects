package com.example.demoapp.data.model;

public class User
{
    private String username;
    private String name;
    private String surname;
    private String email;

    private String jwToken;

    public User()
    {
    }


    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getJwToken()
    {
        return jwToken;
    }

    public void setJwToken(String jwToken)
    {
        this.jwToken = jwToken;
    }
}
