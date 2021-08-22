package com.jadblack;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by DESARROLLO on 1/11/2017.
 */

public class PermissionCheckFunctions {
    final static int REQUEST_WRITE_EXTERNAL_STORAGE_CODE = 9501;


    private static boolean checkPermission(Activity activity, String permission) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    // ---------------------------------------------------------------------------------------------
    //  CHECK WRITE EXTERNAL STORAGE PERMISSION
    // ---------------------------------------------------------------------------------------------
    public static boolean checkWriteExternalStoragePermission(Activity activity){
        if (checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            return true;
        else{
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_EXTERNAL_STORAGE_CODE);
            return false;
        }
    }
}
