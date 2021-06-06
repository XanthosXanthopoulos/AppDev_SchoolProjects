package com.example.demoapp.ui.main.plan.moment;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.demoapp.R;
import com.example.demoapp.ui.adapter.ImageUriAdapter;
import com.example.demoapp.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddMomentFragment extends Fragment
{
    private static final int IMAGE_SELECT = 0;

    private AddMomentViewModel viewModel;

    private RecyclerView momentList;
    private ImageUriAdapter adapter;
    private Button addImageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_moment, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(AddMomentViewModel.class);

        momentList = view.findViewById(R.id.moment_list);
        addImageButton = view.findViewById(R.id.add_image_button);

        adapter = new ImageUriAdapter(getContext());
        momentList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        momentList.setAdapter(adapter);

        viewModel.getImagesLiveData().observe(getViewLifecycleOwner(), paths ->
        {
            List<Uri> uris = new ArrayList<>();
            paths.forEach(path -> uris.add(Uri.parse(path)));
            adapter.setItems(uris);
        });

        addImageButton.setOnClickListener(v ->
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), IMAGE_SELECT);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                viewModel.removeImage(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(momentList);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_SELECT && resultCode == RESULT_OK && data != null)
        {
            ArrayList<Uri> URIs = new ArrayList<>(data.getClipData() == null ? 1 : data.getClipData().getItemCount());

            if (data.getClipData() != null)
            {
                ClipData clipData = data.getClipData();

                for (int i = 0; i < clipData.getItemCount(); i++)
                {
                    URIs.add(clipData.getItemAt(i).getUri());
                }
            }
            else
            {
                URIs.add(data.getData());
            }

            viewModel.addImages(URIs);
        }
    }
}