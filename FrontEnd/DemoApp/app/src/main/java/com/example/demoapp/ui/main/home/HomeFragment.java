package com.example.demoapp.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Notification;
import com.example.demoapp.ui.adapter.NotificationAdapter;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.LinkedList;
import java.util.List;


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

        notificationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getParentFragment().getContext()).inflate(R.layout.layout_nottification_sheet, view.findViewById(R.id.bottomSheetContainer));
                RecyclerView notificationResultList = bottomSheetView.findViewById(R.id.notification_list);
                NotificationAdapter not = new NotificationAdapter(getContext());

                StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                notificationResultList.setLayoutManager(_sGridLayoutManager);
                notificationResultList.setAdapter(notificationAdapter);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                notificationButton.setColorFilter(Color.rgb(255, 255, 255));
                notificationResultList.smoothScrollToPosition(0);
            }
        });

        viewModel.getFeedResult().observe(getViewLifecycleOwner(), new Observer<List<Item>>()
        {
            @Override
            public void onChanged(List<Item> items)
            {
                adapter.setItems(items);
            }
        });

        viewModel.getNotification().observe(getViewLifecycleOwner(), new Observer<Event<Notification>>()
        {
            @Override
            public void onChanged(Event<Notification> notification)
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
            }
        });

        viewModel.getFeed();

        return view;
    }
}