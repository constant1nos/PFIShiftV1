package com.example.dinob.pfishiftv1;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by dinob on 18/10/2017.
 */

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        FragmentSettings f1 = new FragmentSettings();
       // f1.setmContext(this);
        FragmentTransaction fc = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
        fc.add(R.id.InitialFrag, f1);
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
        fc.commit();

    }
}
