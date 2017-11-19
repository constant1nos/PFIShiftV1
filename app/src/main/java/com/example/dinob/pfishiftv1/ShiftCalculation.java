package com.example.dinob.pfishiftv1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by dinob on 1/10/2017.
 */

public class ShiftCalculation{
    private int chosenDay;
    private String chosenShift;
    private static Context mcontext;

    private int startCycle, year;
    private int[] prefInts = new int[10];
    private String[] prefStrings = new String[10];
    private int i; // counter

    // Constructor
    public ShiftCalculation() {
        chosenDay=0;
        i=0;
        chosenShift="NoInput";
    }

    // Set Context so i can use getString(R.string.myVar)
    public void setmContext(Context context){
        this.mcontext=context;
    }


    // Day of year after user input calculation
    public void calculation(int mYear, int cDoy) {

        String[] preferencesTagsInt = {"shift1","shift2","shift3","shift4","shift5","shift6","shift7","shift8","shift9","shift10"};
        String[] preferencesTagsStrings = {"sShift1","sShift2","sShift3","sShift4","sShift5","sShift6","sShift7","sShift8","sShift9","sShift10"};

        appPreferences checkPref = appPreferences.getInstance(mcontext);
        startCycle = checkPref.getPref("startCycle",startCycle);
        year = checkPref.getPref("year",year);
        i=-1;
        do {
            i++;
            prefInts[i] = checkPref.getPref(preferencesTagsInt[i],prefInts[i]);
            prefStrings[i] = checkPref.getPref(preferencesTagsStrings[i],prefStrings[i]);
        } while(prefStrings[i] != null);

        int lastShift = prefInts[i-1];

        if (year < mYear) {
            chosenDay = ((mYear - year) * 365) + cDoy - startCycle;
        } else {
            chosenDay = cDoy - startCycle;
        }
        if(chosenDay>=lastShift)
            chosenDay=chosenDay%(lastShift+1);
        Log.d("chosenDay"," "+chosenDay);

    }

    // The result stored in a String
    public String getPFIShift() {
        if(chosenDay<7)
            chosenShift=mcontext.getString(R.string.resultEvening);
        else if(chosenDay==7)
            chosenShift=mcontext.getString(R.string.resultDayOff1);
        else if (chosenDay>7 && chosenDay<15)
            chosenShift=mcontext.getString(R.string.resultMorning);
        else if(chosenDay==15)
            chosenShift=mcontext.getString(R.string.resultDayOff2);
        else if(chosenDay>15 && chosenDay<23)
            chosenShift=mcontext.getString(R.string.resultNight);
        else if(chosenDay>=23 && chosenDay<28)
            chosenShift=mcontext.getString(R.string.result5DaysOff);
        else
            chosenShift="Error!";
        return chosenShift;
    }

    // The result stored in a String
    public String getShift() {
        if (chosenDay>=0 && chosenDay<=prefInts[0])
            chosenShift = prefStrings[0];
        for (int j = 0; j < i-1; j++) {
            Log.d("prefInts[j]", " " + prefInts[j]+" : "+j);
            if (chosenDay>prefInts[j] && chosenDay<=prefInts[j+1])
                chosenShift = prefStrings[j+1];
            Log.d("chosenShift", " " + chosenShift);
        }
        Log.d("final chosenShift", " " + chosenShift);
        return chosenShift;
    }

}
