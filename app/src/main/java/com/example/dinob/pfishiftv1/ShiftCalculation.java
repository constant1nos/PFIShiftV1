package com.example.dinob.pfishiftv1;

import android.content.Context;

/**
 * Created by dinob on 1/10/2017.
 */

public class ShiftCalculation{
    private int dayOfWeek;
    private int zeroDay;
    private int chosenDay;
    private String chosenShift;
    private static Context mcontext;

    // Constructor
    public ShiftCalculation() {
        dayOfWeek=0;
        zeroDay=0;
        chosenDay=0;
        chosenShift="NoInput";
    }

    // Set Context so i can use getString(R.string.myVar)
    public void setmContext(Context context){
        this.mcontext=context;
    }

    // First day of shift's cycle calculation
    public void findZeroDay(int dow, String vardiaNow) {
        if(mcontext.getString(R.string.Evening) == vardiaNow) {
            if(dow>0 && dow<4)
                zeroDay=dow+3;
            else
                zeroDay=dow-4;
        }
        else if(vardiaNow == mcontext.getString(R.string.DayOff1))
            zeroDay=7;
        else if(vardiaNow == mcontext.getString(R.string.Morning)) {
            if(dow>0 && dow<5)
                zeroDay=dow+10;
            else
                zeroDay = dow + 3;
        }
        else if(vardiaNow == mcontext.getString(R.string.DayOff2))
            zeroDay=15;
        else if(vardiaNow == mcontext.getString(R.string.Night)){
            if(dow>0 && dow<6)
                zeroDay=dow+17;
            else
                zeroDay=dow+10;
        }
        else if (vardiaNow == mcontext.getString(R.string.DaysOff5)) {
            if(dow>0 && dow<4)
                zeroDay=dow+24;
            else
                zeroDay=dow+17;
        }
        else
            zeroDay=0;
    }

    // Day of year after user input calculation
    public void calculation(int uDayYear, int uYear, int mYear, int cDoy) {
        int startCycle = uDayYear - zeroDay;
        if (uYear < mYear) {
            chosenDay = ((mYear - uYear) * 365) + cDoy - startCycle;
        } else {
            chosenDay = cDoy - startCycle;
        }
        if(chosenDay>=28)
            chosenDay=chosenDay%28;
    }

    // The result stored in a String
    public String getShift() {
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

}
