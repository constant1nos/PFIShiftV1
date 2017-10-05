package com.example.dinob.pfishiftv1;

/**
 * Created by dinob on 1/10/2017.
 */

public class ShiftCalculation {
    private int dayOfWeek;
    private int zeroDay;
    private int chosenDay;
    private String chosenShift;

    public ShiftCalculation() {
        dayOfWeek=0;
        zeroDay=0;
        chosenDay=0;
        chosenShift="NoInput";
    }

    public void findZeroDay(int dow, String vardiaNow) {
        if(vardiaNow.equals("Evening")) {
            if(dow>0 && dow<4)
                zeroDay=dow+3;
            else
                zeroDay=dow-4;
        }
        else if(vardiaNow.equals("Day off 1"))
            zeroDay=7;
        else if(vardiaNow.equals("Morning")) {
            if(dow>0 && dow<5)
                zeroDay=dow+10;
            else
                zeroDay = dow + 3;
        }
        else if(vardiaNow.equals("Day off 2"))
            zeroDay=15;
        else if(vardiaNow.equals("Night")){
            if(dow>0 && dow<6)
                zeroDay=dow+17;
            else
                zeroDay=dow+10;
        }
        else if (vardiaNow.equals("5 days off")) {
            if(dow>1 && dow<4)
                zeroDay=dow+24;
            else
                zeroDay=dow+17;
        }
        else
            zeroDay=0;
    }

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

    public String getShift() {
        if(chosenDay<7)
            chosenShift="Evening 14:00-22:00";
        else if(chosenDay==7)
            chosenShift="Day off 1";
        else if (chosenDay>7 && chosenDay<15)
            chosenShift="Morning 06:00-14:00";
        else if(chosenDay==15)
            chosenShift="Day off 2";
        else if(chosenDay>15 && chosenDay<23)
            chosenShift="Night 22:00-06:00";
        else if(chosenDay>=23 && chosenDay<28)
            chosenShift="5 days off";
        else
            chosenShift="Error!";
        return chosenShift;
    }

}
