package com.example.demoapp.data.model;

import java.util.Date;

public class User
{
    private String username;
    private String name;
    private String surname;
    private String email;
    private String description;
    private Date Birthday;
    private Country country;
    private AccountType accountType;
    private String profileImage;

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

    public Date getBirthday()
    {
        return Birthday;
    }

    public void setBirthday(Date birthday)
    {
        Birthday = birthday;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public AccountType getAccountType()
    {
        return accountType;
    }

    public void setAccountType(AccountType accountType)
    {
        this.accountType = accountType;
    }

    public String getProfileImage()
    {
        return profileImage;
    }

    public void setProfileImage(String profileImage)
    {
        this.profileImage = profileImage;
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
