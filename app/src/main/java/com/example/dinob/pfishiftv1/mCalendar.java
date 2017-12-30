package com.example.dinob.pfishiftv1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnNavigationButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class mCalendar extends AppCompatActivity {

    // Initialise views
    CalendarView mCalendar;
    TextView textOut;
    ProgressBar dialog;

    // Initialise local variables
    Calendar cal=Calendar.getInstance(); //get current date
    int mDay=cal.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
    int mMonth = cal.get(Calendar.MONTH);
    int mYear=cal.get(Calendar.YEAR);
    boolean PFI, savedSettings;
    int chosenDoy; //this is to find day of year after user has set the date
    int startFrom=0, stopTo=1460; // values to arrange icons on Calendar
    private final int[] shiftIcons = {R.drawable.afternoon,R.drawable.test,R.drawable.night,R.drawable.off};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_calendar);

        getSupportActionBar().setTitle(R.string.menu_calendar);

        Context context = this;
        appPreferences checkPref = appPreferences.getInstance(context);

        // Check if stored settings found
        PFI = checkPref.getPref("PFIShift", PFI); // for PFI shift
        savedSettings = checkPref.getPref("savedSettings", savedSettings); // for general shift

        // Definition of views
        mCalendar = (CalendarView) findViewById(R.id.calendarView);
        textOut = (TextView) findViewById(R.id.textOutput);
        dialog = (ProgressBar) findViewById(R.id.progressBar);

        // On specific date on calendar click listener
        mCalendar.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                mDay = clickedDayCalendar.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
                mMonth = clickedDayCalendar.get(Calendar.MONTH);
                mYear = clickedDayCalendar.get(Calendar.YEAR);
                ShiftCalculation shift = new ShiftCalculation(getApplicationContext());
                textOut.setText(getString(R.string.outputText) + " " + mDay + "/" + (mMonth + 1) + "/" + mYear + " " + getString(R.string.isString) + " " + doTheCalcs(clickedDayCalendar, shift));
            }
        });

        // On next month button clicked OR scrolled listener
        mCalendar.setOnForwardButtonClickListener(new OnNavigationButtonClickListener() {
            @Override
            public void onClick() {
                if (PFI || savedSettings) {
                    //startFrom+=30;
                    //stopTo+=30;
                }
            }
        });

        // On previous month button clicked OR scrolled listener
        mCalendar.setOnPreviousButtonClickListener(new OnNavigationButtonClickListener() {
            @Override
            public void onClick() {
                //startFrom-=30;
                //stopTo-=30;
            }
        });

        mCalendar.setDate(cal); // set current date

        // Enable asyncTask for long background job
        if (PFI || savedSettings) {
            mCalendar.AsyncTaskCalendarSetEvent doTheJob = new mCalendar.AsyncTaskCalendarSetEvent();
            doTheJob.execute(cal);
        }
    }
    // method to calculate the output and return shift as a string
    public String doTheCalcs(Calendar cal2, ShiftCalculation myShift) {
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

    // AsyncTask class for heavy tasks without affecting UI
    private class AsyncTaskCalendarSetEvent extends AsyncTask<Calendar, Integer, List<EventDay>> {

        ShiftCalculation myShift = new ShiftCalculation(getApplicationContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setVisibility(View.VISIBLE);
            textOut.setText(getString(R.string.data_loading));
        }

        @Override
        protected List<EventDay> doInBackground(Calendar... calendars) {

            String temp;
            List<EventDay> events = new ArrayList<>();

            for(int i=startFrom;i<stopTo;i++) {
                calendars[0] = Calendar.getInstance();
                calendars[0].add(Calendar.DATE, i);
                temp = customizeCalendar(calendars[0], myShift);

                if(temp==getString(R.string.Evening) || temp==getString(R.string.resultEvening) || temp==getString(R.string.Evening))
                    events.add(new EventDay(calendars[0], shiftIcons[0]));
                else if(temp==getString(R.string.Morning) || temp==getString(R.string.resultMorning) || temp==getString(R.string.Morning))
                    events.add(new EventDay(calendars[0], shiftIcons[1]));
                else if(temp==getString(R.string.Night) || temp==getString(R.string.resultNight))
                    events.add(new EventDay(calendars[0], shiftIcons[2]));
                else
                    events.add(new EventDay(calendars[0], shiftIcons[3]));
                //publishProgress(i);
            }
            return events;
        }

        private String customizeCalendar(Calendar cal1, ShiftCalculation myShift) {
            String vardiaOut = null;
            mDay=cal1.get(Calendar.DAY_OF_MONTH); //initialise local variables to hold date type
            mMonth = cal1.get(Calendar.MONTH);
            mYear=cal1.get(Calendar.YEAR);
            chosenDoy = cal1.get(Calendar.DAY_OF_YEAR); //get day of year of chosen date
            myShift.calculation(mYear, chosenDoy);

            // Run the appropriate method to calculate the shift
            if(PFI) {
                vardiaOut = myShift.getPFIShift();
            }
            else if(savedSettings)
                vardiaOut=myShift.getShift();
            return vardiaOut;
        }

        @Override
        protected void onProgressUpdate (Integer...values){
            //dialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<EventDay> eventDays) {
            super.onPostExecute(eventDays);
            mCalendar.setEvents(eventDays);
            dialog.setVisibility(View.INVISIBLE);
            textOut.setText("");
        }
    }


}
