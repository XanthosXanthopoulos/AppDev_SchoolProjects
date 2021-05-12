package com.example.demoapp.ui.main.plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.repository.ContentRepository;

public class ViewPlanViewModel extends ViewModel
{
    private ContentRepository repository;

    public ViewPlanViewModel(ContentRepository repository)
    {
        this.repository = repository;
    }

    public void loadPost(int postID)
    {
        repository.loadPost(postID);
    }

    public LiveData<Post> getPostLiveData()
    {
        return repository.getCurrentPost();
    }
}
