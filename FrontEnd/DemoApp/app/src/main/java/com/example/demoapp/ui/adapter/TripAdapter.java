package com.example.demoapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Trip;

import java.text.SimpleDateFormat;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>
{
    private List<Trip> items;
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
        holder.description.setText(items.get(position).getDescription());
        holder.country.setText(items.get(position).getCountry().toString());
        holder.date.setText(formatter.format(items.get(position).getDate()));
    }


    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView description;
        private TextView country;
        private TextView date;

        public ViewHolder(View view)
        {
            super(view);

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
    }
}
