package com.example.demoapp.data.model.api.request;

public class SingInCredentialsModel
{
    private String email;

    private String password;

    public SingInCredentialsModel() {}

    public SingInCredentialsModel(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
