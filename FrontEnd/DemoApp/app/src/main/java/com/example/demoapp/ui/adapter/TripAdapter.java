package com.example.demoapp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.toolbox.StringRequest;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Image;
import com.example.demoapp.data.model.Trip;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>
{
    private List<Trip> items;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public TripAdapter(List<Trip> itemList)
    {
        this.items = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, null);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(items.get(position).getImage() != null) holder.image.setImageBitmap(items.get(position).getImage().getImage());
        if(items.get(position).getDescription() != null) holder.description.setText(items.get(position).getDescription());
        else holder.getDescription().setVisibility(GONE);
        if(items.get(position).getCountry() != null) holder.country.setText(items.get(position).getCountry().toString());
        else holder.getCountry().setVisibility(GONE);
        if(items.get(position).getDate() != null) holder.date.setText(formatter.format(items.get(position).getDate()));
        else holder.getDate().setVisibility(GONE);

        if (items.get(position).getUsername() == null) holder.getUsername().setVisibility(GONE);

//        if (new Random().nextDouble() > 0.5)
//        {
//            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setFullSpan(true);
//            holder.itemView.setLayoutParams(layoutParams);
//        }

    }


    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView username;
        private TextView description;
        private TextView country;
        private TextView date;
        private ImageView image;

        public ViewHolder(View view)
        {
            super(view);

            image = (ImageView) itemView.findViewById(R.id.Image_area);
            username = (TextView) itemView.findViewById(R.id.Trip_Username);
            description = (TextView) itemView.findViewById(R.id.Description);
            country = (TextView) itemView.findViewById(R.id.Country_Description);
            date = (TextView) itemView.findViewById(R.id.Date_Description);
        }

        public TextView getDescription()
        {
            return description;
        }

        public TextView getCountry()
        {
            return country;
        }

        public TextView getDate()
        {
            return date;
        }

        public TextView getUsername()
        {
            return username;
        }
    }
}
