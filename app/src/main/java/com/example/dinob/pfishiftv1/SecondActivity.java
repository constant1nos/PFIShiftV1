package com.example.dinob.pfishiftv1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by dinob on 18/10/2017.
 */

public class SecondActivity extends AppCompatActivity {

    // Initialise variables
    Calendar myCal = Calendar.getInstance(); //get current date
    int mYear = myCal.get(Calendar.YEAR);
    int mMonth = myCal.get(Calendar.MONTH);
    int mDay = myCal.get(Calendar.DAY_OF_MONTH);
    int[] dates = {0, 0, 0, 0, 0, 0};
    int[] shiftCycleInt = new int[20];

    String[] preferencesTagsInt = {"shift1","shift2","shift3","shift4","shift5","shift6","shift7","shift8","shift9","shift10"};
    String[] preferencesTagsStrings = {"sShift1","sShift2","sShift3","sShift4","sShift5","sShift6","sShift7","sShift8","sShift9","sShift10"};

    String selectedItem; //capture user's choice from spinner
    TextView settingsOutputText;
    Button pickDate1Button, pickDate2Button, addButton, doneButton;
    public int buttonPressed=0;
    int bothDaysPicked = 0, datesPosition;

    // FragmentTextSettings array to hold new fragment objects
    final FragmentTextSettings[] fragmentLayout = {new FragmentTextSettings(), new FragmentTextSettings(), new FragmentTextSettings(),
                                                    new FragmentTextSettings(), new FragmentTextSettings(), new FragmentTextSettings(),
                                                    new FragmentTextSettings(), new FragmentTextSettings(), new FragmentTextSettings(),
                                                    new FragmentTextSettings()};
    // Array to hold the containers of second_activity FrameLayouts
    final int[] fragmentArray = {R.id.myFrag1, R.id.myFrag2, R.id.myFrag3, R.id.myFrag4, R.id.myFrag5,
                                    R.id.myFrag6, R.id.myFrag7, R.id.myFrag8, R.id.myFrag9, R.id.myFrag10};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        getSupportActionBar().setTitle(getString(R.string.menu_settings));
        final Spinner spinner = (Spinner) findViewById(R.id.setSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerShifts, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Define Views
        settingsOutputText = (TextView) findViewById(R.id.settingsOutputText);
        pickDate1Button = (Button) findViewById(R.id.buttonDate1);
        pickDate2Button = (Button) findViewById(R.id.buttonDate2);
        addButton = (Button) findViewById(R.id.addFragmentButton);
        doneButton = (Button) findViewById(R.id.doneFragmentButton);

        // Add listeners
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),getString(R.string.data_saved),Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(SecondActivity.this,MainActivity.class);
                startActivity(mainIntent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = spinner.getItemAtPosition(i).toString(); //this is your selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedItem = null;
            }
        });
        pickDate1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bView) {
                pickDate(bView);
            }
        });
        pickDate2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bView) {
                pickDate(bView);
            }
        });

        // Add new fragment
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Do things when date is picked
    public void pickDate(View bView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Do on date set
                        dates[datesPosition] = dayOfMonth;
                        dates[datesPosition+1] = monthOfYear+1;
                        dates[datesPosition+2] = year;
                        //settingsOutputText.setText("Button 1 pressed"+dates[datesPosition]+dates[datesPosition+1]+dates[datesPosition+2]);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        // Firstly executed this, then onDateSet
        if(bView == pickDate1Button) {
            bothDaysPicked++;
            datesPosition = 0;
        }
        else {
            bothDaysPicked++;
            datesPosition = 3;
        }
    }

    // Do things when add button is pressed
    public void addButton() {

        // Check if all required fields are OK
        if (bothDaysPicked >= 2 && selectedItem != null) {

            // Create bundle with the values needed from fragment's TextView
            Bundle myBundle = new Bundle();
            myBundle.putString("resultText",selectedItem);
            myBundle.putIntArray("dates", dates);
            fragmentLayout[buttonPressed].setArguments(myBundle);

            // Create a fragment to show user's choise
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(fragmentArray[buttonPressed], fragmentLayout[buttonPressed]);
            // or ft.add(R.id.your_placeholder, new FooFragment());
            //Complete the changes added above
            ft.commit();

            appPreferences checkPref = appPreferences.getInstance(this);

            // Generate the desired variables
            if (buttonPressed == 0) { // Always shows the first fragment (startCycle)
                myCal.set(dates[2], dates[1]-1, dates[0]);
                shiftCycleInt[0] = myCal.get(Calendar.DAY_OF_YEAR);
                Log.d("Day of year",""+shiftCycleInt[buttonPressed]);
                myCal.set(dates[5], dates[4]-1, dates[3]);
                shiftCycleInt[1] = myCal.get(Calendar.DAY_OF_YEAR);
                Log.d("Day of year",""+shiftCycleInt[buttonPressed+1]);
                shiftCycleInt[2]=shiftCycleInt[1]-shiftCycleInt[0]; //store the difference

                // Store current fragment's data locally

                checkPref.savePref("startCycle", shiftCycleInt[0]); // stores first dayOfYear of shift cycle
                checkPref.savePref("year", dates[2]); // stores year
                checkPref.savePref(preferencesTagsInt[0],shiftCycleInt[2]);
                checkPref.savePref(preferencesTagsStrings[0], selectedItem);

            }
            else {
                fragmentLayout[buttonPressed-1].deleteFragment.setVisibility(ImageButton.INVISIBLE);

                myCal.set(dates[2], dates[1]-1, dates[0]);
                shiftCycleInt[0] = myCal.get(Calendar.DAY_OF_YEAR);
                Log.d("Day of year",""+shiftCycleInt[0]);
                myCal.set(dates[5], dates[4]-1, dates[3]);
                shiftCycleInt[1] = myCal.get(Calendar.DAY_OF_YEAR);
                Log.d("Day of year",""+shiftCycleInt[1]);
                shiftCycleInt[2]+=(shiftCycleInt[1]-shiftCycleInt[0])+1;

                checkPref.savePref(preferencesTagsInt[buttonPressed],shiftCycleInt[2]);
                checkPref.savePref(preferencesTagsStrings[buttonPressed],selectedItem);
                Log.d("Shift difference",""+shiftCycleInt[2]);
            }
            checkPref.savePref("savedSettings", true);
            // Set/Reset counters
            buttonPressed++;
            bothDaysPicked = 0;
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.Second_Activ_alert_msg),Toast.LENGTH_SHORT).show();
    }
}