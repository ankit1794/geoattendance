package com.tadnyasoftech.geoattendance.features.authentication;

import android.app.Activity;

import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.features.signup.SignUpActivity;

import butterknife.OnClick;
import me.rohanpeshkar.helper.HelperActivity;

/**
 * Created by dell on 18/12/17.
 */

public class LoginActivity extends HelperActivity {


    @Override
    protected void create() {

    }

    @OnClick(R.id.txt_action_sign_up)
    void onClickSignUp(){
        launch(SignUpActivity.class);
    }

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean isToolbarPresent() {
        return false;
    }

}
