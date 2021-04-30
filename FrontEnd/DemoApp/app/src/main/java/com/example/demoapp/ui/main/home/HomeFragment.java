package com.example.demoapp.ui.main.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.viewmodel.ItemUpdate;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.util.ViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment
{
    private HomeViewModel homeViewModel;

    private RecyclerView searchResultList;
    private SearchResultAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(HomeViewModel.class);

        searchResultList = view.findViewById(R.id.Post_list);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        searchResultList.setLayoutManager(_sGridLayoutManager);

        adapter = new SearchResultAdapter();
        searchResultList.setAdapter(adapter);

        homeViewModel.getFeedResult().observe(getViewLifecycleOwner(), new Observer<List<Item>>()
        {
            @Override
            public void onChanged(List<Item> items)
            {
                adapter.setItems(items);
            }
        });

        homeViewModel.getProfileImageUpdate().observe(getViewLifecycleOwner(), new Observer<ItemUpdate>()
        {
            @Override
            public void onChanged(ItemUpdate itemUpdate)
            {
                Post p = (Post)adapter.getItems().get(itemUpdate.getPosition());
                p.setAccountImage(itemUpdate.getImage());
                adapter.notifyItemChanged(itemUpdate.getPosition());
            }
        });

        homeViewModel.getFeed();

        return view;
    }
}