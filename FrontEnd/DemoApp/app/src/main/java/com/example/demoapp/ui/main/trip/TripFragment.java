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
import com.example.demoapp.data.model.Item;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;

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

        List<Item> sList = getListItemData();

        ActivityImageAdapter rcAdapter = new ActivityImageAdapter(sList);
        recyclerView.setAdapter(rcAdapter);

        return view;
    }

    private List<Item> getListItemData()
    {
        List<Item> listViewItems = new ArrayList<>();


        return listViewItems;
    }
}