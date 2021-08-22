package com.example.santoriniapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import static android.content.Context.MODE_PRIVATE;

public class PermissionsUtils
{
    public static boolean permissionGranted(Context context, String permission)
    {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED ;

    }

    public static boolean permissionWasAlreadyAskedOneTime(Context context, String permission)
    {
        return context.getSharedPreferences("first_check_"+permission,MODE_PRIVATE).getBoolean("first",false) ;
    }

    public static void setPermissionWasAlreadyAskedOneTime(Context context, String permission, Boolean isPermissionWasAlreadyAskedOneTime)
    {
        context.getSharedPreferences("first_check_"+permission,MODE_PRIVATE).edit().putBoolean("first",isPermissionWasAlreadyAskedOneTime).apply();

    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission)
    {
        return ActivityCompat.shouldShowRequestPermissionRationale( activity,permission) ;

    }
    public static void openSettings(Activity activity,int requestCode){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivityForResult(intent, requestCode);
    }
    public static void TedPermissionCheck(Context context, String permission, PermissionListener mPermissionListener)
    {  TedPermission.with(context)
            .setPermissionListener(mPermissionListener)
            .setPermissions(
                    permission
            )
            .check();
    }

}
