package com.tadnyasoftech.geoattendance.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tadnyasoftech.geoattendance.R;

/**
 * Created by dell on 27/12/17.
 */

public class GEDialogUtility {


    public static MaterialDialog getSuccessDialog(Context context, String successMsg) {
        return new MaterialDialog.Builder(context)
                .title("Success")
                .titleColorRes(R.color.colorGreen)
                .content(successMsg)
                .positiveText("Okay")
                .onPositive((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getErrorDialog(Context context, String errorMsg) {
        return new MaterialDialog.Builder(context)
                .title(context.getString(R.string.dialog_title_error))
                .titleColorRes(R.color.colorRed)
                .content(errorMsg)
                .positiveText(context.getString(R.string.dialog_action_okay))
                .onPositive((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getResetPasswordDialog(Context context, MaterialDialog.InputCallback inputCallback) {
        return new MaterialDialog.Builder(context)
                .title(R.string.dialog_title_forgot)
                .content(R.string.dialog_content_forgot)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .positiveText(R.string.dialog_action_send)
                .input(R.string.dialog_hint_forgot, 0, inputCallback).build();

    }

    public static MaterialDialog getExitDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .title(context.getString(R.string.dialog_title_confirmation))
                .titleColorRes(R.color.colorAccent)
                .content(context.getString(R.string.dialog_msg_exit))
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onNegative((dialog, which) -> dialog.dismiss())
                .onPositive((dialog, which) -> ((Activity) context).finishAffinity()).build();
    }


    public static MaterialDialog getLogoutDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .title(context.getString(R.string.dialog_title_information))
                .content(context.getString(R.string.dialog_message_logout))
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onPositive((dialog, which) -> GEUtility.logout(context))
                .onNegative((dialog, which) -> dialog.dismiss()).build();

    }
}
