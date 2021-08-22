
package com.jadblack;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;

import androidx.core.os.BuildCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressLint("NewApi")
public class SystemFunctions {
	//TAG
	public static final String tag = "SystemFunctions";
	//PREFERENCE ID
	public static final String preferenceID = "ANDROID_ID";
	
	public static int getDeviceBatteryLevel(Context context){
		//BATTERY LEVEL
		int deviceBatteryLevel = 0;
		//GET BATTERY LEVEL
		Intent batteryIntent = context.registerReceiver(
			null,
			new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
		);
		deviceBatteryLevel = batteryIntent != null ? batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) : 0;
		return deviceBatteryLevel;
	}
	
	//AUTO-GENERATE DEVICE ID FOR DEVICE
	public static String getDeviceId(Context context){
		//UNIQUE ID
		String uniqueID = null;

		//return "352005091095623";
		//return "352005091095623";
		//return "354169083174461";
		//return "AND593995234745";

		//return "AND593999795794";
		return "DEMO";

		/*
		// Get Inalambrik Device Id from Shared preferences.
		SharedPreferences sp = context.getSharedPreferences(preferenceID, Context.MODE_PRIVATE);
		uniqueID = sp.getString(preferenceID, null);
		if( uniqueID != null && !uniqueID.trim().isEmpty()){
			return uniqueID;
		}

		// For Android P (9) and below: If there is no DeviceId saved in sharedPreference, then get the IMEI and save it in the sharedPreferences.
		if(!SystemFunctions.isAtLeastAndroidQ()) {
			uniqueID = generateUniqueID(context);
			saveUniqueID(context, uniqueID);
		}
		
		//RETURN ID
		return uniqueID;
		 */

	}

	public static void saveUniqueID(Context context, String uniqueID) {
		SharedPreferences sp = context.getSharedPreferences(preferenceID, Context.MODE_PRIVATE);
		Log.i(tag, "Saving new Device Id: " + uniqueID);
		Editor editor = sp.edit();
		editor.putString(preferenceID, uniqueID);
		editor.commit();
	}
	
	private static String generateUniqueID(Context context) {
		String uniqueID = "";

		// For Android Q (10) or above we cant get a non-reseatable identifier. So we return empty.
		if(SystemFunctions.isAtLeastAndroidQ())
			return "";

		//TRY IMEI
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			uniqueID = telephonyManager.getImei();
		else
			uniqueID = telephonyManager.getDeviceId();
		if (uniqueID != null && uniqueID.length() > 0 && NumericFunctions.toLong(uniqueID) > 0){
			Log.i(tag, "IMEI Found");
			return uniqueID;
		}
		
		//TRY TO ACCESS WIFI AND GAIN MAC ADDRESS
		try{
			boolean wifiTurnedOnByApp = false;
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			if( !wifi.isWifiEnabled() ){
				wifi.setWifiEnabled(true);
				wifiTurnedOnByApp = true;
			}
			WifiInfo wifiInfo = wifi.getConnectionInfo();
			uniqueID = wifiInfo.getMacAddress();
			
			if(wifiTurnedOnByApp){
				wifi.setWifiEnabled(false);
			}
		}catch(Exception e){
			Log.e(tag, "Error with WIFI: " + e);
		}
		
		if (uniqueID != null && uniqueID.length() > 0){
			uniqueID = uniqueID.replaceAll(":", "");
			Log.i(tag, "WIFI MAC Found");
			return uniqueID;
		}
		
		uniqueID = UUID.randomUUID().toString();
		uniqueID = uniqueID.replaceAll("-", "");
		try{ uniqueID = uniqueID.substring(0, 15); }catch(Exception e){}
		uniqueID = uniqueID.toUpperCase();
		Log.i(tag, "Generating new UID");
		return uniqueID;		
	}
	
	public static String getImagesDirectory(Context context){
		File imagesDir = new File(context.getCacheDir(), "images");
		if (!imagesDir.exists())
			imagesDir.mkdir();

		return imagesDir.getAbsolutePath() + File.separator;
		//return context.getFilesDir().getAbsolutePath() + File.separator;
		//return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator;
	}
	
	public static String getSDRootDirectory(Context context){
		return context.getFilesDir().getAbsolutePath() + File.separator;
		//return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		//return context.getExternalFilesDir(null).getAbsolutePath() + "/";
	}

	public static String getDatabaseDirectory(Context context){
		return context.getFilesDir().getAbsolutePath() + File.separator;
		//return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		//return context.getExternalFilesDir(null).getAbsolutePath() + "/";
	}
	
	public static String getDownloadsDirectory(Context context){
		File downloadsDir = new File(context.getCacheDir(), "downloads");
		if (!downloadsDir.exists())
			downloadsDir.mkdir();

		return downloadsDir.getAbsolutePath() + File.separator;
		//return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		//return context.getExternalFilesDir().getAbsolutePath() + File.separator;
		//return context.getExternalFilesDir(null).getAbsolutePath() + "/";
	}
	
	public static HashMap<String,Object> getDeviceInformationHashMap(Context context){
		HashMap<String,Object> extras = new HashMap<>();
		extras.put("device_id", getDeviceId(context));
		extras.put("battery_level", StringFunctions.toString(getDeviceBatteryLevel(context)));

		return extras;
	}

	public static boolean isAtLeastAndroidQ(){
		return Build.VERSION.SDK_INT >= 29;
	}
}
