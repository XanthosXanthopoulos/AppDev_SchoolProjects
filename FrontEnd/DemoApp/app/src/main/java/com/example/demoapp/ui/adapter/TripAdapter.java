package com.example.demoapp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.demoapp.App;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.util.ApiRoutes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>
{
    public List<Post> getItems()
    {
        return items;
    }

    private List<Post> items;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private Context context;
    private SharedPreferences sharedPreferences;

    public TripAdapter(Context context)
    {
        this.items = new LinkedList<>();

        this.context = context;
        this.sharedPreferences = App.getInstance().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    public void setItems(List<Post> items)
    {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, null));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.title.setText(items.get(position).getTitle());
        holder.countries.setText(items.get(position).getCountrySummary());
        holder.likes.setText(String.valueOf(items.get(position).getLikes()));
        if (items.get(position).getDate().compareTo(new Date(0)) == 0)
        {
            holder.date.setText("");
        }
        else
        {
            holder.date.setText(formatter.format(items.get(position).getDate()));
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id", items.get(position).getThumbnailImageID());
        GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());

        Bundle bundle = new Bundle();
        bundle.putInt("PostID", items.get(position).getPostID());
        holder.image.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_ViewPlan, bundle));

        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
    }


    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView title;
        private final TextView countries;
        private final TextView likes;
        private final TextView date;
        private final ImageView image;

        public ViewHolder(View view)
        {
            super(view);

            image = itemView.findViewById(R.id.Image_area);
            title = itemView.findViewById(R.id.title);
            countries = itemView.findViewById(R.id.country_summary);
            likes = itemView.findViewById(R.id.likes_number);
            date = itemView.findViewById(R.id.Date_Description);
        }
    }
}
