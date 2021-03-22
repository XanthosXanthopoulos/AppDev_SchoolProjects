package com.example.demoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter {
    private View markerItemView;
    LayoutInflater inflater;

    @SuppressLint("ResourceType")
    public CustomMarkerInfoWindowView(Context context) {
        inflater = LayoutInflater.from(context);
        markerItemView = inflater.inflate(R.layout.marker_info_window, null);

    }
    @Override
    public View getInfoWindow(Marker marker) {

        TextView itemNameTextView = (TextView) markerItemView.findViewById(R.id.Text1);
        TextView itemAddressTextView = (TextView) markerItemView.findViewById(R.id.Text2);

        itemNameTextView.setText(marker.getTitle());
//        itemAddressTextView.setText(user.getAddress());

        return markerItemView;

    }

    public void closeInfoWindow(Marker marker){
        marker.hideInfoWindow();
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}