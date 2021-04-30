package com.example.demoapp.ui.main.plan.memory;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demoapp.R;

public class AddMemoryFragment extends Fragment
{

    private AddMemoryViewModel mViewModel;

    public static AddMemoryFragment newInstance()
    {
        return new AddMemoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_memory, container, false);

        return view;
    }
}