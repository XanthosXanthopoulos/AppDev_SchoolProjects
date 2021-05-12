package com.example.demoapp.ui.main.plan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.ui.adapter.ViewPagerAdapter;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreatePlanFragment extends Fragment
{
    private CreatePlanViewModel viewModel;

    private final Calendar myCalendar = Calendar.getInstance();

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText dateSpinner;
    private ImageButton calendarButton;
    private ImageButton uploadButton;
    private ProgressBar loadingProgressBar;


    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @SuppressLint({"ResourceType", "CutPasteId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(CreatePlanViewModel.class);

        titleEditText = view.findViewById(R.id.post_title);
        descriptionEditText = view.findViewById(R.id.post_description);
        dateSpinner = view.findViewById(R.id.Date_Plan);
        calendarButton = view.findViewById(R.id.calendar_button);
        uploadButton = view.findViewById(R.id.upload_button);
        loadingProgressBar = view.findViewById(R.id.loading);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(new String[]{"Memories", "Moments"}[position])).attach();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        calendarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                new DatePickerDialog(requireActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (dateSpinner.getHint().toString().equals("Date"))
                {
                    dateSpinner.setHint("dd/mm/yyyy");
                }
                else
                {
                    dateSpinner.setHint("Date");
                }
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                Date date;
                try
                {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateSpinner.getText().toString());
                }
                catch (ParseException e)
                {
                    date = null;
                }
                loadingProgressBar.setVisibility(View.VISIBLE);
                viewModel.uploadPost(title, description, date);
            }
        });

        viewModel.getUploadResultLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean aBoolean)
            {
                loadingProgressBar.setVisibility(View.GONE);

                if (aBoolean)
                {
                    Navigation.findNavController(view).navigate(R.id.navigation_profile);
                }
                else
                {
                    Toast.makeText(getContext(), "Something went wrong!! Please try uploading again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void updateLabel()
    {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        dateSpinner.setText(sdf.format(myCalendar.getTime()));
    }

}