package com.example.demoapp.ui.main.plan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.ui.adapter.ImageUriAdapter;
import com.example.demoapp.ui.adapter.ImageUrlAdapter;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.util.ApiRoutes;
import com.example.demoapp.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;

public class ViewPlanFragment extends Fragment
{
    private ViewPlanViewModel viewModel;

    private RecyclerView activityList;
    private SearchResultAdapter adapter;
    private ViewPager2 viewPager2;
    private ImageUrlAdapter slideshowAdapter;
    private TextView nameTextView;
    private ImageView profileImage;
    private ImageButton showToMapButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_view_plan, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(ViewPlanViewModel.class);

        activityList = view.findViewById(R.id.activity_list);
        viewPager2 = view.findViewById(R.id.post_images);
        profileImage = view.findViewById(R.id.account_image);
        nameTextView = view.findViewById(R.id.account_name);
        showToMapButton = view.findViewById(R.id.show_to_map);


        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        activityList.setLayoutManager(_sGridLayoutManager);

        adapter = new SearchResultAdapter();
        activityList.setAdapter(adapter);

        slideshowAdapter = new ImageUrlAdapter(getContext());

        viewPager2.setAdapter(slideshowAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer()
        {
            @Override
            public void transformPage(@NonNull View page, float position)
            {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85F + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                sliderHanlder.removeCallbacks(sliderRunnable);
//                sliderHanlder.postDelayed(sliderRunnable,3000); // Slider duration 3 seconds
//            }
//        });

        viewModel.getPostLiveData().observe(getViewLifecycleOwner(), new Observer<Post>()
        {
            @Override
            public void onChanged(Post post)
            {
                if (post == null) return;

                adapter.setItems(new ArrayList<>(post.getActivities()));
                slideshowAdapter.setItems(new ArrayList<>(post.getImages()));

                nameTextView.setText(post.getUsername());

                HashMap<String, String> params = new HashMap<>();
                params.put("id", post.getProfileImageID());
                GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString("JWToken", "")).build());

                Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
            }
        });



        viewModel.loadPost(getArguments().getInt("PostID"));

        return view;
    }
}