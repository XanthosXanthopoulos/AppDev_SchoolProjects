package com.example.demoapp.ui.main.account;

import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.demoapp.R;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.AccountType;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.view.AuthenticatedUserView;
import com.example.demoapp.data.viewmodel.AuthenticationResult;
import com.example.demoapp.ui.authentication.AuthenticationActivity;
import com.example.demoapp.ui.main.MainActivity;
import com.example.demoapp.util.ApiRoutes;
import com.example.demoapp.util.ViewModelFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.example.demoapp.App.SHARED_PREFS;

public class AccountFragment extends Fragment
{
    private static final int IMAGE_SELECT = 0;

    private AccountViewModel viewModel;

    ImageView profileImage;
    EditText nameEditText;
    EditText surnameEditText;
    EditText descriptionEditText;
    DatePicker birthdayDatePicker;
    AutoCompleteTextView countryTextView;
    Spinner accountTypeSpinner;
    Button saveButton;

    private Uri imageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(AccountViewModel.class);

        profileImage  =view.findViewById(R.id.Register_ProfileImage);
        nameEditText = view.findViewById(R.id.Register_Name);
        surnameEditText = view.findViewById(R.id.Register_Surname);
        descriptionEditText = view.findViewById(R.id.Register_Description);
        birthdayDatePicker = view.findViewById(R.id.Register_Birthday);
        countryTextView = view.findViewById(R.id.Register_Country);
        accountTypeSpinner = view.findViewById(R.id.Register_Privacy);
        saveButton = view.findViewById(R.id.Register_Save_Profile);

        countryTextView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Country.values()));
        accountTypeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, AccountType.values()));

        countryTextView.setValidator(new AutoCompleteTextView.Validator()
        {
            @Override
            public boolean isValid(CharSequence text)
            {
                int index = Arrays.binarySearch(Country.values(), text.toString(), new Comparator<Serializable>()
                {
                    @Override
                    public int compare(Serializable o1, Serializable o2)
                    {
                        if (o1 instanceof Country && o2 instanceof String)
                        {
                            return  ((Country) o1).label.compareTo((String)o2);
                        }
                        else
                        {
                            return 1;
                        }
                    }
                });

                return index >= 0;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText)
            {
                return Country.ANY.label;
            }
        });

        viewModel.getProfileData().observe(getViewLifecycleOwner(), new Observer<AuthenticationResult>()
        {
            @Override
            public void onChanged(AuthenticationResult result)
            {
                if (result.getSuccess() != null) updateUiWithUser(result.getSuccess());
            }
        });

        viewModel.getAccountFormState().observe(getViewLifecycleOwner(), new Observer<AccountFormState>()
        {
            @Override
            public void onChanged(AccountFormState accountFormState)
            {
                if (accountFormState.isDataValid())
                {
                    nameEditText.setError(null);
                    surnameEditText.setError(null);
                    countryTextView.setError(null);

                    saveButton.setEnabled(true);

                    return;
                }

                if (accountFormState.getNameError() != null)
                {
                    nameEditText.setError(getString(accountFormState.getNameError()));
                }

                if (accountFormState.getSurnameError() != null)
                {
                    surnameEditText.setError(getString(accountFormState.getSurnameError()));
                }

                if (accountFormState.getCountryError() != null)
                {
                    countryTextView.setError(getString(accountFormState.getCountryError()));
                }

                saveButton.setEnabled(false);
            }
        });

        viewModel.getSaveResult().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>()
        {
            @Override
            public void onChanged(Event<Boolean> result)
            {
                if (result.isHandled()) return;

                if (result.getData())
                {
                    if (getActivity() instanceof AuthenticationActivity)
                    {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Navigation.findNavController(getView()).navigate(R.id.navigation_profile);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Something went wrong while saving you profile", Toast.LENGTH_LONG).show();
                }

                result.setHandled(true);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), IMAGE_SELECT);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = nameEditText.getText().toString();
                String surname = surnameEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                int day = birthdayDatePicker.getDayOfMonth();
                int month = birthdayDatePicker.getMonth();
                int year =  birthdayDatePicker.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                Country country = Country.lookupByLabel(countryTextView.getText().toString());

                viewModel.updateProfile(imageUri, name, surname, description, calendar.getTime(), country, (AccountType) accountTypeSpinner.getSelectedItem());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                viewModel.accountDataChanged(nameEditText.getText().toString(), surnameEditText.getText().toString(), Country.lookupByLabel(countryTextView.getText().toString()));
            }
        };

        nameEditText.addTextChangedListener(afterTextChangedListener);
        surnameEditText.addTextChangedListener(afterTextChangedListener);
        countryTextView.addTextChangedListener(afterTextChangedListener);

        if (getActivity() instanceof MainActivity)
        {
            viewModel.loadUserProfile();
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_SELECT && resultCode == RESULT_OK && data != null)
        {
            Glide.with(getContext()).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
            imageUri = data.getData();
        }
    }

    private void updateUiWithUser(AuthenticatedUserView model)
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", model.getProfileImageID());
        GlideUrl url = new GlideUrl(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + sharedPreferences.getString("JWToken", "")).build());

        Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
        nameEditText.setText(model.getName());
        surnameEditText.setText(model.getSurname());
        descriptionEditText.setText(model.getDescription());
        birthdayDatePicker.init(model.getYear(), model.getMonth(), model.getDay(), null);
        countryTextView.setText(model.getCountry().label);
        accountTypeSpinner.setSelection(model.getAccountType().value);
    }
}