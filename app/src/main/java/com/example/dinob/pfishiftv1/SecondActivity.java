package com.example.dinob.pfishiftv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.Calendar;
import java.util.List;


/**
 * Created by dinob on 18/10/2017.
 */

public class SecondActivity extends AppCompatActivity {

    // Initialise variables
    Calendar myCal = Calendar.getInstance(); //get current date
    boolean checkLinearity=true;
    int[] dates = {0, 0, 0, 0, 0, 0};
    int[] shiftCycleInt = new int[20];
    String prefString;
    int iFromSpinner;

    String selectedItem; //capture user's choice from spinner
    Button pickDate1Button, addButton, doneButton;
    public int buttonPressed=0;
    int bothDaysPicked = 0;

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
        //settingsOutputText = (TextView) findViewById(R.id.settingsOutputText);
        pickDate1Button = (Button) findViewById(R.id.buttonDate1);
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
                switch (i) {
                    case 0:
                        selectedItem = getString(R.string.Morning);
                        iFromSpinner=i;
                        break;
                    case 1:
                        selectedItem = getString(R.string.Evening);
                        iFromSpinner=i;
                        break;
                    case 2:
                        selectedItem = getString(R.string.Night);
                        iFromSpinner=i;
                        break;
                    case 3:
                        selectedItem = getString(R.string.DayOff);
                        iFromSpinner=i;
                        break;
                    }
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

        // Add new fragment (addButton listener)
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton();
            }
        });
    }

    // Do things when date is picked
    public void pickDate(View bView) {
        DatePickerBuilder builder = new DatePickerBuilder(this, new OnSelectDateListener() {
            @Override
            public void onSelect(List<Calendar> calendar) {
                int size=calendar.size();
                int i=0;
                dates[i]=calendar.get(0).get(Calendar.DAY_OF_MONTH);
                dates[i+1]=calendar.get(0).get(Calendar.MONTH)+1;
                dates[i+2]=calendar.get(0).get(Calendar.YEAR);
                dates[i+3]=calendar.get(size-1).get(Calendar.DAY_OF_MONTH);
                dates[i+4]=calendar.get(size-1).get(Calendar.MONTH)+1;
                dates[i+5]=calendar.get(size-1).get(Calendar.YEAR);
                bothDaysPicked=1; // Flag
                if(shiftCycleInt[1]!=0) {
                    myCal.set(dates[2], dates[1] - 1, dates[0]);
                    shiftCycleInt[0] = myCal.get(Calendar.DAY_OF_YEAR);
                    if (shiftCycleInt[0] - shiftCycleInt[1] != 1)
                        checkLinearity = false;
                    else checkLinearity = true;
                }
            }
        })
                .pickerType(CalendarView.RANGE_PICKER).daysNames(R.array.theDays).monthsNames(R.array.theMonths);

        //DatePickerBuilder datePicker = builder.build();
        builder.show();
    }

    // Do things when add button is pressed
    public void addButton() {

        // Check if all required fields are OK
        if (bothDaysPicked >= 1 && selectedItem != null && checkLinearity) {

            appPreferences checkPref = appPreferences.getInstance(this);

            // Generate the desired variables
            if (buttonPressed == 0) { // Always shows the first fragment (startCycle)
                myCal.set(dates[2], dates[1] - 1, dates[0]);
                shiftCycleInt[0] = myCal.get(Calendar.DAY_OF_YEAR);
                myCal.set(dates[5], dates[4] - 1, dates[3]);
                shiftCycleInt[1] = myCal.get(Calendar.DAY_OF_YEAR);
                shiftCycleInt[2] = shiftCycleInt[1] - shiftCycleInt[0]; //store the difference

                // clear previously stored data
                checkPref.savePref("startCycle", 0); // stores first dayOfYear of shift cycle
                checkPref.savePref("year", 0); // stores year
                checkPref.savePref("PFIShift", false);
                checkPref.savePref("savedSettings",false);
                checkPref.savePref("PFIRadioButtonChecked",false);
                for(int i = 0; i<10; i++) {
                    prefString = checkPref.getPrefStringTagFromArrayValues(i);
                    checkPref.savePref(prefString, 0);
                    prefString = checkPref.getPrefStringTagFromArrayShifts(i);
                    checkPref.savePref(prefString, 0);
                }

                // Store current fragment's data locally
                checkPref.savePref("startCycle", shiftCycleInt[0]); // stores first dayOfYear of shift cycle
                checkPref.savePref("year", dates[2]); // stores year
                prefString = checkPref.getPrefStringTagFromArrayValues(0);
                checkPref.savePref(prefString, shiftCycleInt[2]);
                prefString = checkPref.getPrefStringTagFromArrayShifts(0);
                checkPref.savePref(prefString, iFromSpinner); /** i changed it to store int */
                prefString = checkPref.getPrefStringTagFromArrayShifts(buttonPressed + 1);
                checkPref.savePref(prefString, -1);
            } else {
                fragmentLayout[buttonPressed - 1].deleteFragment.setVisibility(ImageButton.INVISIBLE);

                myCal.set(dates[2], dates[1] - 1, dates[0]);
                shiftCycleInt[0] = myCal.get(Calendar.DAY_OF_YEAR);
                myCal.set(dates[5], dates[4] - 1, dates[3]);
                shiftCycleInt[1] = myCal.get(Calendar.DAY_OF_YEAR);
                shiftCycleInt[2] += (shiftCycleInt[1] - shiftCycleInt[0]) + 1;

                prefString = checkPref.getPrefStringTagFromArrayValues(buttonPressed);
                checkPref.savePref(prefString, shiftCycleInt[2]);
                prefString = checkPref.getPrefStringTagFromArrayShifts(buttonPressed);
                checkPref.savePref(prefString, iFromSpinner);
                prefString = checkPref.getPrefStringTagFromArrayShifts(buttonPressed + 1);
                checkPref.savePref(prefString, -1);
            }

            // Create bundle with the values needed from fragment's TextView
            Bundle myBundle = new Bundle();
            myBundle.putString("resultText", selectedItem);
            myBundle.putIntArray("dates", dates);
            fragmentLayout[buttonPressed].setArguments(myBundle);

            // Create a fragment to show user's choise
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(fragmentArray[buttonPressed], fragmentLayout[buttonPressed]);
            // or ft.add(R.id.your_placeholder, new FooFragment());
            //Complete the changes added above
            ft.commit();

            checkPref.savePref("savedSettings", true);
            checkPref.savePref("PFI", false);

            // Set/Reset counters
            buttonPressed++;
            bothDaysPicked = 0;
        } else if(!checkLinearity)
            Toast.makeText(getApplicationContext(), getString(R.string.Second_activity_NoLinearity), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), getString(R.string.Second_Activ_alert_msg), Toast.LENGTH_SHORT).show();
    }
}