package com.example.dinob.pfishiftv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_calendar);

        getSupportActionBar().setTitle(getString(R.string.menu_calendar));

        //TODO add materialCalendarView
    }
}
