package com.tadnyasoftech.geoattendance.utils;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by dell on 26/12/17.
 */

public class GEUtility {

    public static String getCheckedRadioText(RadioGroup radioButtonGroup) {
        int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
        View radioButtonView = radioButtonGroup.findViewById(radioButtonID);
        int idx = radioButtonGroup.indexOfChild(radioButtonView);
        RadioButton radioButton = (RadioButton) radioButtonGroup.getChildAt(idx);
        return radioButton.getText().toString();
    }
}
