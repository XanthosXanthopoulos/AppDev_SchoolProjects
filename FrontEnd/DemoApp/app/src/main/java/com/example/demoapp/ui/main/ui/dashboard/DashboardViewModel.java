package com.example.demoapp.ui.main.ui.dashboard;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel
{

    private final LiveData<List<Item>> searchResult;
    private final ContentRepository repository;

    public DashboardViewModel(ContentRepository repository)
    {
        this.repository = repository;

        searchResult = Transformations.map(repository.getSearchResult(), new Function<RepositoryResponse<List<Item>>, List<Item>>()
        {
            @Override
            public List<Item> apply(RepositoryResponse<List<Item>> input)
            {
                if (input.isSuccessful())
                {
                    return input.getResponse();
                }
                else
                {
                    return new ArrayList<>();
                }
            }
        });
    }

    public void search(String query, String country, String type, int radius)
    {
        repository.search(query, country, type, radius);
    }

    public LiveData<List<Item>> getSearchResult()
    {
        return searchResult;
    }
}