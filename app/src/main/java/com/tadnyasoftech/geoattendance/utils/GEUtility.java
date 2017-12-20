package com.tadnyasoftech.geoattendance.utils;

import android.content.Context;

import com.tadnyasoftech.geoattendance.models.User;

import me.rohanpeshkar.helper.HelperPreferences;

/**
 * Created by dell on 19/12/17.
 */

public class GEUtility {


    public static void saveFullName(Context context, String fullName) {
        HelperPreferences.get(context)
                .saveString(GEConstants.Keys.USER_FULL_NAME, fullName);
    }


    public static String getFullName(Context context) {
        return HelperPreferences.get(context)
                .getString(GEConstants.Keys.USER_FULL_NAME);
    }



    public static void saveCompanyName(Context context,String companyName) {
        HelperPreferences.get(context)
                .saveString(GEConstants.Keys.USER_COMPANY, companyName);
    }

    public static String getCompanyName(Context context)
    {
        return HelperPreferences.get(context)
                .getString(GEConstants.Keys.USER_COMPANY);


    }

    public static void saveEmail(Context context,String email){
        HelperPreferences.get(context)
                .saveString(GEConstants.Keys.USER_EMAIL,email);
}

public static String getEmail(Context context){
        return HelperPreferences.get(context)
                .getString(GEConstants.Keys.USER_EMAIL);
}

    public static void saveUserDetails(Context context, User user) {

        saveFullName(context, user.getFullName());
        saveCompanyName(context,user.getCompanyName());
        saveEmail(context, user.getEmail());

    }
}
