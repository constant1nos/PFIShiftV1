package com.example.dinob.pfishiftv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by dinob on 27/10/2017.
 */

public class FragmentTextSettings extends Fragment {
    TextView fragTest;
    boolean viewCreated;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_text_fragment, container, false);
        fragTest = (TextView) view.findViewById(R.id.settingsOutput);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated=true;
    }

    public boolean getData(String var) {
        if(!viewCreated)
            return false;
        else {
            fragTest.setText(var);
            return true;
        }
    }
}
