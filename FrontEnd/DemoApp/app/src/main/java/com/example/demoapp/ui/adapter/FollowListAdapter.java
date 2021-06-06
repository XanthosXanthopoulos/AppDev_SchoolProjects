package com.example.demoapp.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.demoapp.R;
import com.example.demoapp.actions.FollowActions;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.Status;
import com.example.demoapp.util.ApiRoutes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder>
{
    private List<Follow> items;
    private Context context;
    private SharedPreferences sharedPreferences;

    private FollowActions actions;

    public FollowListAdapter(Context context)
    {
        this.context = context;

        items = new ArrayList<>();
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, null);

        return new FollowListAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", items.get(position).getProfileImageID());
        GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());

        holder.username.setText(items.get(position).getUsername());
        Glide.with(this.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileImage);

        switch (items.get(position).getStatus())
        {
            case NONE:
                holder.actionButton.setText("Follow");
                holder.deleteButton.setVisibility(View.GONE);
                break;
            case FOLLOWED:
            case FOLLOWING:
                holder.actionButton.setText("Remove");
                holder.deleteButton.setVisibility(View.GONE);
                break;
            case PENDING_INCOMING:
                holder.actionButton.setText("Accept");
                holder.deleteButton.setVisibility(View.VISIBLE);
                break;
            case PENDING_OUTCOMING:
                holder.actionButton.setText("Cancel");
                holder.deleteButton.setVisibility(View.GONE);
                break;
        }

        holder.deleteButton.setOnClickListener(v ->
        {
            switch (items.get(position).getStatus())
            {
                case PENDING_INCOMING:
                    actions.decline(items.get(position).getUserID());
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    break;
            }
        });

        holder.actionButton.setOnClickListener(v ->
        {
            Button button = (Button)v;

            switch (items.get(position).getStatus())
            {
                case NONE:
                    button.setText("Cancel");
                    items.get(position).setStatus(Status.PENDING_OUTCOMING);
                    actions.follow(items.get(position).getUserID());
                    break;
                case FOLLOWED:
                    actions.remove(items.get(position).getUserID());
                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    break;
                case FOLLOWING:
                    button.setText("Follow");
                    items.get(position).setStatus(Status.NONE);
                    actions.unfollow(items.get(position).getUserID());
                    break;
                case PENDING_INCOMING:
                    button.setText("Remove");
                    items.get(position).setStatus(Status.FOLLOWED);
                    actions.accept(items.get(position).getUserID());
                    holder.deleteButton.setVisibility(View.GONE);
                    break;
                case PENDING_OUTCOMING:
                    button.setText("Follow");
                    items.get(position).setStatus(Status.NONE);
                    actions.cancel(items.get(position).getUserID());
                    break;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void setActions(FollowActions actions)
    {
        this.actions = actions;
    }

    public void setItems(List<Follow> items)
    {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView profileImage;
        private TextView username;
        private Button actionButton;
        private ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            profileImage = itemView.findViewById(R.id.account_image);
            username = itemView.findViewById(R.id.username);
            actionButton = itemView.findViewById(R.id.action_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
