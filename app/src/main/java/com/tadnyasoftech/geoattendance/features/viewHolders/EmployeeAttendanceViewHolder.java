package com.tadnyasoftech.geoattendance.features.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tadnyasoftech.geoattendance.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell on 11/1/18.
 */

public class EmployeeAttendanceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.dashboard_txt_location)
    public TextView txtLocation;

    @BindView(R.id.txt_check_in_time)
    public TextView txtCheckInTime;

    @BindView(R.id.txt_check_out_Time)
    public TextView txtCheckOutTime;

    /*@BindView(R.id.txt_CheckOut)
    public TextView txtCheckOutTime;*/

    @BindView(R.id.civ_employee_attendance_image)
    public CircleImageView civAttendanceImage;

    @BindView(R.id.btn_checkOut_time)
    public Button btnCheckOut;

    public EmployeeAttendanceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
