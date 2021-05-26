package com.example.demoapp.ui.main.plan.memory;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.main.plan.memory.create.CreateMemoryViewModel;
import com.example.demoapp.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class AddMemoryFragment extends Fragment
{
    private AddMemoryViewModel viewModel;

    private RecyclerView activityList;
    private SearchResultAdapter adapter;

    private Button createMemory;

    public static AddMemoryFragment newInstance()
    {
        return new AddMemoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_memory, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(AddMemoryViewModel.class);

        createMemory = view.findViewById(R.id.create_memory_button);
        activityList = view.findViewById(R.id.memory_list);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        activityList.setLayoutManager(_sGridLayoutManager);

        adapter = new SearchResultAdapter();
        activityList.setAdapter(adapter);


        viewModel.getActivitiesLiveData().observe(getViewLifecycleOwner(), new Observer<Iterable<Activity>>()
        {
            @Override
            public void onChanged(Iterable<Activity> activities)
            {
                //if (activities == null) return;

                List<Item> items = new ArrayList<>();
                activities.forEach(items::add);
                adapter.setItems(items);
            }
        });

        createMemory.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_createMemory));

        return view;
    }
}