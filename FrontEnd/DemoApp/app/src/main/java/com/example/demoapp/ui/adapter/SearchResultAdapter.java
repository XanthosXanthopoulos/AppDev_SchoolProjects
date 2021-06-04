package com.example.demoapp.ui.adapter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.demoapp.App;
import com.example.demoapp.R;
import com.example.demoapp.actions.FollowActions;
import com.example.demoapp.data.ActivityItemClickListener;
import com.example.demoapp.data.CommentLikeClickListener;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.Image;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.Status;
import com.example.demoapp.util.ApiRoutes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.demoapp.App.SHARED_PREFS;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public List<Item> getItems()
    {
        return items;
    }

    private final List<Item> items;
    private final SimpleDateFormat formatter;
    private final SharedPreferences sharedPreferences;
    private FollowActions actions;
    private CommentLikeClickListener commentLikeClickListener;
    private ActivityItemClickListener itemClickListener;

    public SearchResultAdapter()
    {
        this.items = new ArrayList<>();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.sharedPreferences = App.getInstance().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
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
                return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, null));
            case  3:
                return new UserViewHolder((LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, null)));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        HashMap<String, String> params = new HashMap<>();
        GlideUrl url;

        switch (holder.getItemViewType())
        {
            case 0:
                ActivityViewHolder activityViewHolder = (ActivityViewHolder)holder;
                Activity activity = (Activity)items.get(position);

                activityViewHolder.title.setText(activity.getTitle());
                activityViewHolder.description.setText(activity.getDescription());
                activityViewHolder.address.setText(activity.getAddress() + ", " + activity.getCity() + ", " + activity.getCountry().label);
                activityViewHolder.tags.setText("Tags: " + activity.getTags());

                if (itemClickListener != null)
                {
                    activityViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            itemClickListener.onItemClickListener(activity);
                        }
                    });
                }

                layoutParams.setFullSpan(true);
                break;

            case 1:
                ImageViewHolder imageViewHolder = (ImageViewHolder)holder;
                Image image = (Image) items.get(position);

                imageViewHolder.image.setImageBitmap(image.getImage());

                layoutParams.setFullSpan(true);
                break;
            case 2:
                PostViewHolder postViewHolder = (PostViewHolder)holder;
                Post post = (Post) items.get(position);

                postViewHolder.username.setText(post.getUsername());
                postViewHolder.title.setText(post.getTitle());


                params.put("id", post.getProfileImageID());
                url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());
                Glide.with(App.getInstance()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(postViewHolder.accountImage);

                params.put("id", post.getThumbnailImageID());
                url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());
                Glide.with(App.getInstance()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(postViewHolder.planImage);

                Bundle bundle = new Bundle();
                bundle.putInt("PostID", post.getPostID());
                postViewHolder.planImage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_ViewPlan, bundle));

                postViewHolder.showOnMapButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_map, bundle));

                postViewHolder.commentButton.setOnClickListener(v ->
                        postViewHolder.commentSection.setVisibility(postViewHolder.commentSection.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));

                if (post.isLiked())
                {
                    postViewHolder.approveButton.setColorFilter(Color.rgb(0, 153, 255));
                }
                else
                {
                    postViewHolder.approveButton.setOnClickListener(v ->
                    {
                        commentLikeClickListener.sendLike(post.getPostID());
                        postViewHolder.approveButton.setColorFilter(Color.rgb(0, 153, 255));
                    });
                }

                postViewHolder.sendComment.setOnClickListener(v ->
                {
                    if (postViewHolder.comment.getText().toString().isEmpty()) return;

                    commentLikeClickListener.sendComment(post.getPostID(), postViewHolder.comment.getText().toString());

                    Toast.makeText(v.getContext(), "Comment sent", Toast.LENGTH_LONG).show();
                    postViewHolder.commentSection.setVisibility(View.GONE);
                });

                layoutParams.setFullSpan(true);
                break;

            case 3:
                UserViewHolder userViewHolder = (UserViewHolder)holder;
                Follow follow = (Follow) items.get(position);

                userViewHolder.username.setText(follow.getUsername());

                params.put("id", follow.getProfileImageID());
                url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());

                Glide.with(App.getInstance()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(userViewHolder.profileImage);
                userViewHolder.deleteButton.setVisibility(View.GONE);

                switch (follow.getStatus())
                {
                    case NONE:
                        userViewHolder.actionButton.setText("Follow");
                        userViewHolder.actionButton.setOnClickListener(v ->
                        {
                            actions.follow(follow.getUserID());
                            follow.setStatus(Status.PENDING_OUTCOMING);
                            notifyItemChanged(position);
                        });
                        break;
                    case FOLLOWING:
                        userViewHolder.actionButton.setText("Unfollow");
                        userViewHolder.actionButton.setOnClickListener(v ->
                        {
                            actions.unfollow(follow.getUserID());
                            follow.setStatus(Status.NONE);
                            notifyItemChanged(position);
                        });
                        break;
                    case PENDING_OUTCOMING:
                        userViewHolder.actionButton.setText("Cancel");
                        userViewHolder.actionButton.setOnClickListener(v ->
                        {
                            actions.cancel(follow.getUserID());
                            follow.setStatus(Status.NONE);
                            notifyItemChanged(position);
                        });
                        break;
                }

                layoutParams.setFullSpan(true);
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

    public void setActions(FollowActions actions)
    {
        this.actions = actions;
    }

    public void setItemClickListener(ActivityItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setCommentLikeClickListener(CommentLikeClickListener commentLikeClickListener)
    {
        this.commentLikeClickListener = commentLikeClickListener;
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView title;
        private final TextView description;
        private final TextView tags;
        private final TextView address;

        public ActivityViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.Activity_Title);
            description = itemView.findViewById(R.id.Activity_Description);
            tags = itemView.findViewById(R.id.Activity_Tags);
            address = itemView.findViewById(R.id.Activity_Address);

            Button showMore = itemView.findViewById(R.id.Activity_ShowMore);
            Button showLess = itemView.findViewById(R.id.Activity_ShowLess);

            showMore.setOnClickListener(v ->
            {
                showMore.setVisibility(View.GONE);

                address.setVisibility(View.VISIBLE);
                tags.setVisibility(View.VISIBLE);
                showLess.setVisibility(View.VISIBLE);
            });

            showLess.setOnClickListener(v ->
            {
                showLess.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
                tags.setVisibility(View.GONE);

                showMore.setVisibility(View.VISIBLE);
            });
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView image;

        public ImageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            image = itemView.findViewById(R.id.image);
        }
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView accountImage;
        private final TextView username;
        private final ImageView planImage;
        private final ImageButton approveButton;
        private final ImageButton commentButton;
        private final ImageButton showOnMapButton;
        private final TextView title;
        private final LinearLayout commentSection;
        private final EditText comment;
        private final ImageButton sendComment;

        public PostViewHolder(View view)
        {
            super(view);

            accountImage = view.findViewById(R.id.account_image);
            username =  view.findViewById(R.id.account_name);
            planImage = view.findViewById(R.id.plan_image);
            approveButton = view.findViewById(R.id.approve_button);
            commentButton = view.findViewById(R.id.comment_button);
            showOnMapButton = view.findViewById(R.id.show_to_map);
            title = view.findViewById(R.id.title);
            commentSection = view.findViewById(R.id.comment_section);
            comment = view.findViewById(R.id.comment);
            sendComment = view.findViewById(R.id.send_comment);
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView profileImage;
        private final TextView username;
        private final Button actionButton;
        private final ImageButton deleteButton;

        public UserViewHolder(@NonNull View itemView)
        {
            super(itemView);

            profileImage = itemView.findViewById(R.id.account_image);
            username = itemView.findViewById(R.id.username);
            actionButton = itemView.findViewById(R.id.action_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
