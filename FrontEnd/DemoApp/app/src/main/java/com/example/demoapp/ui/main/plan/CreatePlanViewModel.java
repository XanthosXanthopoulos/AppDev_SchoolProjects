package com.example.demoapp.ui.main.plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.demoapp.R;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.repository.RepositoryResponse;
import com.example.demoapp.data.repository.ContentRepository;

import java.util.Date;

public class CreatePlanViewModel extends ViewModel
{
    private final ContentRepository repository;
    private final LiveData<Event<Boolean>> uploadResultLiveData;
    private final MutableLiveData<CreatePlanFormState> planFormState;

    public CreatePlanViewModel(ContentRepository repository)
    {
        this.repository = repository;
        this.planFormState = new MutableLiveData<>();
        repository.initializePostData();

        uploadResultLiveData = Transformations.map(repository.getUploadResult(), RepositoryResponse::getResponse);
    }

    public void uploadPost(String title, String description, Date date)
    {
        repository.uploadPost(title, description, date);
    }

    public LiveData<Event<Boolean>> getUploadResultLiveData()
    {
        return uploadResultLiveData;
    }

    public LiveData<CreatePlanFormState> getPlanFormState()
    {
        return planFormState;
    }

    public void memoryDataChanged(String title)
    {
        if (!isTitleValid(title))
        {
            planFormState.setValue(new CreatePlanFormState(R.string.name_error));
        }
        else
        {
            planFormState.setValue(new CreatePlanFormState(true));
        }
    }

    private boolean isTitleValid(String name)
    {
        if (name == null)
        {
            return false;
        }

        return !name.isEmpty();
    }
}