package com.example.dinob.pfishiftv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Settings extends AppCompatActivity  {

    /*** Initialise variables and data ***/
    Switch enablePFI;
    TextView newShiftPattern, clearDataText;


    // Default values to store for PFI shift ONLY
    int[] PFIDefaultValues = {6, 7, 14, 15, 22, 27};

    // Calendar data to calculate PFI's parameters
    Calendar cal=Calendar.getInstance(); //get current date
    int doy=cal.get(Calendar.DAY_OF_YEAR);
    int dow1=cal.get(Calendar.DAY_OF_WEEK);
    int y=cal.get(Calendar.YEAR);

    boolean bVar;
    String prefString;

    String vardiaPFI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Definition of views
        clearDataText = (TextView) findViewById(R.id.clearDataText);
        enablePFI = (Switch) findViewById(R.id.PFISwitch);
        newShiftPattern = (TextView) findViewById(R.id.createNewCycleText);

        // Change Actionbar title and hide PFI views under RadioButton enablePFI
        getSupportActionBar().setTitle(getString(R.string.menu_settings));

        // Create shared preferences file
        final appPreferences checkPref = appPreferences.getInstance(getApplicationContext());
        if(checkPref.getPref("PFIRadioButtonChecked",bVar))
            enablePFI.setChecked(true);


        /** On Switch PFI listener **/
        enablePFI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!enablePFI.isChecked()) {
                    clearDataText.performClick();
                } else {
                    final String shift[] = new String[]{getString(R.string.Evening), getString(R.string.DayOff1), getString(R.string.Morning), getString(R.string.DayOff2), getString(R.string.Night), getString(R.string.DaysOff5)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                    builder.setTitle(getString(R.string.AlertMsg));
                    builder.setItems(shift, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on shift[which]
                            vardiaPFI = shift[which];

                            // Calculate startCycle for PFI shift
                            int startCycle = findZeroDay(dow1, vardiaPFI);

                            // Store default values from table
                            checkPref.savePref("PFIRadioButtonChecked", true);
                            checkPref.savePref("PFIShift", true);
                            checkPref.savePref("startCycle", startCycle);
                            checkPref.savePref("year", y);
                            for (int i = 0; i <= 5; i++) {
                                prefString = checkPref.getPrefStringTagFromArrayValues(i);
                                checkPref.savePref(prefString, PFIDefaultValues[i]);

                            }
                            prefString = checkPref.getPrefStringTagFromArrayShifts(6);
                            checkPref.savePref(prefString, -1); // Flag to declare and of input data

                            // After saving data, go to Main Activity
                            Toast.makeText(getApplicationContext(), getString(R.string.data_saved), Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(Settings.this, MainActivity.class);
                            startActivity(mainIntent);
                        }
                    });
                    builder.show();
                }
            }
        });



        /** When new cycle button pressed, go to SecondActivity **/
        newShiftPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setCycle = new Intent(Settings.this,SecondActivity.class);
                startActivity(setCycle);
            }
        });

        // Clear data button listener
        clearDataText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPref.savePref("startCycle", 0); // stores first dayOfYear of shift cycle
                checkPref.savePref("year", 0); // stores year
                checkPref.savePref("PFIShift", false);
                checkPref.savePref("savedSettings",false);
                checkPref.savePref("PFIRadioButtonChecked",false);
                enablePFI.setChecked(false);
                for(int i = 0; i<10; i++) {
                    prefString = checkPref.getPrefStringTagFromArrayValues(i);
                    checkPref.savePref(prefString,0);
                    prefString = checkPref.getPrefStringTagFromArrayShifts(i);
                    checkPref.savePref(prefString, 0);
                }
                Toast.makeText(getApplicationContext(),getString(R.string.data_cleared),Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to adjust startCycle when PFI shift is selected
    private int findZeroDay(int dow, String vardiaNow) {
        // First day of shift's cycle calculation
        int zeroDay;
            if(getString(R.string.Evening) == vardiaNow) {
                if(dow>0 && dow<4)
                    zeroDay=dow+3;
                else
                    zeroDay=dow-4;
            }
            else if(vardiaNow == getString(R.string.DayOff1))
                zeroDay=7;
            else if(vardiaNow == getString(R.string.Morning)) {
                if(dow>0 && dow<5)
                    zeroDay=dow+10;
                else
                    zeroDay = dow + 3;
            }
            else if(vardiaNow == getString(R.string.DayOff2))
                zeroDay=15;
            else if(vardiaNow == getString(R.string.Night)){
                if(dow>0 && dow<6)
                    zeroDay=dow+17;
                else
                    zeroDay=dow+10;
            }
            else if (vardiaNow == getString(R.string.DaysOff5)) {
                if(dow>0 && dow<4)
                    zeroDay=dow+24;
                else
                    zeroDay=dow+17;
            }
            else {
                zeroDay = 0;
            }
            zeroDay=doy - zeroDay;
            return zeroDay;
        }

}
