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
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.ViewPagerAdapter;
import com.example.demoapp.ui.adapter.ActivityImageAdapter;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreatePlanFragment extends Fragment {

    Spinner countrySpinner;
    EditText dateSpinner;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"ResourceType", "CutPasteId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        countrySpinner = view.findViewById(R.id.Country_Plan);
        dateSpinner = view.findViewById(R.id.Date_Plan);
        countrySpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, Country.values()));

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tablayout = (TabLayout) view.findViewById(R.id.tabLayout);

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getChildFragmentManager());

        vpAdapter.addFragment(AddMemoryFragment.getInstance(), "Add Memory");
        vpAdapter.addFragment(AddMomentFragment.getInstance(), "Add Moment");
        System.out.println(vpAdapter.getCount());
        viewPager.setAdapter(vpAdapter);
        tablayout.setupWithViewPager(viewPager);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout item = requireActivity().findViewById(R.id.pop_up_layout);
        View child = getLayoutInflater().inflate(R.layout.item_create_activity,null);
        item.addView(child);

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

        dateSpinner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(requireActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        item.findViewById(R.id.cross_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    item.removeAllViews();
//                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)item.getLayoutParams();
//                params.height = 1;
//                item.setLayoutParams(params);
                item.setVisibility(View.GONE);
            }
        });

    }

    public void setDateSpinner(EditText dateSpinner) {
        this.dateSpinner = dateSpinner;
    }

    public EditText getDateSpinner() {
        return dateSpinner;
    }

    public void setCountrySpinner(Spinner countrySpinner) {
        this.countrySpinner = countrySpinner;
    }

    public Spinner getCountrySpinner() {
        return countrySpinner;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        dateSpinner.setText(sdf.format(myCalendar.getTime()));
    }

}