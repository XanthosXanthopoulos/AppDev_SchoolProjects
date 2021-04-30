package com.example.demoapp.ui.main.plan.moment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Image;
import com.example.demoapp.data.model.Trip;
import com.example.demoapp.ui.adapter.ImageUriAdapter;
import com.example.demoapp.ui.adapter.SearchResultAdapter;
import com.example.demoapp.ui.adapter.TripAdapter;
import com.example.demoapp.ui.main.home.HomeViewModel;
import com.example.demoapp.util.ViewModelFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        addImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), IMAGE_SELECT);
            }
        });

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

            adapter.setItems(URIs);
        }
    }
}