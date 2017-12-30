package com.example.dinob.pfishiftv1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This is a Singleton Class to manage and maintain data
 * stored in shared preferences XML.
 * Very handy for other future apps
 * Created by dbatz 17/11/2017
 */

public class appPreferences {

    private final SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;
    private static appPreferences instance;
    private final String[] preferencesTagsValues = {"shift1","shift2","shift3","shift4","shift5","shift6","shift7","shift8","shift9","shift10"};
    private final String[] preferencesTagsShift = {"sShift1","sShift2","sShift3","sShift4","sShift5","sShift6","sShift7","sShift8","sShift9","sShift10"};

    // Constructor
    private appPreferences(Context context) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();
    }

    // Method to hold only one instance
    public static appPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new appPreferences(context.getApplicationContext());
        }
        return instance;
    }

    public void savePref(String key, int var){
        sharedPrefEditor.putInt(key, var);
        sharedPrefEditor.apply();
    }

    public void savePref(String key, String var) {
        sharedPrefEditor.putString(key, var);
        sharedPrefEditor.apply();
    }

    public void savePref(String key, boolean var) {
        sharedPrefEditor.putBoolean(key, var);
        sharedPrefEditor.apply();
    }

    public int getPref(String key, int var) {
        return sharedPref.getInt(key, var);
    }

    public String getPrefStringTagFromArrayValues(int i) {
        return preferencesTagsValues[i];
    }

    public String getPrefStringTagFromArrayShifts(int i) {
        return preferencesTagsShift[i];
    }

    public String getPref(String key, String var) {
        return sharedPref.getString(key, var);
    }

    public Boolean getPref(String key, Boolean var) {
        return sharedPref.getBoolean(key, var);
    }
}
