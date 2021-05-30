package com.example.demoapp.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.data.CommentLikeClickListener;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.ui.adapter.NotificationAdapter;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.LinkedList;


public class HomeFragment extends Fragment
{
    private HomeViewModel viewModel;

    private RecyclerView searchResultList;
    private SearchResultAdapter adapter;
    private ImageButton notificationButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(HomeViewModel.class);

        searchResultList = view.findViewById(R.id.Post_list);
        notificationButton = view.findViewById(R.id.notification_button);
        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext());
        notificationAdapter.setItems((LinkedList<Notification>) viewModel.getNotifications().clone());

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        searchResultList.setLayoutManager(_sGridLayoutManager);

        adapter = new SearchResultAdapter();
        searchResultList.setAdapter(adapter);
        adapter.setCommentLikeClickListener(new CommentLikeClickListener()
        {
            @Override
            public void sendComment(int postID, String content)
            {
                viewModel.sendComment(postID, content);
            }

            @Override
            public void sendLike(int postID)
            {
                viewModel.sendLike(postID);
            }
        });

        notificationButton.setOnClickListener(v ->
        {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getParentFragment().getContext()).inflate(R.layout.layout_nottification_sheet, view.findViewById(R.id.bottomSheetContainer));
            RecyclerView notificationResultList = bottomSheetView.findViewById(R.id.notification_list);

            StaggeredGridLayoutManager _sGridLayoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            notificationResultList.setLayoutManager(_sGridLayoutManager1);
            notificationResultList.setAdapter(notificationAdapter);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

            notificationButton.setColorFilter(Color.rgb(255, 255, 255));
            notificationResultList.smoothScrollToPosition(0);
        });

        viewModel.getFeedResult().observe(getViewLifecycleOwner(), items -> adapter.setItems(items));

        viewModel.getNotification().observe(getViewLifecycleOwner(), notification ->
        {
            if (!notification.isHandled())
            {
                if (!notificationAdapter.getItems().isEmpty() && notificationAdapter.getItems().getFirst().equals(notification.getData()))
                {
                    notification.setHandled(true);
                    return;
                }

                notificationAdapter.appendNotification(notification.getData());
                notification.setHandled(true);
                notificationButton.setColorFilter(Color.rgb(200, 120, 150));
            }
        });

        viewModel.getFeed();

        return view;
    }
}