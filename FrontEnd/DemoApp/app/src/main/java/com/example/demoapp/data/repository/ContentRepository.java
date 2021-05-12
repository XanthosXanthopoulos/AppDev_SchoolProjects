package com.example.demoapp.data.repository;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.api.request.SearchQueryModel;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.viewmodel.ItemUpdate;

import java.util.Date;
import java.util.List;

public class ContentRepository extends Repository
{
    private static volatile ContentRepository instance;
    private final ApiDataSource dataSource;

    private final MediatorLiveData<RepositoryResponse<List<Item>>> searchResult;
    private final MediatorLiveData<RepositoryResponse<List<Item>>> feedResult;
    private final MediatorLiveData<RepositoryResponse<Boolean>> uploadResult;

    private final MutableLiveData<Activity> currentActivity;
    private final MutableLiveData<Post> currentPost;

    private ContentRepository(ApiDataSource dataSource)
    {
        this.dataSource = dataSource;

        searchResult = new MediatorLiveData<>();
        feedResult = new MediatorLiveData<>();
        uploadResult = new MediatorLiveData<>();

        currentActivity = new MutableLiveData<>();
        currentPost = new MutableLiveData<>();
    }

    public static ContentRepository getInstance(ApiDataSource dataSource)
    {
        if (instance == null)
        {
            instance = new ContentRepository(dataSource);
        }
        return instance;
    }

    public void search(String query, String country, String type, int radius)
    {
        LiveData<DataSourceResponse<List<Item>>> dataSourceResult = dataSource.search(new SearchQueryModel(query, country, type, radius), "");
        searchResult.addSource(dataSourceResult, new Observer<DataSourceResponse<List<Item>>>()
        {
            @Override
            public void onChanged(@Nullable DataSourceResponse<List<Item>> result)
            {
                if (result.isSuccessful())
                {
                    searchResult.setValue(new RepositoryResponse<>(result.getResponse()));
                }
                else
                {
                    searchResult.setValue(new RepositoryResponse<>(result.getErrorMessage()));
                }

                searchResult.removeSource(dataSourceResult);
            }
        });
    }

    public void updateFeed()
    {
        LiveData<DataSourceResponse<List<Item>>> dataSourceResult = dataSource.getFeed(loadFromPrefs("JWToken"));
        feedResult.addSource(dataSourceResult, new Observer<DataSourceResponse<List<Item>>>()
        {
            @Override
            public void onChanged(DataSourceResponse<List<Item>> result)
            {
                if (!result.isSuccessful())
                {
                    feedResult.setValue(new RepositoryResponse<>(result.getErrorMessage()));
                }
                else
                {
                    feedResult.setValue(new RepositoryResponse<>(result.getResponse()));
                }

                feedResult.removeSource(dataSourceResult);
            }
        });
    }

    public LiveData<RepositoryResponse<List<Item>>> getSearchResult()
    {
        return searchResult;
    }

    public LiveData<RepositoryResponse<List<Item>>> getFeedResult()
    {
        return feedResult;
    }

    public LiveData<RepositoryResponse<Boolean>> getUploadResult()
    {
        return uploadResult;
    }

    public void uploadPost(String title, String description, Date date)
    {
        Post post = currentPost.getValue();
        post.setDate(date);
        post.setDescription(description);
        post.setTitle(title);

        LiveData<DataSourceResponse<Boolean>> result = dataSource.uploadPost(post, loadFromPrefs("JWToken"));
        uploadResult.addSource(result, new Observer<DataSourceResponse<Boolean>>()
        {
            @Override
            public void onChanged(DataSourceResponse<Boolean> booleanDataSourceResponse)
            {
                uploadResult.setValue(new RepositoryResponse<>(booleanDataSourceResponse.getResponse()));
                uploadResult.removeSource(result);
            }
        });
    }

    public void addImages(List<Uri> images)
    {
        Post post = currentPost.getValue();
        post.getImages().addAll(images);
        currentPost.setValue(post);
    }

    public void storeActivity(Activity activity)
    {
        currentActivity.setValue(activity);
    }

    public void addActivity(Activity activity)
    {
        currentPost.getValue().getActivities().addLast(activity);
    }

    public void initializePostData()
    {
        if (currentPost.getValue() == null)
        {
            currentPost.setValue(new Post());
        }
    }

    public void resetPostData()
    {
        currentPost.setValue(null);
    }

    public MutableLiveData<Activity> getCurrentActivity()
    {
        return currentActivity;
    }

    public MutableLiveData<Post> getCurrentPost()
    {
        return currentPost;
    }
}
