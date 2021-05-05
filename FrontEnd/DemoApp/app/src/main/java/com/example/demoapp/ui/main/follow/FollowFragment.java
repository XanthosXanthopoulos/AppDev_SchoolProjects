
package com.example.demoapp.ui.main.follow;

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
import com.example.demoapp.actions.FollowActions;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.FragmentContent;
import com.example.demoapp.data.model.Status;
import com.example.demoapp.ui.adapter.FollowListAdapter;
import com.example.demoapp.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class FollowFragment extends Fragment
{

    private FollowViewModel viewModel;

    private RecyclerView followList;
    private FollowListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        FragmentContent content = (FragmentContent) getArguments().getSerializable("contentType");

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(FollowViewModel.class);

        followList = view.findViewById(R.id.follow_list);
        adapter = new FollowListAdapter(getContext());

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        followList.setLayoutManager(_sGridLayoutManager);
        followList.setAdapter(adapter);
        adapter.setItems(getItems());

        adapter.setActions(new FollowActions()
        {
            @Override
            public void follow(String userID)
            {

            }

            @Override
            public void accept(String userID)
            {

            }

            @Override
            public void decline(String userID)
            {

            }

            @Override
            public void unfollow(String userID)
            {

            }

            @Override
            public void remove(String userID)
            {

            }

            @Override
            public void cancel(String userID)
            {

            }
        });

        return view;
    }

    private List<Follow> getItems()
    {
        List<Follow> items = new ArrayList<>();

        for (int i = 0; i < 10; ++i)
        {
            items.add(new Follow("", "", "User " + i, Status.FOLLOWING));
        }

        return items;
    }
}