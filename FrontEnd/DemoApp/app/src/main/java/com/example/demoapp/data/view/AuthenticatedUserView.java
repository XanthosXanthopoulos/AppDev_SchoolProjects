package com.example.demoapp.data.view;

import com.example.demoapp.data.model.AccountType;
import com.example.demoapp.data.model.Country;

import java.util.Date;

/**
 * Class exposing authenticated user details to the UI.
 */
public class AuthenticatedUserView
{
    private final String displayName;

    private String name;
    private String surname;
    private String email;
    private String description;
    private int year;
    private int month;
    private int day;
    private Country country;

    public AuthenticatedUserView(String displayName, String name, String surname, String email, String description, int year, int month, int day, Country country, AccountType accountType)
    {
        this.displayName = displayName;
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

    public AuthenticatedUserView(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
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
}