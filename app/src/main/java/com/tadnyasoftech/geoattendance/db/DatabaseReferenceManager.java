package com.tadnyasoftech.geoattendance.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tadnyasoftech.geoattendance.BuildConfig;

/**
 * Created by dell on 19/12/17.
 */

public class DatabaseReferenceManager {

    private static DatabaseReferenceManager instance;


    private  final String DATABASE_ROOT;

    private final class ReferenceKeys{

        static final String USERS = "users";
        static final String ATTENDANCE="Attendance";




    }

    public static DatabaseReferenceManager getInstance() {
        if (instance == null) {
            instance = new DatabaseReferenceManager();
        }
        return instance;
    }

    private DatabaseReferenceManager() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DATABASE_ROOT = BuildConfig.DATABASE_ROOT;
    }

    private DatabaseReference getRoot() {
        return FirebaseDatabase.getInstance().getReference().child(DATABASE_ROOT);
    }

    public DatabaseReference getUsersReference() {
        return getRoot().child(ReferenceKeys.USERS);
    }

    public DatabaseReference getAttendanceReference() {
        return getRoot().child(ReferenceKeys.ATTENDANCE);
    }

}
