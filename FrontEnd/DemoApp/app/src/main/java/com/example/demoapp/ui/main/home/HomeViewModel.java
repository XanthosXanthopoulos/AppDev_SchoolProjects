package com.example.demoapp.ui.main.home;

import android.graphics.Bitmap;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;
import com.example.demoapp.data.viewmodel.ItemUpdate;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel
{
    private ContentRepository repository;
    private List<Item> feed;
    private final LiveData<List<Item>> feedResult;
    private final LiveData<ItemUpdate> profileImageUpdate;
    private final LiveData<ItemUpdate> planImageUpdate;
    private int profilePosition = 0;

    public HomeViewModel(ContentRepository repository)
    {
        this.repository = repository;

        feedResult = Transformations.map(repository.getFeedResult(), new Function<RepositoryResponse<List<Item>>, List<Item>>()
        {
            @Override
            public List<Item> apply(RepositoryResponse<List<Item>> input)
            {
                if (input.isSuccessful())
                {
                    feed = input.getResponse();
                    profilePosition = 0;
                    loadProfileImage();
                    //loadPlanImage();
                    return input.getResponse();
                }
                else
                {
                    return new ArrayList<>();
                }
            }
        });

        profileImageUpdate = Transformations.map(repository.getProfileResult(), new Function<RepositoryResponse<ItemUpdate>, ItemUpdate>()
        {
            @Override
            public ItemUpdate apply(RepositoryResponse<ItemUpdate> input)
            {
                if (input.isSuccessful())
                {
                    loadProfileImage();
                    return input.getResponse();
                }
                else
                {
                    return null;
                }
            }
        });

        planImageUpdate = Transformations.map(repository.getPlanResult(), new Function<RepositoryResponse<Bitmap>, ItemUpdate>()
        {
            @Override
            public ItemUpdate apply(RepositoryResponse<Bitmap> input)
            {
                if (input.isSuccessful())
                {
                    loadPlanImage();
                    return new ItemUpdate(0, input.getResponse());
                }
                else
                {
                    return null;
                }
            }
        });
    }

    public void getFeed()
    {
        repository.updateFeed();
    }

    private void loadProfileImage()
    {
        if (profilePosition < feed.size())
        {
            repository.getProfileImage(((Post)feed.get(profilePosition)).getProfileImageID(), profilePosition);
            ++profilePosition;
        }
    }

    private void loadPlanImage()
    {

    }

    public LiveData<List<Item>> getFeedResult()
    {
        return feedResult;
    }

    public LiveData<ItemUpdate> getProfileImageUpdate()
    {
        return profileImageUpdate;
    }

    public LiveData<ItemUpdate> getPlanImageUpdate()
    {
        return planImageUpdate;
    }
}