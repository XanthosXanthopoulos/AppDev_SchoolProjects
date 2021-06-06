
package com.example.demoapp.ui.main.followee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.actions.FollowActions;
import com.example.demoapp.ui.adapter.FollowListAdapter;
import com.example.demoapp.util.ViewModelFactory;

public class FolloweeFragment extends Fragment
{

    private FolloweeViewModel viewModel;

    private RecyclerView followList;
    private FollowListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(FolloweeViewModel.class);

        followList = view.findViewById(R.id.follow_list);

        adapter = new FollowListAdapter(getContext());

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        followList.setLayoutManager(_sGridLayoutManager);
        followList.setAdapter(adapter);

        adapter.setActions(new FollowActions()
        {
            @Override
            public void follow(String userID)
            {
                viewModel.sendFollowRequest(userID);
            }

            @Override
            public void accept(String userID) { }

            @Override
            public void decline(String userID) { }

            @Override
            public void unfollow(String userID)
            {
                viewModel.unfollow(userID);
            }

            @Override
            public void remove(String userID) { }

            @Override
            public void cancel(String userID)
            {
                viewModel.cancelFollowRequest(userID);
            }
        });

        viewModel.getFollowList().observe(getViewLifecycleOwner(), event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);
            adapter.setItems(event.getData());
        });

        viewModel.getFollows();

        return view;
    }
}