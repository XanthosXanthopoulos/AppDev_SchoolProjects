package com.example.demoapp.ui.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.demoapp.App;
import com.example.demoapp.R;
import com.example.demoapp.util.ApiRoutes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;


public class ImageUrlAdapter extends RecyclerView.Adapter<ImageUrlAdapter.ViewHolder>
{
    private List<String> items;
    private Context context;
    private SharedPreferences sharedPreferences;

    public ImageUrlAdapter(Context context)
    {
        this.items = new ArrayList<>();
        this.context = context;
        this.sharedPreferences = App.getInstance().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);

        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", items.get(position));
        GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());

        Glide.with(this.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
    }


    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void setItems(List<String> items)
    {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView image;

        public ViewHolder(View view)
        {
            super(view);

            image = itemView.findViewById(R.id.image);
        }
    }
}
