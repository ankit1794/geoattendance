package com.tadnyasoftech.geoattendance.features.employee_dashboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.db.DatabaseReferenceManager;
import com.tadnyasoftech.geoattendance.db.StorageReferenceManager;
import com.tadnyasoftech.geoattendance.models.Attendance;
import com.tadnyasoftech.geoattendance.utils.GEDialogUtility;
import com.tadnyasoftech.geoattendance.utils.GEUtility;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.rohanpeshkar.helper.HelperActivity;

/**
 * Created by dell on 4/1/18.
 */

public class MarkAttendanceActivity extends HelperActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.txt_time)
    TextView txtTime;

    @BindView(R.id.txt_location)
    TextView txtLocation;

    @BindView(R.id.civ_employee_profile_image_attendance)
    CircleImageView civProfileImage;

    private Attendance mAttendance;

    private FirebaseAuth mFirebaseAuth;

    //private Uri employeeImageAttendanceUri = null;

    Attendance attendance;


    private GoogleApiClient gApiClient;

    private Location lastLocation = null;

    private Geocoder mGeocoder;

    private Uri employeeAttendanceImageUri;

    private long timeInMillis;


    @Override
    protected void create() {
        setTitle("Mark Attendance");
        enableHome();
        gApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        Calendar calendar = Calendar.getInstance();
        timeInMillis = calendar.getTimeInMillis();
        txtTime.setText(GEUtility.getFormattedDate(timeInMillis));
        attendance = new Attendance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (gApiClient != null) {
            gApiClient.connect();
        }
    }


    @Override
    protected void onStop() {
        gApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w("ADVSEARCH", "Error on connection failed");

    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.w("ADVSEARCH", "Error on connection suspended");

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(gApiClient);
            new GeoCoderTask().execute(lastLocation);
        }

    }

    class GeoCoderTask extends AsyncTask<Location, Void, List<Address>> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MarkAttendanceActivity.this);
            progressDialog.setMessage("Getting address...");
            //progressDialog.show();
            progressDialog.setCancelable(false);
            mGeocoder = new Geocoder(MarkAttendanceActivity.this, Locale.getDefault());
        }

        @Override
        protected List<Address> doInBackground(Location... locations) {
            Location location = locations[0];
            try {
                return mGeocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", "Error while decoding " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);
            progressDialog.dismiss();
            if (progressDialog != null)
                progressDialog.dismiss();
            if (addresses != null) {
                String address = addresses.get(0).getAddressLine(0);
                txtLocation.setText(address);
            } else {
                txtLocation.setText("Not got addresses");
            }
        }
    }

    @OnClick(R.id.civ_employee_profile_image_attendance)
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
                employeeAttendanceImageUri = result.getUri();
                civProfileImage.setImageURI(employeeAttendanceImageUri);
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

    @OnClick(R.id.btn_mark_attendance)
    void onMarkAttendanceClick() {

        if (employeeAttendanceImageUri == null) {
            showToast("Capture image to mark attendance");
            return;
        }

        createNewAttendance();

    }


    public void createNewAttendance() {
        showProgressDialog("Marking Attendance...", false);
        attendance.setAddress(txtLocation.getText().toString());
        attendance.setCheckInTime(timeInMillis);
        attendance.setLatitude(lastLocation.getLatitude());
        attendance.setLongitude(lastLocation.getLongitude());

        DatabaseReference userAttendanceRef = DatabaseReferenceManager.getInstance()
                .getAttendanceReference(GEUtility.getUid(this));
        String id = userAttendanceRef.push().getKey();
        attendance.setId(id);
        writeNewAttendanceImage(id);
    }

    private void markAttendance(String id) {
        DatabaseReferenceManager.getInstance()
                .getAttendanceReference(GEUtility.getUid(this))
                .child(id).setValue(attendance)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismissProgressDialog();
                        showToast("Attendance marked");
                        finish();
                    } else {
                        dismissProgressDialog();
                        GEDialogUtility.getErrorDialog(MarkAttendanceActivity.this,
                                "Marking attendance failed try again").show();
                    }
                });

    }

    public void writeNewAttendanceImage(String id) {
        StorageReferenceManager.getInstance()
                .getUserAttendanceImageReference()
                .child(GEUtility.getUid(this))
                .child(id)
                .putFile(employeeAttendanceImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if (downloadUrl != null) {
                        attendance.setImageUrl(downloadUrl.toString());

                    }
                    markAttendance(id);
                });

    }


    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_mark_attendance;
    }

    @Override
    protected boolean isToolbarPresent() {
        return true;
    }

}
