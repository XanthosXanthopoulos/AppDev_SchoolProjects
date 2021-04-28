package com.example.demoapp.ui.main.plan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Image;
import com.example.demoapp.data.model.Trip;
import com.example.demoapp.ui.adapter.TripAdapter;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddMomentFragment extends Fragment {

    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_OK = -1;

    List<Trip> TripList = new ArrayList<>();
    RecyclerView recyclerView;
    Image Image;
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
        View view = inflater.inflate(R.layout.fragment_add_moment, container, false);

        recyclerView = view.findViewById(R.id.Image_List);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        TripAdapter rcAdapter = new TripAdapter(getListItemData());
        recyclerView.setAdapter(rcAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout item = requireActivity().findViewById(R.id.pop_up_layout);
        View child = getLayoutInflater().inflate(R.layout.item_create_activity, null);
        item.addView(child);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }

        view.findViewById(R.id.create_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add activity and dynamically fill the gaps
                onImageGalleryClicked(v);
            }
        });

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

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case RESULT_LOAD_IMAGE:

                if(resultCode == RESULT_OK) {       // if we are here we communicated if the image gallery.

                    // The address of the image on th SD card.
                    Uri selectedImage = data.getData();
                    // Declaring a stream to read the image data for the sd card.
                    InputStream imageInputStream;

                    try {
                        // make connection based on the URI of the image.
                        imageInputStream = requireActivity().getContentResolver().openInputStream(selectedImage);

                        // get a bitmap from the stream.
                        Bitmap image = BitmapFactory.decodeStream(imageInputStream);

                        // add the image to the variable ImageView.
                        Image = new Image(image);

                        // Close stream.
                        imageInputStream.close();

                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(requireActivity(),"Unable to load image",Toast.LENGTH_LONG).show();
                    }

                    TripAdapter tripAdapter = null;
                    try {
                        tripAdapter = new TripAdapter(updateTripList(getListItemData()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    recyclerView.setAdapter(tripAdapter);

                }

        }
    }

    private List<Trip> getListItemData () {
        return TripList;
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

    @SuppressLint("SimpleDateFormat")
    public List<Trip> updateTripList(List<Trip> tripList) throws ParseException {

        List<Trip> trips = tripList;
        final EditText d = requireActivity().findViewById(R.id.Title_Plan);
        final Spinner c = requireActivity().findViewById(R.id.Country_Plan);
        final EditText dt = requireActivity().findViewById(R.id.Date_Plan);

        String description = d.getText().toString();
        Country country = (Country) c.getSelectedItem();
        Date date;
        if(!dt.getText().toString().equals("")) {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dt.getText().toString());
        }else date = null;
        trips.add(new Trip(Image,description,country,date));

        return trips;
    }

    public void onImageGalleryClicked(View v){

        Intent i = new Intent(Intent.ACTION_PICK);

        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");

        startActivityForResult(i,RESULT_LOAD_IMAGE);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        dateSpinner.setText(sdf.format(myCalendar.getTime()));
    }

    public static AddMomentFragment getInstance(){
        return new AddMomentFragment();
    }

}