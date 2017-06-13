package com.example.astroboy.family_master_version01.Util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by AstroBoy on 2016/9/7.
 */
public class verifyPermissions {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.GET_TASKS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}
