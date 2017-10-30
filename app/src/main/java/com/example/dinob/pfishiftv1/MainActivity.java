package com.example.dinob.pfishiftv1;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    Button myButton1, myButton2; //2 buttons
    TextView textShift, textDate, output; //4 textViews
    private int mYear=0, mMonth=0, mDay=0; //int variables to hold the date
    String vardiaNow, vardiaOut; //Strings for the text
    Calendar cal=Calendar.getInstance(); //get current date
    int dom=cal.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
    int m = cal.get(Calendar.MONTH);
    int doy=cal.get(Calendar.DAY_OF_YEAR);
    int dow1=cal.get(Calendar.DAY_OF_WEEK);
    int y=cal.get(Calendar.YEAR);
    int chosenDoy; //this is to find day of year after user has set the date
    ShiftCalculation myShift = new ShiftCalculation();

        // Set views and listeners onCreate
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            vardiaNow=getString(R.string.dtString);

            // Used for screen rotation to keep data
            if(savedInstanceState != null){
                mYear = savedInstanceState.getInt("nYear");
                mMonth = savedInstanceState.getInt("nMonth");
                mDay = savedInstanceState.getInt("nDay");
                vardiaNow = savedInstanceState.getString("nVardia");
            }

            // Definition of views
            myButton1 = (Button) findViewById(R.id.checkVardia);
            myButton2 = (Button) findViewById(R.id.checkDate);
            textShift = (TextView) findViewById(R.id.textShift);
            textDate = (TextView) findViewById(R.id.textDate);
            output = (TextView) findViewById(R.id.textOutput);

            // Trigger listeners
            myButton1.setOnClickListener(this);
            myButton2.setOnClickListener(this);

            if(mYear!=0 && mMonth!=0 && mDay!=0 && vardiaNow!=getString(R.string.dtString)) {
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
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                return true;
            case R.id.About:
                return true;
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
                                Toast.makeText(getApplicationContext(),"Date saved",Toast.LENGTH_SHORT).show();
                                //check if the shift has a value from user
                                if(vardiaNow!=getString(R.string.dtString)) {
                                    doTheCalcs();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            } else {

                final String shift[] = new String[]{getString(R.string.Evening), getString(R.string.Morning), getString(R.string.Night), getString(R.string.DayOff1), getString(R.string.DayOff2),getString( R.string.DaysOff5)};

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.AlertMsg));
                builder.setItems(shift, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on shift[which]
                        textShift.setText(getString(R.string.textMsg1) + " " + shift[which]);
                        vardiaNow=shift[which];
                        Toast.makeText(getApplicationContext(),"Shift saved",Toast.LENGTH_SHORT).show();
                        //check if user has picked a day
                        if(mDay!=0 && mMonth!=0 && mYear!=0) {
                            doTheCalcs();
                        }
                    }
                });
                builder.show();
            }

        }
        // method to call my class and calculate the output
        public void doTheCalcs() {
            myShift.setmContext(this);
            myShift.findZeroDay(dow1, vardiaNow);
            cal.clear();
            cal.set(mYear, mMonth, mDay); //set the chosen day on calendar
            chosenDoy = cal.get(Calendar.DAY_OF_YEAR); //get day of year of chosen date
            myShift.calculation(doy, y, mYear, chosenDoy);
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