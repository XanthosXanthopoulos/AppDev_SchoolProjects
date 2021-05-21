package com.example.demoapp.data.view;

import com.example.demoapp.data.model.AccountType;
import com.example.demoapp.data.model.Country;

/**
 * Class exposing authenticated user details to the UI.
 */
public class AuthenticatedUserView
{
    private final String userID;
    private String profileImageID;
    private String name;
    private String surname;
    private String email;
    private String description;
    private int year;
    private int month;
    private int day;
    private Country country;

    public void setProfileImageID(String profileImageID)
    {
        this.profileImageID = profileImageID;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public void setAccountType(AccountType accountType)
    {
        this.accountType = accountType;
    }

    public AuthenticatedUserView(String userID, String profileImageID, String name, String surname, String email, String description, int year, int month, int day, Country country, AccountType accountType)
    {
        this.userID = userID;
        this.profileImageID = profileImageID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.description = description;
        this.year = year;
        this.month = month;
        this.day = day;
        this.country = country;
        this.accountType = accountType;
    }

    private AccountType accountType;

    public AuthenticatedUserView(String userID)
    {
        this.userID = userID;
    }

    public String getUserID()
    {
        return userID;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getEmail()
    {
        return email;
    }

    public String getDescription()
    {
        return description;
    }

    public Country getCountry()
    {
        return country;
    }

    public AccountType getAccountType()
    {
        return accountType;
    }

    public int getYear()
    {
        return year;
    }

    public int getMonth()
    {
        return month;
    }

    public int getDay()
    {
        return day;
    }

    public String getProfileImageID()
    {
        return profileImageID;
    }
}