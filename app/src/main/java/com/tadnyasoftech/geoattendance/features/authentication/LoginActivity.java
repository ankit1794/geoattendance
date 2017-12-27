package com.tadnyasoftech.geoattendance.features.authentication;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.features.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.OnClick;
import me.rohanpeshkar.helper.HelperActivity;

/**
 * Created by dell on 18/12/17.
 */

public class LoginActivity extends HelperActivity {


    private FirebaseAuth firebaseAuth;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.edt_password)
    EditText edtPassword;


    @Override
    protected void create() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_login)
    void onClick() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {

            new MaterialDialog.Builder(this)
                    .title(R.string.title2)
                    .content(R.string.content2)
                    .positiveText(R.string.agree2)
                    .show();
            //showToast("Enter your Registered Email");
            return;
        }

        if (TextUtils.isEmpty(password)) {

            new MaterialDialog.Builder(this)
                    .title(R.string.title3)
                    .content(R.string.content3)
                    .positiveText(R.string.agree3)
                    .negativeText(R.string.disagree2)
                    .show();
            //showToast("Enter Password");
            return;
        }
        login(email, password);
    }


    public void login(String email, String password) {
        showProgressDialog("Authenticating...", false);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authResultTask -> {
                    dismissProgressDialog();
                    if (authResultTask.isSuccessful()) {
                        String uid = authResultTask.getResult().getUser().getUid();
                        new MaterialDialog.Builder(this)
                                .title(R.string.title1)
                                .content(R.string.content1)
                                .positiveText("Continue")
                                .negativeText("Back")
                                .show();
                        //showToast("Success", Toast.LENGTH_LONG);
                        //loadUserDetails(uid);
                    } else {
                        //LoginView.dismissProgress();
                        String errorMessage = authResultTask.getException().getMessage();
                        new MaterialDialog.Builder(this)
                                .title(R.string.title)
                                .content(R.string.content)
                                .positiveText(R.string.agree)
                                .show();

                        //showToast("Not Success", Toast.LENGTH_LONG);

                    }
                });
    }


    @OnClick(R.id.txt_action_sign_up)
    void onClickSignUp() {
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
