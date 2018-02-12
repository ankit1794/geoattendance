package com.tadnyasoftech.geoattendance.features.employee_dashboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tadnyasoftech.geoattendance.R;
import com.tadnyasoftech.geoattendance.customviews.DividerItemDecoration;
import com.tadnyasoftech.geoattendance.db.DatabaseReferenceManager;
import com.tadnyasoftech.geoattendance.features.profile.EmployeeProfileActivity;
import com.tadnyasoftech.geoattendance.models.Attendance;
import com.tadnyasoftech.geoattendance.utils.GEDialogUtility;
import com.tadnyasoftech.geoattendance.utils.GEUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.rohanpeshkar.helper.HelperActivity;

/**
 * Created by dell on 4/1/18.
 */

public class EmployeeDashboardActivity extends HelperActivity implements
        AttendancListAdapter.OnCheckOutListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.nav_drawer)
    NavigationView mNavigationView;

    @BindView(R.id.nav_drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.vip_dashboard_tabs)
    ViewPager vipTabs;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tbl_dashboard)
    TabLayout tblDashboard;

    @BindView(R.id.fab_add)
    FloatingActionButton fab;

    private GoogleApiClient gApiClient;

    private Location lastLocation = null;

    private Geocoder mGeocoder;

    private HashMap<Integer, Intent> mNavItemMap;

    private AttendancListAdapter mAttendancListAdapter;


    ArrayList<Attendance> attendanceList;

    private Attendance mAttendance;


    @Override
    protected void create() {
        setUpToolbar();
        setTitle("Attendance Dashboard");
        enableHome();
        gApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        init();
        setUpNavDrawer();
        setUpTabs();
        initRecyclerView();
        load();


    }


    public void load() {
        DatabaseReferenceManager.getInstance().
                getAttendanceReference(GEUtility.getUid(this))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        attendanceList.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Attendance attendance = dataSnapshot1.getValue(Attendance.class);
                            attendanceList.add(attendance);
                            mAttendancListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("hello", "Failed to read value",
                                databaseError.toException());

                    }
                });


    }


    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
    }


    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        GEDialogUtility.getExitDialog(this).show();
    }

    private void init() {
        mNavItemMap = new HashMap<>();
        mNavItemMap.put(R.id.nav_item_employee, getNewIntent(MarkAttendanceActivity.class));
        mNavItemMap.put(R.id.nav_item_profile, getNewIntent(EmployeeProfileActivity.class));
    }

    private void initRecyclerView() {
        // create a empty list
        attendanceList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        //creating a object of adapter class
        mAttendancListAdapter = new AttendancListAdapter(this, attendanceList,
                this);
        recyclerView.setAdapter(mAttendancListAdapter);
    }


    private void setUpTabs() {


        tblDashboard.setupWithViewPager(vipTabs, true);

    }

    private void setUpNavDrawer() {

        TextView txtEntityName = (TextView) mNavigationView.getHeaderView(0)
                .findViewById(R.id.txt_entity_name);
        txtEntityName.setText(GEUtility.getEntityName(this));

        mToolbar.setNavigationIcon(R.drawable.ic_menu_white);
        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(item -> {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            if (item.getItemId() == R.id.nav_item_logout) {
                GEDialogUtility.getLogoutDialog(this).show();
            } else {
                startActivity(getIntentForNav(item.getItemId()));
            }
            return true;
        });
    }

    private Intent getIntentForNav(Integer index) {
        return mNavItemMap.get(index);
    }


    @OnClick(R.id.fab_add)
    void onClick() {
        launch(MarkAttendanceActivity.class);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_employee_daashboard;
    }

    @Override
    protected boolean isToolbarPresent() {
        return true;
    }


    @Override
    public void onClickCheckout(int position) {
        Attendance attendance = mAttendancListAdapter.getItem(position);
        if (GEUtility.isMockLocation(lastLocation)) {
            //showToast("Mock Location,Can't CheckOut");
            GEDialogUtility.getErrorDialog(this, "Mock location," +
                    "Please Enter valid Location else you can't CheckOut").show();
        } else {
            if (GEUtility.isNear(lastLocation, attendance.getLatitude(),
                    attendance.getLongitude())) {

                Map<String, Object> checkOut = new HashMap<>();
                checkOut.put("checkOutTime", System.currentTimeMillis());
                DatabaseReferenceManager.getInstance().
                        getAttendanceReference(GEUtility.getUid(this))
                        .child(attendance.getId()).updateChildren(checkOut);

                showToast("Can checkout");
            } else {
                showToast("Can't checkout far away from location");
            }
        }
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
            new EmployeeDashboardActivity.GeoCoderTask().execute(lastLocation);
        }

    }


    class GeoCoderTask extends AsyncTask<Location, Void, List<Address>> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EmployeeDashboardActivity.this);
            progressDialog.setMessage("Getting address...");
            //progressDialog.show();
            progressDialog.setCancelable(false);
            mGeocoder = new Geocoder(EmployeeDashboardActivity.this,
                    Locale.getDefault());
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

                //txtLocation.setText(address);
            } else {
                //txtLocation.setText("Not got addresses");
            }
        }

    }


}
