package com.example.demoapp.ui.main.plan.memory.create;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateMemoryFragment extends Fragment
{
    RecyclerView recyclerView;
    List<Item> ActivityList = new ArrayList<>() ;

    private CreateMemoryViewModel mViewModel;

    public static CreateMemoryFragment newInstance()
    {
        return new CreateMemoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_memory, container, false);

        recyclerView = view.findViewById(R.id.memory_list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.add_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityImageAdapter rcAdapter = new ActivityImageAdapter(updateListItemData(getListItemData()));
                recyclerView.setAdapter(rcAdapter);
//                item.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreateMemoryViewModel.class);
        // TODO: Use the ViewModel
    }

    private List<Item> getListItemData()
    {
        return ActivityList;
    }

    private List<Item> updateListItemData(List<Item> activities){

        List<Item> ActivityList = activities;

        final EditText t = (EditText)requireActivity().findViewById(R.id.activity_title);
        final EditText desc = (EditText)requireActivity().findViewById(R.id.activity_description);
        final EditText tgs = (EditText)requireActivity().findViewById(R.id.activity_tags);
        final EditText add = (EditText)requireActivity().findViewById(R.id.activity_address);
        final Spinner tp = (Spinner)requireActivity().findViewById(R.id.activity_type);

        String title = t.getText().toString();
        String description = desc.getText().toString();
        String tags = tgs.getText().toString();
        String address = add.getText().toString();
//        String type = tp.getSelectedItem().toString();

        ActivityList.add(new Activity(title, description, tags, address, null));

        return ActivityList;
    }

<<<<<<< Updated upstream
=======
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println(LocationActivity.getLocation());
    }
>>>>>>> Stashed changes
}