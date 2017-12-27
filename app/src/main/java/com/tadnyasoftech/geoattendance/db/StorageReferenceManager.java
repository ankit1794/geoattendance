package com.tadnyasoftech.geoattendance.db;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tadnyasoftech.geoattendance.BuildConfig;

/**
 * Created by dell on 19/12/17.
 */

public class StorageReferenceManager {

    private static StorageReferenceManager instance;


    private String STORAGE_ROOT = null;

    private final class ReferenceKeys {
        static final String USER_IMAGE = "user_image";

    }

    public static StorageReferenceManager getInstance() {
        if (instance == null) {
            instance = new StorageReferenceManager();
        }
        return instance;
    }

    private StorageReferenceManager() {
        STORAGE_ROOT = BuildConfig.DATABASE_ROOT;
    }

    private StorageReference getRoot() {
        return FirebaseStorage.getInstance().getReference().child(STORAGE_ROOT);
    }

    public StorageReference getUserImageReference(String entityId) {
        return getRoot().child(entityId).child(ReferenceKeys.USER_IMAGE);
    }


}