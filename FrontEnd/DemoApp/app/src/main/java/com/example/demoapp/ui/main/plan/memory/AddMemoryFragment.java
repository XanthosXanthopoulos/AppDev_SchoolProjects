package com.example.demoapp.ui.main.plan.memory;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddMemoryFragment extends Fragment
{
    List<Item> ActivityList = new ArrayList<>() ;
    RecyclerView recyclerView;
    private AddMemoryViewModel mViewModel;

    public static AddMemoryFragment newInstance()
    {
        return new AddMemoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_add_memory, container, false);

        recyclerView = view.findViewById(R.id.memory_list);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        ActivityImageAdapter rcAdapter = new ActivityImageAdapter(getListItemData());
        recyclerView.setAdapter(rcAdapter);

        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        FrameLayout item = requireActivity().findViewById(R.id.pop_up_layout);
//        View child = getLayoutInflater().inflate(R.layout.fragment_create_memory,null);
//        item.addView(child);

        view.findViewById(R.id.create_memory_button).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_CreateMemory));

//        item.findViewById(R.id.add_activity).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityImageAdapter rcAdapter = new ActivityImageAdapter(updateListItemData(getListItemData()));
//                recyclerView.setAdapter(rcAdapter);
//                item.setVisibility(View.GONE);
//
//            }
//        });
//
//        item.findViewById(R.id.cross_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.setVisibility(View.GONE);
//            }
//        });

    }

    private List<Item> getListItemData()
    {
        return ActivityList;
    }

    public static AddMemoryFragment getInstance(){
        AddMemoryFragment addMemoryFragment = new AddMemoryFragment();
        return addMemoryFragment;
    }
}