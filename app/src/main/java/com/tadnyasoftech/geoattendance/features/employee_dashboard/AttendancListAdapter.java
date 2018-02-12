package com.tadnyasoftech.geoattendance.features.employee_dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.features.viewHolders.EmployeeAttendanceViewHolder;
import com.tadnyasoftech.geoattendance.glide.GlideApp;
import com.tadnyasoftech.geoattendance.models.Attendance;
import com.tadnyasoftech.geoattendance.utils.GEUtility;

import java.util.ArrayList;


/**
 * Created by rohan on 12/1/17.
 */

public class AttendancListAdapter extends RecyclerView.Adapter<EmployeeAttendanceViewHolder> {

    private Context mContext;

    private ArrayList<Attendance> attendanceList;

    private OnCheckOutListener onCheckOutListener;


    Attendance attendance;
    private long timeInMillis;


    public AttendancListAdapter(Context mContext, ArrayList<Attendance> attendanceList,
                                OnCheckOutListener onCheckOutListener) {
        this.mContext = mContext;
        this.attendanceList = attendanceList;
        this.onCheckOutListener = onCheckOutListener;
    }

    @Override
    public EmployeeAttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EmployeeAttendanceViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.activity_marked_attendance, parent, false));
    }


    @Override
    public void onBindViewHolder(EmployeeAttendanceViewHolder holder, int position) {
        Attendance attendance = getItem(position);
        holder.txtLocation.setText(getItem(position).getAddress());
        holder.txtCheckInTime.setText(GEUtility.
                getFormattedDate(getItem(position).getCheckInTime()));
        if (attendance.getCheckOutTime() == 0) {
            //show checkoutbutton hide textview
            holder.btnCheckOut.setVisibility(View.VISIBLE);
            holder.txtCheckOutTime.setVisibility(View.INVISIBLE);
            holder.btnCheckOut.setOnClickListener(view ->
                    onCheckOutListener.onClickCheckout(position));
        } else {

            holder.txtCheckOutTime.setText(GEUtility.
                    getFormattedDate(getItem(position).getCheckOutTime()));
            holder.btnCheckOut.setVisibility(View.INVISIBLE);
            holder.txtCheckOutTime.setVisibility(View.VISIBLE);
        }
        //holder.txtCheckOutTime.setText(String.valueOf(getItem(position).getCheckOutTime()));

        GlideApp.with(mContext)
                .load(attendance.getImageUrl())
                .placeholder(R.drawable.ic_user)
                .into(holder.civAttendanceImage);

    }

    public Attendance getItem(int position) {
        return attendanceList.get(position);
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public interface OnCheckOutListener {
        void onClickCheckout(int position);
    }
}
