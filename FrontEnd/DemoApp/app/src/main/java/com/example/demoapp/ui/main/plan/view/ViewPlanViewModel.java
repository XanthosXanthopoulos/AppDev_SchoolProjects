package com.example.demoapp.ui.main.plan.view;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

public class ViewPlanViewModel extends ViewModel
{
    private ContentRepository repository;
    private final LiveData<Post> postData;

    public ViewPlanViewModel(ContentRepository repository)
    {
        this.repository = repository;

        postData = Transformations.map(repository.getPostResult(), new Function<RepositoryResponse<Post>, Post>()
        {
            @Override
            public Post apply(RepositoryResponse<Post> input)
            {
                if (input.isSuccessful())
                {
                    return input.getResponse();
                }
                else
                {
                    return null;
                }
            }
        });
    }

    public void loadPost(int postID)
    {
        repository.loadPost(postID);
    }

    public LiveData<Post> getPostLiveData()
    {
        return postData;
    }
}
