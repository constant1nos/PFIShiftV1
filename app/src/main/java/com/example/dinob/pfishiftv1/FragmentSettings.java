package com.example.dinob.pfishiftv1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Fragment to repeat when define a shift cycle in the settings menu.
 */

public class FragmentSettings extends android.support.v4.app.Fragment {
    Calendar myCal = Calendar.getInstance();
    int mYear = myCal.get(Calendar.YEAR);
    int mMonth = myCal.get(Calendar.MONTH);
    int mDay = myCal.get(Calendar.DAY_OF_MONTH);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        // Declare the Spinner
        Spinner spinner = view.findViewById(R.id.setSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinnerShifts, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Button pickDate1Button = (Button) view.findViewById(R.id.buttonDate1);
        Button pickDate2Button = (Button) view.findViewById(R.id.buttonDate2);

        pickDate1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
        pickDate2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
        return view;
    }

    // Do things when date is picked
    public void pickDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //Todo what i want on DateSet
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
