package com.example.demoapp.data.repository;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.datasource.DiskDataSource;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.api.request.SearchQueryModel;
import com.example.demoapp.data.model.api.response.PostResponseModel;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.viewmodel.ItemUpdate;

import java.util.List;

public class ContentRepository
{
    private static volatile ContentRepository instance;
    private final ApiDataSource dataSource;

    private final MediatorLiveData<RepositoryResponse<List<Item>>> searchResult;
    private final MediatorLiveData<RepositoryResponse<List<Item>>> feedResult;
    private final MediatorLiveData<RepositoryResponse<ItemUpdate>> profileResult;
    private final MediatorLiveData<RepositoryResponse<Bitmap>> planResult;

    private ContentRepository(ApiDataSource dataSource)
    {
        this.dataSource = dataSource;

        searchResult = new MediatorLiveData<>();
        feedResult = new MediatorLiveData<>();
        profileResult = new MediatorLiveData<>();
        planResult = new MediatorLiveData<>();
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
        LiveData<DataSourceResponse<List<Item>>> dataSourceResult = dataSource.getFeed("");
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

    public void getProfileImage(String imageID, int position)
    {
        DiskDataSource source = DiskDataSource.getInstance();
        LiveData<DataSourceResponse<Bitmap>> diskResult = source.loadImage(imageID);
        profileResult.addSource(diskResult, new Observer<DataSourceResponse<Bitmap>>()
        {
            @Override
            public void onChanged(DataSourceResponse<Bitmap> response)
            {
                if (response.isSuccessful())
                {
                    profileResult.setValue(new RepositoryResponse<>(new ItemUpdate(position, response.getResponse())));
                }
                else
                {
                    LiveData<DataSourceResponse<Bitmap>> webResult = dataSource.requestImage(imageID, "");
                    profileResult.addSource(webResult, new Observer<DataSourceResponse<Bitmap>>()
                    {
                        @Override
                        public void onChanged(DataSourceResponse<Bitmap> response)
                        {
                            profileResult.setValue(new RepositoryResponse<>(new ItemUpdate(position, response.getResponse())));
                            source.storeImage(imageID, ".jpg", response.getResponse());
                            profileResult.removeSource(webResult);
                        }
                    });
                }

                profileResult.removeSource(diskResult);
            }
        });

//        LiveData<DataSourceResponse<Bitmap>> webResult = dataSource.requestImage(imageID, "");
//        profileResult.addSource(webResult, new Observer<DataSourceResponse<Bitmap>>()
//        {
//            @Override
//            public void onChanged(DataSourceResponse<Bitmap> response)
//            {
//                profileResult.setValue(new RepositoryResponse<>(new ItemUpdate(position, response.getResponse())));
//                profileResult.removeSource(webResult);
//            }
//        });
    }

    public LiveData<RepositoryResponse<List<Item>>> getSearchResult()
    {
        return searchResult;
    }


    public MediatorLiveData<RepositoryResponse<List<Item>>> getFeedResult()
    {
        return feedResult;
    }

    public MediatorLiveData<RepositoryResponse<ItemUpdate>> getProfileResult()
    {
        return profileResult;
    }

    public MediatorLiveData<RepositoryResponse<Bitmap>> getPlanResult()
    {
        return planResult;
    }
}
