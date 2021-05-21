package com.example.demoapp.util;

import android.util.EventLogTags;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class
Place {

    String place = null,description = null;
    LatLng coordinates = null;

    //    Constructors
    public Place(){

    }

    public Place(String string, LatLng lng){
        place = string;
        coordinates = lng;
    }

    public Place(String string, String description, LatLng lng){
        place = string;
        coordinates = lng;
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return place;
    }

    //    Setters & Getters
    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public String getPlace() {
        return place;
    }

    public String getDescription() {
        return description;
    }
}
