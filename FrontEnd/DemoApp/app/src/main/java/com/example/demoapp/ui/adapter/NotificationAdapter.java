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
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.util.ApiRoutes;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
{
    public LinkedList<Notification> getItems()
    {
        return items;
    }

    private LinkedList<Notification> items;
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public NotificationAdapter(Context context)
    {
        this.items = new LinkedList<>();
        this.context = context;
        this.sharedPreferences = App.getInstance().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    @NonNull
    @NotNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, null);

        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", items.get(position).getUserImageID());
        GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());

        Glide.with(this.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileImage);

        holder.message.setText(items.get(position).toString());
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void appendNotification(Notification notification)
    {
        items.addFirst(notification);
        notifyItemInserted(0);
    }

    public void setItems(LinkedList<Notification> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView profileImage;
        private final TextView message;

        public ViewHolder(View view)
        {
            super(view);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            message = itemView.findViewById(R.id.message);

        }
    }
}
