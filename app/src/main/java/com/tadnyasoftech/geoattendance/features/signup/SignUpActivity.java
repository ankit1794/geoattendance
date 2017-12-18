package com.tadnyasoftech.geoattendance.features.signup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tadnyasoftech.geoattendance.R;

import me.rohanpeshkar.helper.HelperActivity;

/**
 * Created by dell on 18/12/17.
 */

public class SignUpActivity extends HelperActivity{


    @Override
    protected void create() {
        setTitle("New Registration");
        enableHome();
    }

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_signup;
    }

    @Override
    protected boolean isToolbarPresent() {
        return true;
    }

}


