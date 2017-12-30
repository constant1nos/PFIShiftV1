package com.example.dinob.pfishiftv1;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnNavigationButtonClickListener;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Initialise widgets
    CardView quickSearchButton, calendarButton;
    ImageView outputImage;
    TextView outputText;

    // Initialise local variables
    Calendar cal=Calendar.getInstance(); //get current date
    int mDay=cal.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
    int mMonth = cal.get(Calendar.MONTH);
    int mYear=cal.get(Calendar.YEAR);
    int chosenDoy; //this is to find day of year after user has set the date
    boolean PFI,savedSettings; // Flags for settings
    String mString;
    private final int[] shiftIcons = {R.drawable.afternoon,R.drawable.test,R.drawable.night,R.drawable.off};


    // Set views and listeners onCreate
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);

            // Create shared preferences XML
            Context context = this;
            appPreferences checkPref = appPreferences.getInstance(context);

            // Check if stored settings found
            PFI = checkPref.getPref("PFIShift", PFI); // for PFI shift
            savedSettings = checkPref.getPref("savedSettings", savedSettings); // for general shift

            // Create alert dialog if no settings found
            if (!PFI && !savedSettings) {
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
                                // Direct user to settings
                                Intent mIntent = new Intent(MainActivity.this, Settings.class);
                                startActivity(mIntent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            // Definition of views
            quickSearchButton = (CardView) findViewById(R.id.cardSearch);
            calendarButton = (CardView) findViewById(R.id.cardCalendar);
            outputImage = (ImageView) findViewById(R.id.containerImage);
            outputText = (TextView) findViewById(R.id.outputText);

            /** Trigger listeners **/
            // Quick search spinner date picker
           quickSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SpinnerDatePickerDialogBuilder()
                            .callback(new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    mYear = year;
                                    mMonth = monthOfYear;
                                    mDay = dayOfMonth;
                                    cal.set(mYear, mMonth, mDay);
                                    ShiftCalculation myShift = new ShiftCalculation(getApplicationContext());
                                    mString = doTheCalcs(cal, myShift);
                                    outputText.setText(getString(R.string.outputText) + " " + mDay + "/" + (mMonth + 1) + "/" + mYear + " " + getString(R.string.isString) + " " +mString);
                                    showIcon(mString);
                                }
                            })
                            .context(MainActivity.this)
                            .defaultDate(mYear, mMonth, mDay)
                            .maxDate(2050, 0, 1)
                            .minDate(2010, 0, 1)
                            .build()
                            .show();
                }
            });

           // Go to calendar activity
           calendarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent calendarIntent = new Intent(MainActivity.this, mCalendar.class);
                    startActivity(calendarIntent);
                }
            });

           if(PFI || savedSettings) {
               ShiftCalculation myShift = new ShiftCalculation(this);
               mString = doTheCalcs(cal, myShift);
               outputText.setText(getString(R.string.outputText) + " " + mDay + "/" + (mMonth + 1) + "/" + mYear + " " + getString(R.string.isString) + " " +mString);
               showIcon(mString);
           }

        }


    // Create Menu
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
                Intent intentCalendar = new Intent(MainActivity.this, mCalendar.class);
                startActivity(intentCalendar);
                return true;
            case R.id.Help:
                Intent intentHelp = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intentHelp);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    // method to calculate the output and return shift as a string
    private String doTheCalcs(Calendar cal2, ShiftCalculation myShift) {
        String vardiaOut = null;
        cal2.set(mYear, mMonth, mDay); //set the chosen day on calendar
        chosenDoy = cal2.get(Calendar.DAY_OF_YEAR); //get day of year of chosen date
        myShift.calculation(mYear, chosenDoy);

        // Run the appropriate method to calculate the shift
        if(PFI) {
            vardiaOut = myShift.getPFIShift();
        }
        else if(savedSettings)
            vardiaOut=myShift.getShift();
        return vardiaOut;
    }

    private void showIcon(String result) {
        if(result==getString(R.string.Evening) || result==getString(R.string.resultEvening) || result==getString(R.string.Evening))
            outputImage.setImageResource(shiftIcons[0]);
        else if(result==getString(R.string.Morning) || result==getString(R.string.resultMorning) || result==getString(R.string.Morning))
            outputImage.setImageResource(shiftIcons[1]);
        else if(result==getString(R.string.Night) || result==getString(R.string.resultNight))
            outputImage.setImageResource(shiftIcons[2]);
        else
            outputImage.setImageResource(shiftIcons[3]);
    }

}