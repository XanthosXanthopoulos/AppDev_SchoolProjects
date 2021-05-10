package com.example.demoapp.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.util.ViewModelFactory;

import java.util.List;


public class HomeFragment extends Fragment
{
    private HomeViewModel viewModel;

    private RecyclerView searchResultList;
    private SearchResultAdapter adapter;
    private ImageButton notificationButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(HomeViewModel.class);

        searchResultList = view.findViewById(R.id.Post_list);
        notificationButton = view.findViewById(R.id.notification_button);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        searchResultList.setLayoutManager(_sGridLayoutManager);

        adapter = new SearchResultAdapter();
        searchResultList.setAdapter(adapter);

        viewModel.getFeedResult().observe(getViewLifecycleOwner(), new Observer<List<Item>>()
        {
            @Override
            public void onChanged(List<Item> items)
            {
                adapter.setItems(items);
            }
        });

        viewModel.getNotifications().observe(getViewLifecycleOwner(), new Observer<List<Notification>>()
        {
            @Override
            public void onChanged(List<Notification> notifications)
            {
                if (!notifications.isEmpty()) notificationButton.setColorFilter(Color.rgb(200, 120, 150));
            }
        });

        viewModel.getFeed();

        return view;
    }
}