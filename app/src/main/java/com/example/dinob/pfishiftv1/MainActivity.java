package com.example.dinob.pfishiftv1;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import static com.example.dinob.pfishiftv1.R.id.myText;


public class MainActivity extends AppCompatActivity implements
            View.OnClickListener {

    //Initialise my variables
    Button myButton1, myButton2; //2 buttons
    TextView a, textShift, textDate, output; //4 textViews
    private int mYear=0, mMonth=0, mDay=0; //int variables to hold the date
    String vardiaNow="NoInput", vardiaOut; //Strings for the text
    Calendar cal=Calendar.getInstance(); //get current date
    int dom=cal.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
    int m = cal.get(Calendar.MONTH);
    int doy=cal.get(Calendar.DAY_OF_YEAR);
    int dow1=cal.get(Calendar.DAY_OF_WEEK);
    int y=cal.get(Calendar.YEAR);
    int chosenDoy; //this is to find day of year after user has set the date
    ShiftCalculation myShift = new ShiftCalculation(); //my class

        //Set views and listeners onCreate
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            myButton1 = (Button) findViewById(R.id.checkVardia);
            myButton2 = (Button) findViewById(R.id.checkDate);
            a = (TextView) findViewById(myText);
            textShift = (TextView) findViewById(R.id.textShift);
            textDate = (TextView) findViewById(R.id.textDate);
            output = (TextView) findViewById(R.id.textOutput);

            myButton1.setOnClickListener(this);
            myButton2.setOnClickListener(this);
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
                                //check if the shift has a value from user
                                if(vardiaNow!="NoInput") {
                                    doTheCalcs();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            } else {

                final String shift[] = new String[]{"Evening", "Morning", "Night", "Day off 1", "Day off 2", "5 days off"};

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Pick your shift today");
                builder.setItems(shift, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on shift[which]
                        textShift.setText(getString(R.string.textMsg1) + " " + shift[which]);
                        vardiaNow=shift[which];
                        //check if user has picked a day
                        if(mDay!=0 && mMonth!=0 && mYear!=0) {
                            doTheCalcs();
                        }
                    }
                });
                builder.show();
            }

        }
        //method to call my class and calculate the output
        public void doTheCalcs() {
            myShift.findZeroDay(dow1, vardiaNow);
            cal.clear();
            cal.set(mYear, mMonth, mDay); //set the chosen day on calendar
            chosenDoy = cal.get(Calendar.DAY_OF_YEAR); //get day of year of chosen date
            myShift.calculation(doy, y, mYear, chosenDoy);
            vardiaOut=myShift.getShift();
            output.setText("Your shift is: "+vardiaOut);
        }

    }