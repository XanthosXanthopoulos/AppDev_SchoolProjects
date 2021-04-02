package com.example.demoapp.ui.main.account;

import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.data.view.AuthenticatedUserView;

public class AccountFragment extends Fragment
{

    private AccountViewModel accountViewModel;

    EditText nameEditText;
    EditText surnameEditText;
    EditText descriptionEditText;
    DatePicker birthdayDatePicker;
    Spinner countrySpinner;
    Spinner accountTypeSpinner;
    Button saveButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        nameEditText = view.findViewById(R.id.Register_Name);
        surnameEditText = view.findViewById(R.id.Register_Surname);
        descriptionEditText = view.findViewById(R.id.Register_Description);
        birthdayDatePicker = view.findViewById(R.id.Register_Birthday);
        countrySpinner = view.findViewById(R.id.Register_Country);
        accountTypeSpinner = view.findViewById(R.id.Register_Privacy);
        saveButton = view.findViewById(R.id.Register_Save_Profile);

        return view;
    }

    private void updateUiWithUser(AuthenticatedUserView model)
    {
        nameEditText.setText(model.getName());
        surnameEditText.setText(model.getSurname());
        descriptionEditText.setText(model.getDescription());
        birthdayDatePicker.init(model.getYear(), model.getMonth(), model.getDay(), null);
        countrySpinner.setSelection(model.getCountry().code);
        accountTypeSpinner.setSelection(model.getAccountType().value);
    }

    private void showLoginFailed(@StringRes Integer errorString)
    {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}