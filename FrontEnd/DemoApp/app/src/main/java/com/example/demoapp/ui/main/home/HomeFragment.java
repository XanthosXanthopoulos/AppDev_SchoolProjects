package com.example.demoapp.ui.main.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.util.ViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment
{
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(HomeViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.Post_list);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        List<Item> sPost = getListItemData();

        SearchResultAdapter rcAdapter = new SearchResultAdapter();
        rcAdapter.setItems(sPost);
        recyclerView.setAdapter(rcAdapter);

        return view;
    }

    private List<Item> getListItemData()
    {
        List<Item> listViewItems = new ArrayList<>();
        try
        {
            Bitmap account_image = null;
            Bitmap plan_image = null;
            listViewItems.add(new Post(account_image, "Nikolas Vattis", plan_image, "Loreal Pari", Country.AD, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998")));
            listViewItems.add(new Post(account_image, "Pantelis Konstantinou", plan_image, "Loreal ASDASD", Country.AL, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1991")));
            listViewItems.add(new Post(account_image, "Antreas Taouksi", plan_image, "Loreal ASDASDWQEQ", Country.AL, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1928")));
            listViewItems.add(new Post(account_image, "Xanthos Xanthopoulos", plan_image, "Loreal Parasdadasdasg afasfas", Country.AS, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1938")));
            listViewItems.add(new Post(account_image, "Nikols Nikolaou", plan_image, "Loreal 2312adasdasdasd", Country.AX, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1994")));
            listViewItems.add(new Post(account_image, "Nikos", plan_image, "Loreal safwqrqeasd asdasdqwe asdasd", Country.AD, new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2212")));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return listViewItems;
    }
}