
package com.example.demoapp.ui.main.follower;

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

public class FollowerFragment extends Fragment
{

    private FollowerViewModel viewModel;

    private RecyclerView followList;
    private FollowListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(FollowerViewModel.class);

        followList = view.findViewById(R.id.follow_list);
        adapter = new FollowListAdapter(getContext());

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        followList.setLayoutManager(_sGridLayoutManager);
        followList.setAdapter(adapter);

        adapter.setActions(new FollowActions()
        {
            @Override
            public void follow(String userID) { }

            @Override
            public void accept(String userID)
            {
                viewModel.acceptFollowRequest(userID);
            }

            @Override
            public void decline(String userID)
            {
                viewModel.declineFollowRequest(userID);
            }

            @Override
            public void unfollow(String userID) { }

            @Override
            public void remove(String userID)
            {
                viewModel.removeFollower(userID);
            }

            @Override
            public void cancel(String userID) { }
        });

        viewModel.getFollowersList().observe(getViewLifecycleOwner(), event ->
        {
            if (event.isHandled()) return;

            event.setHandled(true);
            adapter.setItems(event.getData());
        });

        viewModel.getFollowers();

        return view;
    }
}