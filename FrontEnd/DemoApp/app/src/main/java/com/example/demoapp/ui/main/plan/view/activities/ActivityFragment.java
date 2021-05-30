package com.example.demoapp.ui.main.plan.view.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.main.plan.memory.AddMemoryViewModel;
import com.example.demoapp.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment
{

    private ActivityViewModel viewModel;

    private RecyclerView activityList;
    private SearchResultAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(ActivityViewModel.class);

        activityList = view.findViewById(R.id.activity_list);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        activityList.setLayoutManager(_sGridLayoutManager);

        adapter = new SearchResultAdapter();
        activityList.setAdapter(adapter);

        viewModel.getActivitiesResult().observe(getViewLifecycleOwner(), activities ->
        {
            List<Item> items = new ArrayList<>();
            activities.forEach(items::add);
            adapter.setItems(items);
        });

        return view;
    }
}