package com.tadnyasoftech.geoattendance.features.signup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.db.DatabaseReferenceManager;
import com.tadnyasoftech.geoattendance.db.StorageReferenceManager;
import com.tadnyasoftech.geoattendance.features.employee_dashboard.EmployeeDashboardActivity;
import com.tadnyasoftech.geoattendance.models.User;
import com.tadnyasoftech.geoattendance.utils.GEDialogUtility;
import com.tadnyasoftech.geoattendance.utils.GEUtility;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.rohanpeshkar.helper.HelperActivity;


/**
 * Created by dell on 18/12/17.
 */

public class SignUpActivity extends HelperActivity {

    @BindView(R.id.edt_full_name)
    EditText edtFullName;

    @BindView(R.id.il_full_name)
    TextInputLayout ilFullName;

    @BindView(R.id.rgp_gender)
    RadioGroup rgpGender;

    @BindView(R.id.edt_company_name)
    EditText edtCompanyName;

    @BindView(R.id.il_company_name)
    TextInputLayout ilCompanyName;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.il_email)
    TextInputLayout ilEmail;

    @BindView(R.id.edt_passsword)
    EditText edtPassword;

    @BindView(R.id.il_password)
    TextInputLayout ilPassword;

    @BindView(R.id.edt_repeat_password)
    EditText edtRepeatPassword;

    @BindView(R.id.il_repeat_password)
    TextInputLayout ilRepeatPassword;

    @BindView(R.id.civ_employee_attendance_image)
    CircleImageView civProfileImage;

    private User mUser;

    private Uri employeeImageUri = null;

    private FirebaseAuth mFirebaseAuth;

    private boolean isIndividual = false;

    @Override
    protected void create() {
        setTitle("New Registration");
        enableHome();
        mUser = new User();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.civ_employee_attendance_image)
    void onClickEmployeeImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                employeeImageUri = result.getUri();
                civProfileImage.setImageURI(employeeImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                if (error != null) {
                    showToast(error.getMessage());
                } else {
                    showToast("Image selection cancelled");
                }
            }
        }
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String fullName = edtFullName.getText().toString().trim();
        if ((TextUtils.isEmpty(fullName)) && (TextUtils.isGraphic(fullName))) {
            ilFullName.setError("Enter valid Name");
            return;
        } else {
            ilFullName.setErrorEnabled(false);
        }


        String gender = GEUtility.getCheckedRadioText(rgpGender);
        if (rgpGender.getCheckedRadioButtonId() == -1) {
            showToast("Select Gender");
            return;
        }

        //gender = GEUtility.getCheckedRadioText(rgpGender);


        String companyName = edtCompanyName.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            ilCompanyName.setError("Enter Company Name");
            return;
        } else {
            ilCompanyName.setErrorEnabled(false);
        }

        String email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ilEmail.setError("Enter valid Email Id");
            return;
        } else {
            ilEmail.setErrorEnabled(false);
        }

        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ilPassword.setError("Enter Password");
            return;
        } else {
            ilPassword.setErrorEnabled(false);
        }

        String repeatPassword = edtRepeatPassword.getText().toString().trim();
        if (TextUtils.isEmpty(repeatPassword)) {
            ilRepeatPassword.setError("Enter Password Again");
            return;
        } else {
            ilRepeatPassword.setErrorEnabled(false);
        }

        if (!repeatPassword.equals(password)) {
            ilRepeatPassword.setError("Passwords do not match");
            edtPassword.requestFocus();
            return;
        } else {
            ilRepeatPassword.setErrorEnabled(false);
        }

        setPersonalDetails(fullName, gender, companyName, email);
        signUp(email, password);
    }


    public void setPersonalDetails(String fullName,
                                   String gender,
                                   String companyName,
                                   String email) {
        mUser.setFullName(fullName);
        mUser.setGender(gender);
        mUser.setCompanyName(companyName);
        mUser.setEmail(email);
    }


    public void signUp(String email, String password) {
        showProgressDialog("Creating Account...", false);

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(createAccountTask -> {
                    if (createAccountTask.isSuccessful()) {
                        String uid = createAccountTask.getResult().getUser().getUid();
                        mUser.setId(uid);
                        if (employeeImageUri == null) {
                            writeNewUser(uid);
                        } else {
                            writeNewImage(uid);
                        }
                    } else {
                        dismissProgressDialog();
                        String errorMsg = createAccountTask.getException().getMessage();
                        GEDialogUtility.getErrorDialog(this, errorMsg).show();
                    }
                });
    }

    public void writeNewUser(String uid) {
        DatabaseReferenceManager.getInstance().getUsersReference().child(uid)
                .setValue(mUser)
                .addOnCompleteListener(dataUpdateTask -> {
                    dismissProgressDialog();
                    if (dataUpdateTask.isSuccessful()) {
                        GEUtility.saveUid(SignUpActivity.this, uid);
                        launch(EmployeeDashboardActivity.class);
                        finish();
                    } else {
                        String errorMsg = dataUpdateTask.getException().getMessage();
                        GEDialogUtility.getErrorDialog(this, errorMsg).show();
                    }
                });
    }

    public void writeNewImage(String uid) {
        StorageReferenceManager.getInstance()
                .getUserProfileImageReference().child(uid)
                .putFile(employeeImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if (downloadUrl != null) {
                        mUser.setImageUrl(downloadUrl.toString());
                    }
                    writeNewUser(uid);
                });
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


