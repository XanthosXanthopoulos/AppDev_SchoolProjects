package com.example.demoapp.ui.main.plan.view.comments;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Comment;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.LinkedList;

public class CommentViewModel extends ViewModel
{
    private final ContentRepository repository;
    private final LiveData<Iterable<Comment>> commentsResult;

    public CommentViewModel(ContentRepository repository)
    {
        this.repository = repository;

        commentsResult = Transformations.map(repository.getPostResult(), input ->
        {
            if (input.isSuccessful())
            {
                return input.getResponse().getComments();
            }
            else
            {
                return new LinkedList();
            }
        });
    }

    public LiveData<Iterable<Comment>> getCommentsResult()
    {
        return commentsResult;
    }
}