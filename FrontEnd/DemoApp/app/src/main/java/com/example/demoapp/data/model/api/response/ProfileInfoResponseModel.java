package com.example.demoapp.data.model.api.response;

import android.net.Uri;

import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.AccountType;

import java.util.Date;

public class ProfileInfoResponseModel
{
    private Uri profileImage;
    private String name;
    private String surname;
    private Date birthday;
    private Country country;
    private String description;
    private AccountType accountType;

    public ProfileInfoResponseModel(Uri profileImage, String name, String surname, String description, Date birthday, Country country, AccountType accountType)
    {
        this.profileImage = profileImage;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.country = country;
        this.description = description;
        this.accountType = accountType;
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

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
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

    public Uri getProfileImage()
    {
        return profileImage;
    }

    public void setProfileImage(Uri profileImage)
    {
        this.profileImage = profileImage;
    }
}
