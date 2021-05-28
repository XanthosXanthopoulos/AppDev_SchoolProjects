package com.example.demoapp.ui.location_picker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.demoapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.jetbrains.annotations.NotNull;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.select_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent data = new Intent();
                double latitude = map.getCameraPosition().target.latitude;
                double longitude = map.getCameraPosition().target.longitude;
                data.putExtra("latitude", latitude);
                data.putExtra("longitude", longitude);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap)
    {
        map = googleMap;
    }
}