package com.example.demoapp.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.demoapp.App;
import com.example.demoapp.R;
import com.example.demoapp.data.model.Comment;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.util.ApiRoutes;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
{
    public List<Comment> getItems()
    {
        return items;
    }

    private List<Comment> items;
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public CommentAdapter(Context context)
    {
        this.items = new LinkedList<>();
        this.context = context;
        this.sharedPreferences = App.getInstance().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    @NonNull
    @NotNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, null);

        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", items.get(position).getProfileImageID());
        GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());

        Glide.with(this.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileImage);

        holder.username.setText(items.get(position).getUsername());
        holder.comment.setText(items.get(position).getContent());
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void setItems(List<Comment> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView profileImage;
        private final TextView username;
        private final TextView comment;

        public ViewHolder(View view)
        {
            super(view);
            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
