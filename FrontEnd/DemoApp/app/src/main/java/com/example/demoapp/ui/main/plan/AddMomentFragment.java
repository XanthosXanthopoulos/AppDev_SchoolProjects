package com.example.demoapp.ui.main.plan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.demoapp.R;
import com.example.demoapp.data.model.TripContent;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddMomentFragment extends Fragment {

    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_OK = ;

    List<TripContent> ActivityList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_moment, container, false);

        recyclerView = view.findViewById(R.id.Create_Plan_List);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        ActivityImageAdapter rcAdapter = new ActivityImageAdapter(getListItemData());
        recyclerView.setAdapter(rcAdapter);

        return view;

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_create_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout item = requireActivity().findViewById(R.id.pop_up_layout);
        View child = getLayoutInflater().inflate(R.layout.item_create_activity, null);
        item.addView(child);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }

        view.findViewById(R.id.create_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add activity and dynamically fill the gaps
//                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)item.getLayoutParams();
//                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT; //1300
//                item.setLayoutParams(params);
//                item.setVisibility(View.VISIBLE);
//                ActivityList.add(new Activity(title, desc, tags, address, type));

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,RESULT_LOAD_IMAGE);
            }
        });

        view.findViewById(R.id.Add_memory_btn).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_CreatePlan));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case RESULT_LOAD_IMAGE:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                }
        }
    }

    private List<TripContent> getListItemData () {
        return ActivityList;
    }

}