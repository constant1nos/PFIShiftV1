package com.example.dinob.pfishiftv1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import static com.example.dinob.pfishiftv1.R.id.fragmentResultText;

/**
 * Created by dinob on 18/10/2017.
 */

public class SecondActivity extends AppCompatActivity {
    Calendar myCal = Calendar.getInstance();
    int mYear = myCal.get(Calendar.YEAR);
    int mMonth = myCal.get(Calendar.MONTH);
    int mDay = myCal.get(Calendar.DAY_OF_MONTH);
    String selectedItem;
    TextView fragmentResultText;
    Button pickDate1Button, pickDate2Button, addButton, doneButton;
    int buttonPressed=0;
    final FragmentTextSettings[] fragmentLayout = {new FragmentTextSettings(), new FragmentTextSettings(), new FragmentTextSettings()};
    final int[] fragmentArray = {R.id.myFrag1, R.id.myFrag2, R.id.myFrag3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        final Spinner spinner = (Spinner) findViewById(R.id.setSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerShifts, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Define Views
        fragmentResultText = (TextView) findViewById(R.id.fragmentResultText);
        pickDate1Button = (Button) findViewById(R.id.buttonDate1);
        pickDate2Button = (Button) findViewById(R.id.buttonDate2);
        addButton = (Button) findViewById(R.id.addFragmentButton);
        doneButton = (Button) findViewById(R.id.removeButton);

        // Add listeners
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = spinner.getItemAtPosition(i).toString(); //this is your selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        //Log.d("SecondActivity OnCreate","Started");
        //Intent myIntent = getIntent();
        //addbuttonCounter = myIntent.getIntExtra("fButton",addbuttonCounter);
        //Log.d("addButtonCounter: ",""+addbuttonCounter);
        //if (savedInstanceState == null) {
            //Log.d("SavedInstanceState: ", "null");
           // FragmentSettings fi = new FragmentSettings();
            //FragmentTransaction fc1 = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
            //fc1.add(R.id.InitialFrag, fi);
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
            //fc1.commit();
        //}
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
                        //Todo what i want on DateSet
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        if(bView == pickDate1Button) {
            fragmentResultText.setText("Button 1 pressed");
        }
        else fragmentResultText.setText("Button 2 pressed");
    }

    // Do things when add button is pressed
    public void addButton() {
        Log.d("buttonPressed:",""+buttonPressed);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
        ft.replace(fragmentArray[buttonPressed], fragmentLayout[buttonPressed]);
// or ft.add(R.id.your_placeholder, new FooFragment());
//Complete the changes added above
        ft.commit();
        buttonPressed++;
    }
}
