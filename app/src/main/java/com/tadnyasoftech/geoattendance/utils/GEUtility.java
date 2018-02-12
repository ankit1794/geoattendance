package com.tadnyasoftech.geoattendance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tadnyasoftech.geoattendance.features.authentication.LoginActivity;
import com.tadnyasoftech.geoattendance.features.employee_dashboard.EmployeeDashboardActivity;
import com.tadnyasoftech.geoattendance.models.Attendance;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.rohanpeshkar.helper.HelperPreferences;

/**
 * Created by dell on 26/12/17.
 */

public class GEUtility {

    public  static final String ENTITY_NAME="EMPLOYEE";

    public static String getCheckedRadioText(RadioGroup radioButtonGroup) {
        int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
        View radioButtonView = radioButtonGroup.findViewById(radioButtonID);
        int idx = radioButtonGroup.indexOfChild(radioButtonView);
        RadioButton radioButton = (RadioButton) radioButtonGroup.getChildAt(idx);
        return radioButton.getText().toString();
    }


    public static String getFormattedDate(long milliSeconds) {
        return new SimpleDateFormat("dd MMM yyyy hh:mm a").format(new Date(milliSeconds));
    }

    public static String getEntityName(Context context) {
        return HelperPreferences.get(context)
                .getString(ENTITY_NAME);
    }

    public static void saveUid(Context context,String uid){
        HelperPreferences.get(context).saveString(GEConstants.KEY_UID,uid);
    }

    public  static String getUid(Context context){
        return  HelperPreferences.get(context).getString(GEConstants.KEY_UID);
    }


    public static void logout(Context context) {
        HelperPreferences.get(context).clear();
        FirebaseAuth.getInstance().signOut();
        try {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity) context).finishAffinity();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static boolean isNear(Location currLocation, double lat, double lng){
        Location location = new Location("");
        location.setLongitude(lng);
        location.setLatitude(lat);
        double distance = (double) location.distanceTo(currLocation);//in meters
        double limit = 2000.00;
        return distance <= limit;
    }

    public static boolean isMockLocation(Location location) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && location != null &&
                location.isFromMockProvider();
    }
}

