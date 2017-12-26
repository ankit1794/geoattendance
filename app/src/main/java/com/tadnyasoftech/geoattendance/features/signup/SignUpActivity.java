package com.tadnyasoftech.geoattendance.features.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.db.DatabaseReferenceManager;
import com.tadnyasoftech.geoattendance.models.Attendance;
import com.tadnyasoftech.geoattendance.models.Client;
import com.tadnyasoftech.geoattendance.models.User;
import com.tadnyasoftech.geoattendance.utils.GEUtility;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import me.rohanpeshkar.helper.HelperActivity;

import static com.tadnyasoftech.geoattendance.R.id.Email;
import static com.tadnyasoftech.geoattendance.R.id.companyname;
import static com.tadnyasoftech.geoattendance.R.id.EdtRePassword;
import static com.tadnyasoftech.geoattendance.R.id.EdtRePassword;
import static com.tadnyasoftech.geoattendance.R.id.EdtRePassword;
import static com.tadnyasoftech.geoattendance.R.id.edt_password;
import static com.tadnyasoftech.geoattendance.R.id.name;
import static com.tadnyasoftech.geoattendance.R.id.password;
import static com.tadnyasoftech.geoattendance.R.id.radioButton;
import static com.tadnyasoftech.geoattendance.R.id.radioButton2;
import static com.tadnyasoftech.geoattendance.R.id.repassword;


/**
 * Created by dell on 18/12/17.
 */

public class SignUpActivity extends HelperActivity {

    private FirebaseAuth firebaseAuth;

    private User gUser;

    private DatabaseReference mDatabase;


    @BindView(R.id.ed_Name)
    EditText edtFullname;

    @BindView(R.id.name)
    TextInputLayout ilFullName;


    @BindView(R.id.rgp_gender)
    RadioGroup rgpGender;

    @BindView(R.id.ed_companyName)
    EditText edtCompanyname;

    @BindView(R.id.companyname)
    TextInputLayout ilCompanyName;

    @BindView(R.id.edt_email)
    EditText edtemail;

    @BindView(R.id.Email)
    TextInputLayout ilEmail;

    @BindView(R.id.edtpassword)
    EditText edtpassword;

    @BindView(R.id.password)
    TextInputLayout ilPassword;

    @BindView(R.id.EdtRePassword)
    EditText EdtRePassword;

    @BindView(R.id.repassword)
    TextInputLayout ilRePassword;


    @Override
    protected void create() {
        setTitle("New Registration");
        enableHome();
        gUser = new User();

    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick()

    {
        String fullName = edtFullname.getText().toString().trim();
        if (TextUtils.isEmpty(fullName)) {
            ilFullName.setError("Enter valid Name");
            return;
        } else {
            ilFullName.setErrorEnabled(false);
        }


        //String gender1=rgpGender.toString().trim();
        //String gender = rgpGender.toString().trim();
        //boolean isIndividual = false;
        String gender = GEUtility.getCheckedRadioText(rgpGender);

        if (rgpGender.getCheckedRadioButtonId() == -1) {
            showToast("Select Gender");
            return;
        }


        String CompanyName = edtCompanyname.getText().toString().trim();
        if (TextUtils.isEmpty(CompanyName)) {

            ilCompanyName.setError("Enter Company Name");
            return;
        } else {
            ilCompanyName.setErrorEnabled(false);
        }

        String email = edtemail.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            ilEmail.setError("Enter valid Email Id");
            return;
        } else {
            ilEmail.setErrorEnabled(false);
        }

        String password = edtpassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ilPassword.setError("Enter Password");

            //showToast("Enter your Registered Email");
            return;
        } else {
            ilPassword.setErrorEnabled(false);
        }


        String RePassword = EdtRePassword.getText().toString().trim();
        if (TextUtils.isEmpty(RePassword)) {

            ilRePassword.setError("Enter Password Again");
            return;
        } else {
            ilRePassword.setErrorEnabled(false);
        }


        if (!RePassword.equals(password)) {

            ilRePassword.setError("Passwords do not match");
            edtpassword.requestFocus();
            return;

        } else {
            ilRePassword.setErrorEnabled(false);
        }


        setPersonalDetails(fullName, gender, CompanyName, email);
        signUp(email, password);


    }


    public void setPersonalDetails(String fullName, String gender, String companyName,
                                   String email) {
        gUser.setFullName(fullName);
        gUser.setGender(gender);
        gUser.setCompanyName(companyName);
        gUser.setEmail(email);
    }


    public void signUp(String email, String password) {
        showProgressDialog("Creating Acccount", false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(createAccountTask -> {

                    if (createAccountTask.isSuccessful()) {
                        String uid = createAccountTask.getResult().getUser().getUid();
                        writeNewUser(uid);


                    } else {
                        dismissProgressDialog();
                        //Account creation failed showing error
                        String errorMsg = createAccountTask.getException().getMessage();
                        //showToast("Failure " + errorMsg, Toast.LENGTH_LONG);
                        new MaterialDialog.Builder(this)
                                .title("Failure")
                                .content(errorMsg)
                                .positiveText("Continue")
                                .negativeText("Back")
                                .show();


                    }
                });


    }

    public void writeNewUser(String uid) {
        DatabaseReferenceManager.getInstance().getUsersReference().child(uid)
                .setValue(gUser)
                .addOnCompleteListener(dataUpdateTask -> {
                    dismissProgressDialog();
                    if (dataUpdateTask.isSuccessful()) {

                        new MaterialDialog.Builder(this)
                                .title("Registered")
                                .content("Dashboard")
                                .positiveText("Go to Dashboard Activity")
                                .negativeText("Back")
                                .show();

                    } else {

                        showToast("Fail..Please Try Again", Toast.LENGTH_LONG);
                    }

                });
    }

    /*public void goToDashboard(){
        launch(EmployeeDashboardActivity.class);
    }*/


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


