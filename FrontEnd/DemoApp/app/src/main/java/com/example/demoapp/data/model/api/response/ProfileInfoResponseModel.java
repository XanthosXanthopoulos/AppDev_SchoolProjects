package com.example.demoapp.data.model.api.response;

import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.AccountType;

public class ProfileInfoResponseModel
{
    private String username;

    private String email;

    private String name;

    private String surname;

    private String birthday;

    private Country country;

    private String description;

    private AccountType accountType;

    public ProfileInfoResponseModel(String username, String email, String name, String surname, String description, String birthday, Country country, AccountType accountType)
    {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.country = country;
        this.description = description;
        this.accountType = accountType;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
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

    public AccountType getAccountType()
    {
        return accountType;
    }

    public void setAccountType(AccountType accountType)
    {
        this.accountType = accountType;
    }
}
