package com.jadblack;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
/**
 * Created by Peter on 27/11/2018.
 */

public class InalambrikDBUtils {

    private static String[] countProjection = new String[]{"count(*)"};

    public static int getTableRowCount(Context context, Uri uri, String selection, String[] selectionArgs){

        if(context == null){
            Log.d("InalambrikDBUtils","Context was null in getTableRowCount...");
            return 0;
        }

        int rowCount = 0;

        Cursor cursor = context.getContentResolver().query(uri,
                countProjection,
                selection,
                selectionArgs,
                null);
        if(cursor != null){
            try {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    rowCount = cursor.getInt(0);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(!cursor.isClosed())cursor.close();
            }
        }

        return rowCount;
    }

    public static int updateRows(Context context, Uri uri, ContentValues cv, String selection, String[] selectionArgs) {
        if(context == null){
            Log.d("InalambrikDBUtils","Context was null in updateRows...");
            return 0;
        }


        int updatedRows = 0;

        try {
            updatedRows = context.getContentResolver().update(uri,
                    cv,
                    selection,
                    selectionArgs);
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("InalambrikDBUtils","Rows updated: " + updatedRows);

        return updatedRows;
    }

    /*
    // Return a Cursor that MUST be closed in the process who is calling it.
     */
    public static Cursor getCursorFromNativeQuery(Context context, Uri uri, String[] projection, String selection,String[] selectionArgs){

        if(context == null){
            Log.d("InalambrikDBUtils","Context was null in getTableRowCount...");
            return null;
        }

        try {
            return context.getContentResolver().query(uri,
                    projection,
                    selection,
                    selectionArgs,
                    null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*
    // Return a Cursor that MUST be closed in the process who is calling it.
     */
    public static Cursor getCursorFromNativeQueryWithOrderBy(Context context, Uri uri, String[] projection, String selection,String[] selectionArgs, String orderBy){
        if(context == null){
            Log.d("InalambrikDBUtils","Context was null in getTableRowCount...");
            return null;
        }

        try {
            return context.getContentResolver().query(uri,
                    projection,
                    selection,
                    selectionArgs,
                    orderBy);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // NOTE: There is a TWIN method in the Native Code (class InalambrikSession)
    public static void setLoggedInFromJadblack(Context context, boolean isLoggedIn){
        if(context == null) return;
        Log.d("InalambrikSession","Session is being set to: " + isLoggedIn);

        try {
            SharedPreferences.Editor editor = getPreferences(context).edit();
            editor.putBoolean("LOGGED_IN", isLoggedIn);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
