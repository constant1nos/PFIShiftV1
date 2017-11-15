package com.example.dinob.pfishiftv1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by dinob on 27/10/2017.
 */

public class FragmentTextSettings extends Fragment {

    TextView fragTest;
    ImageButton deleteFragment;
    int[] dates = new int[6];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_text_fragment, container, false);

        // Definition of views
        fragTest = (TextView) view.findViewById(R.id.settingsOutput);
        deleteFragment = (ImageButton) view.findViewById(R.id.deleteFragment);
        dates = getArguments().getIntArray("dates");
        fragTest.setText(getArguments().getString("resultText")+" "+dates[0]+
                "/"+dates[1] +"/"+dates[2]+" - "+dates[3]+"/"+dates[4]+"/"+dates[5]);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Listeners
        deleteFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFragment();
            }
        });
    }

    public void clearFragment() {
        FragmentTransaction fc = getActivity().getSupportFragmentManager().beginTransaction();
        fc.remove(this);
        fc.commit();

        // decrease buttonPressed counter when fragment removes and set previous delete button visible
        //TODO find another way to do this, this is not best practise
        SecondActivity mActivity = (SecondActivity) getActivity();
        mActivity.buttonPressed--;
        if (mActivity.buttonPressed >= 1)
            mActivity.fragmentLayout[mActivity.buttonPressed-1].deleteFragment.setVisibility(ImageButton.VISIBLE);
    }
}
