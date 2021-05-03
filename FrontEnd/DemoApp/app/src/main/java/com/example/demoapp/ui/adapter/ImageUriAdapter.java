package com.example.demoapp.ui.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demoapp.R;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.List;


public class ImageUriAdapter extends RecyclerView.Adapter<ImageUriAdapter.ViewHolder>
{
    private List<Uri> items;
    private Context context;

    public ImageUriAdapter(Context context)
    {
        this.items = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null);

        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Glide.with(this.context).load(items.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
    }


    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void setItems(List<Uri> items)
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
