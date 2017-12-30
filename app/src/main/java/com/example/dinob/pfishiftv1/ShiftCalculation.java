package com.example.dinob.pfishiftv1;

import android.content.Context;

/**
 * Created by dinob on 1/10/2017.
 */

public class ShiftCalculation{
    private int chosenDay;              // used for user's chosen date calculation
    private String chosenShift;         // storing the result as a string
    private static Context mcontext;    // needed to use getString method

    private int startCycle, year;
    private int[] prefInts = new int[10];       // stores data table from preferences array with values
    private int[] prefShift = new int[10];      // stores date from preferences pointing the shift (0,1,2,3)
    private int i;                              // counter

    // Constructor
    public ShiftCalculation(Context context) {
        mcontext=context;
        chosenDay=0;
        chosenShift="NoInput";
        String prefString;      // stores string from preferences final arrays (see appPreferences class)

        appPreferences checkPref = appPreferences.getInstance(mcontext);    // Initialise using Singleton class
        startCycle = checkPref.getPref("startCycle",startCycle);       // Get stored data from preferences
        year = checkPref.getPref("year",year);
        i=-1; //starting value

        // Collect data and store them to local variables to work with
        do {
            i++; //counting from zero
            prefString = checkPref.getPrefStringTagFromArrayValues(i);
            prefInts[i] = checkPref.getPref(prefString,prefInts[i]);
            prefString = checkPref.getPrefStringTagFromArrayShifts(i);
            prefShift[i] = checkPref.getPref(prefString,prefShift[i]);
        } while(prefShift[i] != -1);
    }

    // Set Context so i can use getString(R.string.myVar)
    public void setmContext(Context context){
        this.mcontext=context;
    }


    // Day of year after user input calculation
    public void calculation(int mYear, int cDoy) {

        int lastShift = prefInts[i-1];      // i-1 because i points to nothing stored

        if (year < mYear) {
            if(mYear > 2020 || mYear == 2025 || mYear == 2028 || mYear == 2032 || mYear == 2036 || mYear == 2040) // disekta eti
                chosenDay = (((mYear - year) * 365) + 1) + cDoy - startCycle;
            else
                chosenDay = ((mYear - year) * 365) + cDoy - startCycle;
        } else {
            chosenDay = cDoy - startCycle;
        }
        if(chosenDay>=lastShift)
            chosenDay=chosenDay%(lastShift+1);
    }

    /** Result String for PFI ONLY **/
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

    /** Result for custom shift **/
    // The result stored in a String
    public String getShift() {
        int temp=-1;
        if (chosenDay>=0 && chosenDay<=prefInts[0])
            temp = prefShift[0];
        for (int j = 0; j < i-1; j++) {
            if (chosenDay>prefInts[j] && chosenDay<=prefInts[j+1])
                temp = prefShift[j+1];
        }
        switch (temp) {
            case 0:
                chosenShift=mcontext.getString(R.string.Morning);
                break;
            case 1:
                chosenShift=mcontext.getString(R.string.Evening);
                break;
            case 2:
                chosenShift=mcontext.getString(R.string.Night);
                break;
            case 3:
                chosenShift=mcontext.getString(R.string.DayOff);
                break;
            default:
                chosenShift="Error";
                break;
        }
        return chosenShift;
    }

}
