package com.example.demoapp.ui.main.plan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.ui.adapter.CreatePlanViewPagerAdapter;
import com.example.demoapp.util.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private BottomNavigationView navBar;


    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @SuppressLint({"ResourceType", "CutPasteId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        navBar = getActivity().findViewById(R.id.nav_view);
        navBar.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(CreatePlanViewModel.class);

        titleEditText = view.findViewById(R.id.post_title);
        descriptionEditText = view.findViewById(R.id.post_description);
        dateSpinner = view.findViewById(R.id.Date_Plan);
        calendarButton = view.findViewById(R.id.calendar_button);
        uploadButton = view.findViewById(R.id.upload_button);
        loadingProgressBar = view.findViewById(R.id.loading);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new CreatePlanViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(new String[]{"Memories", "Moments"}[position])).attach();

        DatePickerDialog.OnDateSetListener dateSetListener = (view1, year, month, dayOfMonth) ->
        {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        calendarButton.setOnClickListener(v ->
        {
            // TODO Auto-generated method stub
            new DatePickerDialog(requireActivity(), dateSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        dateSpinner.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (dateSpinner.getHint().toString().equals("Date"))
            {
                dateSpinner.setHint("dd/mm/yyyy");
            }
            else
            {
                dateSpinner.setHint("Date");
            }
        });

        uploadButton.setOnClickListener(v ->
        {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            Date date;
            try
            {
                if (dateSpinner.getText().toString().isEmpty())
                {
                    date = new Date(0);
                }
                else
                {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateSpinner.getText().toString());
                }
            }
            catch (ParseException e)
            {
                date = new Date(0);
            }

            loadingProgressBar.setVisibility(View.VISIBLE);
            viewModel.uploadPost(title, description, date);
        });

        viewModel.getUploadResultLiveData().observe(getViewLifecycleOwner(), result ->
        {
            if (result.isHandled()) return;

            result.setHandled(true);
            loadingProgressBar.setVisibility(View.GONE);

            if (result.getData())
            {
                Navigation.findNavController(view).navigate(R.id.navigation_profile);
                navBar.setVisibility(View.VISIBLE);
            }
            else
            {
                Toast.makeText(getContext(), "Something went wrong!! Please try uploading again.", Toast.LENGTH_LONG).show();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                viewModel.memoryDataChanged(titleEditText.getText().toString());
            }
        };

        titleEditText.addTextChangedListener(afterTextChangedListener);

        viewModel.getPlanFormState().observe(getViewLifecycleOwner(), createPlanFormState ->
        {
            if (createPlanFormState.isDataValid())
            {
                titleEditText.setError(null);

                uploadButton.setEnabled(true);

                return;
            }

            if (createPlanFormState.getTitleError() != null)
            {
                titleEditText.setError(getString(createPlanFormState.getTitleError()));
            }

            uploadButton.setEnabled(false);
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */)
        {
            @Override
            public void handleOnBackPressed()
            {
                new AlertDialog.Builder(getContext())
                        .setTitle("Create plan")
                        .setMessage("Your plan template will be discarded.")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) ->
                        {
                            Navigation.findNavController(view).navigate(R.id.navigation_profile);
                            navBar.setVisibility(View.VISIBLE);
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    private void updateLabel()
    {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        dateSpinner.setText(sdf.format(myCalendar.getTime()));
    }

}