package com.example.demoapp.ui.main.plan.moment;

import android.net.Uri;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddMomentViewModel extends ViewModel{

    private ContentRepository repository;

    private final LiveData<Iterable<String>> imagesLiveData;

    public AddMomentViewModel(ContentRepository repository)
    {
        this.repository = repository;

        imagesLiveData = Transformations.map(repository.getCurrentPost(), new Function<Post, Iterable<String>>()
        {
            @Override
            public Iterable<String> apply(Post post)
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
        List<String> paths = new ArrayList<>(images.size());
        images.forEach(uri -> paths.add(uri.toString()));
        repository.addImages(paths);
    }

    public void removeImage(int index)
    {
        repository.removeImages(index);
    }

    public LiveData<Iterable<String>> getImagesLiveData()
    {
        return imagesLiveData;
    }
}
