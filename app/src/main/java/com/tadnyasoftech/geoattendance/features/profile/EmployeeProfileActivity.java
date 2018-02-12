package com.tadnyasoftech.geoattendance.features.profile;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.glide.GlideApp;
import com.tadnyasoftech.geoattendance.models.User;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import me.rohanpeshkar.helper.HelperActivity;

/**
 * Created by dell on 10/1/18.
 */

public class EmployeeProfileActivity extends HelperActivity {

    @BindView(R.id.civ_employee_attendance_image)
    CircleImageView civEmployeeProfileImage;

    @BindView(R.id.txt_employee_name)
    TextView txtEmployeeName;

    @BindView(R.id.txt_employee_company_name)
    TextView txtEmployeeCompanyName;

    @BindView(R.id.txt_employee_email_id)
    TextView txtEmployeeeEmailId;

    private User mUser;

    private Context mContext;


    @Override
    protected void create() {
        setTitle("Profile");
        enableHome();

    }

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_employee_profile;
    }

    @Override
    protected boolean isToolbarPresent() {
        return true;
    }


}
