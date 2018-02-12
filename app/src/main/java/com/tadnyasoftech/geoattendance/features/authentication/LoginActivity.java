package com.tadnyasoftech.geoattendance.features.authentication;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.features.employee_dashboard.EmployeeDashboardActivity;
import com.tadnyasoftech.geoattendance.features.signup.SignUpActivity;
import com.tadnyasoftech.geoattendance.utils.GEDialogUtility;
import com.tadnyasoftech.geoattendance.utils.GEUtility;

import butterknife.BindView;
import butterknife.OnClick;
import me.rohanpeshkar.helper.HelperActivity;
import me.rohanpeshkar.helper.HelperUtils;

/**
 * Created by dell on 18/12/17.
 */

public class LoginActivity extends HelperActivity {

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    private FirebaseAuth mFirebaseAuth;

    private int PERMISSION_ALL_REQ_CODE = 101;

    private String[] PERMISSIONS_NEEDED;


    @Override
    protected void create() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        PERMISSIONS_NEEDED = HelperUtils.getManifestPermissions(this);
        if (HelperUtils.hasPermissions(this, PERMISSIONS_NEEDED)) {
            handleRedirection();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_NEEDED,
                    PERMISSION_ALL_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ALL_REQ_CODE && (grantResults.length > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleRedirection();
        } else {
            showToast(getString(R.string.permission_not_granted_msg));
            finishAffinity();
        }
    }

   private void handleRedirection(){
       FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
       if(firebaseUser!=null){
           launch(EmployeeDashboardActivity.class);
           finish();
       }
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
                        GEUtility.saveUid(LoginActivity.this,uid);
                        launch(EmployeeDashboardActivity.class);
                        finish();
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
