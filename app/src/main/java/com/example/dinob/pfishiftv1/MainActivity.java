package com.example.dinob.pfishiftv1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Initialise my variables
    CalendarView mCalendar;

    String vardiaNow, vardiaOut; //Strings for the text
    Calendar cal=Calendar.getInstance(); //get current date
    int mDay=cal.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
    int mMonth = cal.get(Calendar.MONTH);
    int mYear=cal.get(Calendar.YEAR);
    int chosenDoy; //this is to find day of year after user has set the date
    ShiftCalculation myShift = new ShiftCalculation();

    boolean PFI,savedSettings;

    List<EventDay> events = new ArrayList<>();

    private int[] shiftIcons = {R.drawable.alpha,R.drawable.pi,R.drawable.nu,R.drawable.rho};


        // Set views and listeners onCreate
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Create shared preferences XML
            Context context = this;
            appPreferences checkPref = appPreferences.getInstance(context);

            // Check if stored settings found
            PFI = checkPref.getPref("PFIShift",PFI); // for PFI shift
            savedSettings = checkPref.getPref("savedSettings",savedSettings); // for general shift
            if(!PFI && !savedSettings) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle(R.string.alert_intro_title)
                        .setMessage(R.string.alert_intro_msg)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mIntent = new Intent(MainActivity.this, Settings.class);
                                startActivity(mIntent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            // Used for screen rotation to keep data
            if(savedInstanceState != null){
                mYear = savedInstanceState.getInt("nYear");
                mMonth = savedInstanceState.getInt("nMonth");
                mDay = savedInstanceState.getInt("nDay");
                vardiaNow = savedInstanceState.getString("nVardia");
            }

            // Definition of views
            mCalendar = (CalendarView) findViewById(R.id.calendarView);

            // Trigger listeners

            // Set current day on CalendarView
            mCalendar.setDate(cal);

            if(PFI || savedSettings) {
                customizeCalendar();
                mCalendar.setEvents(events);
            }
        }

    // Create the ActionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appmenu, menu);
        return true;
    }

    // On a menu item selected, do what is necessary
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.About:
                // User chose the "About" item, show the app about UI...
                Intent intentAbout = new Intent(MainActivity.this, About.class);
                startActivity(intentAbout);
                return true;
                // User clicked calendar menu icon, go to CustomCalendar activity
            case R.id.Calendar:

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // method to add labels on CalendarView
    private void customizeCalendar() {
        for(int i=1;i<60;i++) {
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i);
            mDay=cal.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
            mMonth = cal.get(Calendar.MONTH);
            mYear=cal.get(Calendar.YEAR);
            doTheCalcs();
        }
        mCalendar.setEvents(events);
        doTheCalcs();
    }

    private void changeDateValues() {

    }

    // method to call ShiftCalculation class and calculate the output
    public void doTheCalcs() {
        myShift.setmContext(this); // Pass context to myShift
        cal.set(mYear, mMonth, mDay); //set the chosen day on calendar
        chosenDoy = cal.get(Calendar.DAY_OF_YEAR); //get day of year of chosen date
        myShift.calculation(mYear, chosenDoy);

        // Run the appropriate method to calculate the shift
        if(PFI) {
            vardiaOut = myShift.getPFIShift();
            setCalendarIcons(vardiaOut);
        }
        else if(savedSettings)
            vardiaOut=myShift.getShift();
            setCalendarIcons(vardiaOut);
    }

    private void setCalendarIcons(String shift) {
        if(shift==getString(R.string.Evening) || shift==getString(R.string.resultEvening))
            events.add(new EventDay(cal, shiftIcons[0]));
        else if(shift==getString(R.string.Morning) || shift==getString(R.string.resultMorning))
            events.add(new EventDay(cal, shiftIcons[1]));
        else if(shift==getString(R.string.Night) || shift==getString(R.string.resultNight))
            events.add(new EventDay(cal, shiftIcons[2]));
        else
            events.add(new EventDay(cal, shiftIcons[3]));
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("nYear",mYear);
        outState.putInt("nMonth",mMonth);
        outState.putInt("nDay",mDay);
        outState.putString("nVardia",vardiaNow);
        }

    }