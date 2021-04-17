package com.example.demoapp.ui.main.plan;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.TripContent;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreatePlanFragment extends Fragment {
    List<TripContent> ActivityList = new ArrayList<>() ;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        recyclerView = view.findViewById(R.id.Create_Plan_List);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        ActivityImageAdapter rcAdapter = new ActivityImageAdapter(getListItemData());
        recyclerView.setAdapter(rcAdapter);

        return view;

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_create_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout item = requireActivity().findViewById(R.id.pop_up_layout);
        View child = getLayoutInflater().inflate(R.layout.item_create_activity,null);
        item.addView(child);

        view.findViewById(R.id.create_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add activity and dynamically fill the gaps
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)item.getLayoutParams();
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT; //1300
                item.setLayoutParams(params);
                item.setVisibility(View.VISIBLE);
//                ActivityList.add(new Activity(title, desc, tags, address, type));
            }

        });

        item.findViewById(R.id.cross_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    item.removeAllViews();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)item.getLayoutParams();
                params.height = 1;
                item.setLayoutParams(params);
                item.setVisibility(View.INVISIBLE);
            }
        });

        item.findViewById(R.id.Submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    item.removeAllViews();
                ActivityImageAdapter rcAdapter = new ActivityImageAdapter(updateListItemData(getListItemData()));
                recyclerView.setAdapter(rcAdapter);
            }
        });

    }

    private List<TripContent> getListItemData()
    {
        return ActivityList;
    }

    private List<TripContent> updateListItemData(List<TripContent> activities){

        List<TripContent> ActivityList = activities;

        final EditText t = (EditText)requireActivity().findViewById(R.id.Title_text);
        final EditText desc = (EditText)requireActivity().findViewById(R.id.Description_text);
//        final EditText tgs = (EditText)requireActivity().findViewById(R.id.);
        final EditText add = (EditText)requireActivity().findViewById(R.id.Place_text);
        final EditText tp = (EditText)requireActivity().findViewById(R.id.Type_text);

        String title = t.getText().toString();
        String description = desc.getText().toString();
        String tags = null;
        String address = add.getText().toString();
        String type = tp.getText().toString();

        ActivityList.add(new Activity(title, description, tags, address, type));

        return ActivityList;
    }

}