package com.example.dinob.pfishiftv1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Settings extends AppCompatActivity  {

    /*** Initialise variables and data ***/
    RadioButton enablePFI;
    Button checkShift, newCycle, clearData;
    TextView PFIHint1;

    // Using string array to set easier shared prefernces, one for integers and one for strings
    String[] preferencesTagsInt = {"shift1","shift2","shift3","shift4","shift5","shift6","shift7","shift8","shift9","shift10"};
    String[] preferencesTagsStrings = {"sShift1","sShift2","sShift3","sShift4","sShift5","sShift6","sShift7","sShift8","sShift9","sShift10"};
    // Default values to store for PFI shift ONLY
    int[] PFIDefaultValues = {6, 7, 14, 15, 22, 27};

    // Calendar data to calculate PFI's parameters
    Calendar cal=Calendar.getInstance(); //get current date
    int doy=cal.get(Calendar.DAY_OF_YEAR);
    int dow1=cal.get(Calendar.DAY_OF_WEEK);
    int y=cal.get(Calendar.YEAR);

    String vardiaPFI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Definition of views
        checkShift = (Button) findViewById(R.id.checkShift);
        newCycle = (Button) findViewById(R.id.newCycle);
        clearData = (Button) findViewById(R.id.clear);
        PFIHint1 = (TextView) findViewById(R.id.textSettingsHint1);
        enablePFI = (RadioButton) findViewById(R.id.enablePFIButton);

        // Change Actionbar title and hide PFI views under RadioButton enablePFI
        PFIHint1.setVisibility(TextView.INVISIBLE);
        checkShift.setVisibility(Button.INVISIBLE);
        getSupportActionBar().setTitle(getString(R.string.menu_settings));

        // Create shared preferences file
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);

        /** On RadioButton PFI listener **/
        enablePFI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkShift.setVisibility(Button.VISIBLE);
                PFIHint1.setVisibility(TextView.VISIBLE);

                // On check shift under RadioButton listener
                checkShift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String shift[] = new String[]{getString(R.string.Evening), getString(R.string.DayOff1), getString(R.string.Morning), getString(R.string.DayOff2), getString(R.string.Night),getString( R.string.DaysOff5)};
                        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                        builder.setTitle(getString(R.string.AlertMsg));
                        builder.setItems(shift, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on shift[which]
                                vardiaPFI=shift[which];

                                // Calculate startCycle for PFI shift
                                int startCycle = findZeroDay(dow1,vardiaPFI);

                                // Store default values from table
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("PFIShift", true);
                                editor.putInt("startCycle",startCycle);
                                editor.putInt("year",y);
                                for(int i = 0; i<=5; i++){
                                    editor.putInt(preferencesTagsInt[i],PFIDefaultValues[i]);
                                    editor.putString(preferencesTagsStrings[i],shift[i]);
                                }
                                editor.apply();

                                // After saving data, go to Main Activity
                                Toast.makeText(getApplicationContext(),getString(R.string.data_saved),Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(Settings.this,MainActivity.class);
                                startActivity(mainIntent);
                            }
                        });
                        builder.show();
                    }
                });
            }
        });

        /** When new cycle button pressed, go to SecondActivity **/
        newCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setCycle = new Intent(Settings.this,SecondActivity.class);
                startActivity(setCycle);
            }
        });

        // Clear data button listener
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("startCycle", 0); // stores first dayOfYear of shift cycle
                editor.putInt("year", 0); // stores year
                editor.putBoolean("PFIShift", false);
                editor.putBoolean("savedSettings",false);
                for(int i = 0; i<10; i++) {
                    editor.putInt(preferencesTagsInt[i],0);
                    editor.putString(preferencesTagsStrings[i], null);
                }
                editor.apply();
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
