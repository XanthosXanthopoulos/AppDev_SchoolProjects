package com.example.demoapp.data.model.api.response;

import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;

import java.util.List;

public class PostResponseModel
{
    private String title;
    private String description;
    private Country country;
    private List<String> images;
    private List<Activity> activities;
}
