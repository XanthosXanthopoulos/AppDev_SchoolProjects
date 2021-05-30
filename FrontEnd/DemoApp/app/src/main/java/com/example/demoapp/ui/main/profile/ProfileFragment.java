package com.example.demoapp.ui.main.profile;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.ui.adapter.TripAdapter;
import com.example.demoapp.util.ApiRoutes;
import com.example.demoapp.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;

public class ProfileFragment extends Fragment
{

    private ProfileViewModel viewModel;

    private TextView followsTextView;
    private TextView followersTextView;
    private ImageView profileImage;

    private TripAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(ProfileViewModel.class);

        followsTextView = view.findViewById(R.id.follows);
        followersTextView = view.findViewById(R.id.followers);
        profileImage = view.findViewById(R.id.Register_ProfileImage);
        adapter = new TripAdapter(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.Trip_List);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        recyclerView.setAdapter(adapter);


        followsTextView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_followee));

        followersTextView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_follower));

        profileImage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_account));
        view.findViewById(R.id.Create_Plan).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_CreatePlan));

        HashMap<String, String> params = new HashMap<>();
        params.put("id", viewModel.getProfileImage());
        GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString("JWToken", "")).build());

        Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);

        viewModel.getFeedResult().observe(getViewLifecycleOwner(), items ->
        {
            List<Post> posts = new ArrayList<>(items.size());
            items.forEach(item -> posts.add((Post)item));
            adapter.setItems(posts);
        });

        viewModel.getFeed();

        return view;
    }
}