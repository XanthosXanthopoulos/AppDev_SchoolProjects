package com.example.demoapp.ui.main.plan.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import com.example.demoapp.ui.adapter.ImageUrlAdapter;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.adapter.CreatePlanViewPagerAdapter;
import com.example.demoapp.ui.adapter.ViewPlanViewPagerAdapter;
import com.example.demoapp.util.ApiRoutes;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;

public class ViewPlanFragment extends Fragment
{
    private ViewPlanViewModel viewModel;

    private ViewPager2 imagesCarousel;
    private ImageUrlAdapter slideshowAdapter;
    private TextView nameTextView;
    private ImageView profileImage;
    private ImageButton showToMapButton;
    private TextView title;
    private TextView description;

    private TabLayout tabLayout;
    private ViewPager2 content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_view_plan, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(ViewPlanViewModel.class);

        imagesCarousel = view.findViewById(R.id.post_images);
        profileImage = view.findViewById(R.id.account_image);
        nameTextView = view.findViewById(R.id.account_name);
        showToMapButton = view.findViewById(R.id.show_to_map);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);

        tabLayout = view.findViewById(R.id.tab_layout);
        content = view.findViewById(R.id.view_pager);
        content.setAdapter(new ViewPlanViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, content, (tab, position) -> tab.setText(new String[]{"Memories", "Comments"}[position])).attach();


        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        slideshowAdapter = new ImageUrlAdapter(getContext());

        imagesCarousel.setAdapter(slideshowAdapter);
        imagesCarousel.setClipToPadding(false);
        imagesCarousel.setClipChildren(false);
        imagesCarousel.setOffscreenPageLimit(3);
        imagesCarousel.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) ->
        {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85F + r * 0.15f);
        });

        imagesCarousel.setPageTransformer(compositePageTransformer);
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                sliderHanlder.removeCallbacks(sliderRunnable);
//                sliderHanlder.postDelayed(sliderRunnable,3000); // Slider duration 3 seconds
//            }
//        });

        viewModel.getPostLiveData().observe(getViewLifecycleOwner(), post ->
        {
            if (post == null) return;

            slideshowAdapter.setItems(new ArrayList<>(post.getImages()));
            title.setText(post.getTitle());
            if (post.getDescription().isEmpty()) description.setVisibility(View.GONE);
            else description.setText(post.getDescription());

            nameTextView.setText(post.getUsername());

            HashMap<String, String> params = new HashMap<>();
            params.put("id", post.getProfileImageID());
            GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString("JWToken", "")).build());

            Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
        });

        showToMapButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_map, getArguments()));

        viewModel.loadPost(getArguments().getInt("PostID"));

        return view;
    }
}