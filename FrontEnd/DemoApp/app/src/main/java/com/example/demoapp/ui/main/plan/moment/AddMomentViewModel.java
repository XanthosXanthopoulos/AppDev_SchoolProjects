package com.example.demoapp.ui.main.plan.moment;

import android.net.Uri;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.LinkedList;
import java.util.List;

public class AddMomentViewModel extends ViewModel{

    private ContentRepository repository;

    private final LiveData<Iterable<Uri>> imagesLiveData;

    public AddMomentViewModel(ContentRepository repository)
    {
        this.repository = repository;

        imagesLiveData = Transformations.map(repository.getCurrentPost(), new Function<Post, Iterable<Uri>>()
        {
            @Override
            public Iterable<Uri> apply(Post post)
            {
                if (post == null)
                {
                    return new LinkedList<>();
                }
                else
                {
                    return post.getImages();
                }
            }
        });
    }

    public void addImages(List<Uri> images)
    {
        repository.addImages(images);
    }

    public LiveData<Iterable<Uri>> getImagesLiveData()
    {
        return imagesLiveData;
    }
}
