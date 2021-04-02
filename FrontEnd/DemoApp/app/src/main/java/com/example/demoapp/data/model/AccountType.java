package com.example.demoapp.data.model;

import com.google.gson.annotations.SerializedName;

public enum AccountType
{
    @SerializedName("0")
    Public(0),

    @SerializedName("1")
    Private(1);

    public final int value;

    AccountType(int value)
    {
        this.value = value;
    }
}
