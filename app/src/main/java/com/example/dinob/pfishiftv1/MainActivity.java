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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements
            View.OnClickListener {

    // Initialise my variables
    Button myButton2; //2 buttons
    TextView textDate, output; //4 textViews
    private int mYear=0, mMonth=0, mDay=0; //int variables to hold the date
    String vardiaNow, vardiaOut; //Strings for the text
    Calendar cal=Calendar.getInstance(); //get current date
    int dom=cal.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
    int m = cal.get(Calendar.MONTH);
    int y=cal.get(Calendar.YEAR);
    int chosenDoy; //this is to find day of year after user has set the date
    ShiftCalculation myShift = new ShiftCalculation();

    int savedSC, savedYear, prefInt;
    String prefString;
    boolean PFI,savedSettings;


        // Set views and listeners onCreate
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //Check if app runs for the first time
          // TODO
            Context context = this;
            final SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);

            //prefInt=sharedPref.getInt("shift1",prefInt);
            //prefString=sharedPref.getString("sShift1",prefString);
            //vardiaNow=sharedPref.getString("PFIShift",vardiaNow);
            PFI = sharedPref.getBoolean("PFIShift",PFI);
            savedSettings = sharedPref.getBoolean("savedSettings",savedSettings);
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
            myButton2 = (Button) findViewById(R.id.checkDate);
            textDate = (TextView) findViewById(R.id.textDate);
            output = (TextView) findViewById(R.id.textOutput);

            //output.setText("start date: "+savedSC+", year: "+savedYear+", shift1 int: "+prefInt+", shift: "+prefString+"PFIShift: "+vardiaNow);

            // Trigger listeners
            myButton2.setOnClickListener(this);

            if(mYear!=0 && mMonth!=0 && mDay!=0 && vardiaNow!=null) {
                doTheCalcs(); //in case screen rotation, if user gave inputs, just do the calcs
            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.About:
                // User chose the "Settings" item, show the app settings UI...
                Intent intentAbout = new Intent(MainActivity.this, About.class);
                startActivity(intentAbout);
                return true;
            case R.id.Calendar:
                Intent intentCalendar = new Intent(MainActivity.this, CustomCalendar.class);
                startActivity(intentCalendar);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
        @Override
        public void onClick(View v) {
            if (v == myButton2) {
                mYear = y;
                mMonth = m;
                mDay = dom;
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                textDate.setText(getString(R.string.textMsg2) + " " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                mDay=dayOfMonth;
                                mMonth=monthOfYear;
                                mYear=year;
                                // Call method to calculate the shift on this day
                                doTheCalcs();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        }
        // method to call my class and calculate the output
        public void doTheCalcs() {
            myShift.setmContext(this);
            cal.clear();
            cal.set(mYear, mMonth, mDay); //set the chosen day on calendar
            chosenDoy = cal.get(Calendar.DAY_OF_YEAR); //get day of year of chosen date
            myShift.calculation(mYear, chosenDoy);
            if(PFI)
                vardiaOut=myShift.getPFIShift();
            else if(savedSettings)
                vardiaOut=myShift.getShift();
            output.setText(getString(R.string.outputText)+" "+mDay+"/"+(mMonth+1)+"/"+mYear+" "+getString(R.string.isString)+" "+vardiaOut);
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