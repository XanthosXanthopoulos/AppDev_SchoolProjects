package com.example.demoapp.ui.main.trip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Trip;
import com.example.demoapp.data.model.TripContent;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;
import com.example.demoapp.ui.adapter.TripAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TripFragment extends Fragment
{
    private TripViewModel tripViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.Trip_List);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        List<TripContent> sList = getListItemData();

        ActivityImageAdapter rcAdapter = new ActivityImageAdapter(sList);
        recyclerView.setAdapter(rcAdapter);

        return view;
    }

    private List<TripContent> getListItemData()
    {
        List<TripContent> listViewItems = new ArrayList<>();

        String title = "Lorem ipsum";
        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce elementum neque non sapien sagittis, vestibulum dignissim ipsum pellentesque. Sed fringilla vulputate urna et consectetur.";
        String address = "Fusce porta tincidunt eros";
        String type = "Indoor";
        String tags = "Curabitur, Tortor, Libero";

        listViewItems.add(new Activity(title, desc, tags, address, type));
        listViewItems.add(new Activity(title, desc, tags, address, type));
        listViewItems.add(new Activity(title, desc, tags, address, type));
        listViewItems.add(new Activity(title, desc, tags, address, type));
        listViewItems.add(new Activity(title, desc, tags, address, type));
        listViewItems.add(new Activity(title, desc, tags, address, type));
        listViewItems.add(new Activity(title, desc, tags, address, type));

        return listViewItems;
    }
}