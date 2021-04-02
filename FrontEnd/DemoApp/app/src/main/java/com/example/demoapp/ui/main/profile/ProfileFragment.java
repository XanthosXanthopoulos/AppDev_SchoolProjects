package com.example.demoapp.ui.main.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Trip;
import com.example.demoapp.ui.adapter.TripAdapter;
import com.example.demoapp.ui.authentication.login.LoginViewModel;
import com.example.demoapp.util.ViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileFragment extends Fragment
{

    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(ProfileViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.Trip_List);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        List<Trip> sList = getListItemData();

        TripAdapter rcAdapter = new TripAdapter(sList);
        recyclerView.setAdapter(rcAdapter);

        view.findViewById(R.id.Register_ProfileImage).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_trip));

        return view;
    }

    private List<Trip> getListItemData()
    {
        List<Trip> listViewItems = new ArrayList<Trip>();
        try
        {
            listViewItems.add(new Trip("1984", Country.AD, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998")));
            listViewItems.add(new Trip("Pride and Prejudice", Country.AL, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998")));
            listViewItems.add(new Trip("One Hundred Years of Solitude", Country.AD, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998")));
            listViewItems.add(new Trip("The Book Thief", Country.AO, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998")));
            listViewItems.add(new Trip("The Hunger Games", Country.AD, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998")));
            listViewItems.add(new Trip("The Hitchhiker's Guide to the Galaxy", Country.AX, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998")));
            listViewItems.add(new Trip("The Theory Of Everything", Country.AD, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998")));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return listViewItems;
    }
}