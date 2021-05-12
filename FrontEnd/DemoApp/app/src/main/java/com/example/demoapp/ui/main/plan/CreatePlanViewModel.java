package com.example.demoapp.ui.main.plan;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;
import com.example.demoapp.data.repository.UserRepository;

import java.util.Date;

public class CreatePlanViewModel extends ViewModel
{
    private ContentRepository repository;
    private final LiveData<Boolean> uploadResultLiveData;

    public CreatePlanViewModel(ContentRepository repository)
    {
        this.repository = repository;

        repository.initializePostData();

        uploadResultLiveData = Transformations.map(repository.getUploadResult(), new Function<RepositoryResponse<Boolean>, Boolean>()
        {
            @Override
            public Boolean apply(RepositoryResponse<Boolean> input)
            {
                return input.getResponse();
            }
        });
    }

    public void uploadPost(String title, String description, Date date)
    {
        repository.uploadPost(title, description, date);
    }

    public LiveData<Boolean> getUploadResultLiveData()
    {
        return uploadResultLiveData;
    }
}