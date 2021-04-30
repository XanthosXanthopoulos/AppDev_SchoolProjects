package com.example.demoapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Image;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.Trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public List<Item> getItems()
    {
        return items;
    }

    private List<Item> items;
    private SimpleDateFormat formatter;

    public SearchResultAdapter()
    {
        this.items = new ArrayList<>();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case 0:
                return new ActivityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, null));
            case 1:
                return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null));
            case 2:
                return new TripViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, null));
            case 3:
                return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (holder.getItemViewType())
        {
            case 0:
                ActivityViewHolder activityViewHolder = (ActivityViewHolder)holder;
                Activity activity = (Activity)items.get(position);

                activityViewHolder.title.setText(activity.getTitle());
                activityViewHolder.description.setText(activity.getDescription());
                activityViewHolder.address.setText(activity.getAddress());
                activityViewHolder.type.setText(activity.getType());
                activityViewHolder.tags.setText("Tags: " + activity.getTags());

                layoutParams.setFullSpan(true);
                break;

            case 1:
                ImageViewHolder imageViewHolder = (ImageViewHolder)holder;
                Image image = (Image) items.get(position);

                imageViewHolder.image.setImageBitmap(image.getImage());

                layoutParams.setFullSpan(true);
                break;
            case 2:
                TripViewHolder tripViewHolder = (TripViewHolder)holder;
                Trip trip = (Trip) items.get(position);

                tripViewHolder.description.setText(trip.getDescription());
                tripViewHolder.country.setText(trip.getCountry().toString());
                tripViewHolder.date.setText(formatter.format(trip.getDate()));

                if (trip.getUsername() == null) tripViewHolder.getUsername().setVisibility(GONE);
                break;
            case 3:
                PostViewHolder postViewHolder = (PostViewHolder)holder;
                Post post = (Post) items.get(position);

                postViewHolder.getUsername().setText(post.getUsername());
                postViewHolder.getAccountImage().setImageBitmap(post.getAccountImage());

                break;
        }

        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return items.get(position).getContentType().ordinal();
    }

    public void setItems(List<Item> items)
    {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView description;
        private TextView tags;
        private TextView address;
        private TextView type;

        public ActivityViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.Activity_Title);
            description = itemView.findViewById(R.id.Activity_Description);
            tags = itemView.findViewById(R.id.Activity_Tags);
            address = itemView.findViewById(R.id.Activity_Address);
            type = itemView.findViewById(R.id.Activity_Type);

            Button showMore = itemView.findViewById(R.id.Activity_ShowMore);
            Button showLess = itemView.findViewById(R.id.Activity_ShowLess);

            showMore.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showMore.setVisibility(View.GONE);

                    address.setVisibility(View.VISIBLE);
                    type.setVisibility(View.VISIBLE);
                    tags.setVisibility(View.VISIBLE);
                    showLess.setVisibility(View.VISIBLE);
                }
            });

            showLess.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showLess.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    type.setVisibility(View.GONE);
                    tags.setVisibility(View.GONE);

                    showMore.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView image;

        public ImageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            image = itemView.findViewById(R.id.image);
        }
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder
    {
        private TextView username;
        private TextView description;
        private TextView country;
        private TextView date;

        public TripViewHolder(View view)
        {
            super(view);

            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_trip));

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

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView accountImage;
        private TextView username;
        private ImageView planImage;
        private ImageButton approveButton;
        private ImageButton commentButton;

        public PostViewHolder(View view)
        {
            super(view);

            accountImage = (ImageView) view.findViewById(R.id.account_image);
            username =  view.findViewById(R.id.account_name);
            planImage = (ImageView) view.findViewById(R.id.plan_image);
            approveButton = (ImageButton) view.findViewById(R.id.approve_button);
            commentButton = (ImageButton) view.findViewById(R.id.comment_button);
        }

        public ImageView getAccountImage()
        {
            return accountImage;
        }

        public TextView getUsername()
        {
            return username;
        }

        public ImageView getPlanImage()
        {
            return planImage;
        }

        public ImageButton getApproveButton()
        {
            return approveButton;
        }

        public ImageButton getCommentButton()
        {
            return commentButton;
        }
    }
}
