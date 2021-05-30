package com.example.demoapp.ui.main.plan.view.comments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Comment;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.ui.adapter.CommentAdapter;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.main.plan.view.activities.ActivityViewModel;
import com.example.demoapp.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment
{

    private CommentViewModel viewModel;

    private RecyclerView commentList;
    private CommentAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(CommentViewModel.class);

        commentList = view.findViewById(R.id.comment_list);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        commentList.setLayoutManager(_sGridLayoutManager);

        adapter = new CommentAdapter(getContext());
        commentList.setAdapter(adapter);

        viewModel.getCommentsResult().observe(getViewLifecycleOwner(), comments ->
        {
            List<Comment> items = new ArrayList<>();
            comments.forEach(items::add);
            adapter.setItems(items);
        });

        return view;
    }
}