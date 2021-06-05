package com.example.demoapp.ui.main.plan.memory;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.main.plan.memory.create.CreateMemoryActivity;
import com.example.demoapp.ui.main.search.SearchActivity;
import com.example.demoapp.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddMemoryFragment extends Fragment
{
    private static final int ACTIVITY_ADD = 0;
    private static final int ACTIVITY_SELECT = 1;

    private AddMemoryViewModel viewModel;

    private RecyclerView activityList;
    private SearchResultAdapter adapter;

    private Button createMemory;
    private Button findMemory;

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
        findMemory = view.findViewById(R.id.find_memory_button);
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
                List<Item> items = new ArrayList<>();
                activities.forEach(items::add);
                adapter.setItems(items);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                viewModel.removeMemory(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(activityList);

        createMemory.setOnClickListener(v -> startActivityForResult(new Intent(getActivity(),  CreateMemoryActivity.class), ACTIVITY_ADD));

        findMemory.setOnClickListener(v -> startActivityForResult(new Intent(getActivity(),  SearchActivity.class), ACTIVITY_SELECT));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_ADD && resultCode == RESULT_OK && data != null)
        {
            String title = data.getStringExtra("title");
            Country country = (Country) data.getSerializableExtra("country");
            String city = data.getStringExtra("city");
            String address = data.getStringExtra("address");
            String description = data.getStringExtra("description");
            String tags = data.getStringExtra("tags");
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);

            viewModel.addMemory("", title, country, city, address, description, tags, latitude, longitude);
        }
        else if (requestCode == ACTIVITY_SELECT && resultCode == RESULT_OK && data != null)
        {
            String id = data.getStringExtra("id");
            String title = data.getStringExtra("title");
            Country country = (Country) data.getSerializableExtra("country");
            String city = data.getStringExtra("city");
            String address = data.getStringExtra("address");
            String description = data.getStringExtra("description");
            String tags = data.getStringExtra("tags");

            viewModel.addMemory(id, title, country, city, address, description, tags);
        }
    }
}