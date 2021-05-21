package com.example.demoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.demoapp.data.model.Activity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter
{
    private final View view;

    @SuppressLint("ResourceType")
    public CustomMarkerInfoWindowView(Context context)
    {
        view = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        TextView titleTextView = view.findViewById(R.id.title);
        TextView descriptionTextView = view.findViewById(R.id.description);
        TextView tagsTextView = view.findViewById(R.id.tags);

        Activity activity = (Activity) marker.getTag();

        titleTextView.setText(activity.getTitle());
        descriptionTextView.setText(activity.getDescription());
        tagsTextView.setText(activity.getTags());

        return view;

    }

    public void closeInfoWindow(Marker marker)
    {
        marker.hideInfoWindow();
    }


    @Override
    public View getInfoContents(Marker marker)
    {
        return null;
    }
}