package com.example.demoapp.data.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.demoapp.data.datasource.ApiDataSource;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.api.request.SearchQueryModel;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.data.model.repository.RepositoryResponse;

import java.util.List;

public class ContentRepository
{
    private static volatile ContentRepository instance;
    private final ApiDataSource dataSource;

    private final MediatorLiveData<RepositoryResponse<List<Item>>> searchResult;

    private ContentRepository(ApiDataSource dataSource)
    {
        this.dataSource = dataSource;

        searchResult = new MediatorLiveData<>();
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

    public LiveData<RepositoryResponse<List<Item>>> getSearchResult()
    {
        return searchResult;
    }
}
