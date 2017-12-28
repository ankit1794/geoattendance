package com.tadnyasoftech.geoattendance.features.authentication;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.features.signup.SignUpActivity;
import com.tadnyasoftech.geoattendance.utils.GEDialogUtility;

import butterknife.BindView;
import butterknife.OnClick;
import me.rohanpeshkar.helper.HelperActivity;

/**
 * Created by dell on 18/12/17.
 */

public class LoginActivity extends HelperActivity {

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void create() {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_login)
    void onClick() {
        String email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            GEDialogUtility.getErrorDialog(this,"Enter Proper Email ID").show();
            return;
        }
        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            GEDialogUtility.getErrorDialog(this,"Enter Proper Password").show();
            return;
        }

        login(email, password);
    }

    @OnClick(R.id.txt_forgot_password)
    void onClickForgot() {
        GEDialogUtility.getResetPasswordDialog(this, (dialog, input) -> {
            String email = input.toString().trim();
            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Enter valid email");
            } else {
                dialog.dismiss();
                initiateResetPassword(email);
            }
        }).show();
    }

    public void initiateResetPassword(String email) {
        mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                GEDialogUtility.getSuccessDialog(this,"Email sent").show();
            } else {
                GEDialogUtility.getErrorDialog(this,"Invalid Email").show();
            }
        });
    }

    public void login(String email, String password) {
        showProgressDialog("Authenticating...", false);
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authResultTask -> {
                    dismissProgressDialog();
                    if (authResultTask.isSuccessful()) {
                        String uid = authResultTask.getResult().getUser().getUid();
                        GEDialogUtility.getSuccessDialog(this,
                                "Dashboard Activity").show();
                    } else {
                        String errorMessage = authResultTask.getException().getMessage();
                        GEDialogUtility.getErrorDialog(this,
                                errorMessage).show();
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
