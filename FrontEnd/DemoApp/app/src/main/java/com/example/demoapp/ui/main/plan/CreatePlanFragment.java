package com.example.demoapp.ui.main.plan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;
import com.example.demoapp.ui.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreatePlanFragment extends Fragment
{
    List<Item> ActivityList = new ArrayList<>();
    RecyclerView recyclerView;
    AutoCompleteTextView countrySpinner;
    EditText dateSpinner;
    ImageButton calendarIcon;
    final Calendar myCalendar = Calendar.getInstance();


    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @SuppressLint({"ResourceType", "CutPasteId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        countrySpinner = view.findViewById(R.id.country_plan);
        dateSpinner = view.findViewById(R.id.Date_Plan);
        calendarIcon = view.findViewById(R.id.calendar_button);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(new String[]{"Memories", "Moments"}[position])).attach();

        countrySpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Country.values()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout item = requireActivity().findViewById(R.id.pop_up_layout);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        calendarIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(requireActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(dateSpinner.getHint().toString().equals("Date")){
                    dateSpinner.setHint("dd/mm/yyyy");
                }else{
                    dateSpinner.setHint("Date");
                }
            }
        });

        view.findViewById(R.id.upload_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        dateSpinner.setText(sdf.format(myCalendar.getTime()));
    }

}