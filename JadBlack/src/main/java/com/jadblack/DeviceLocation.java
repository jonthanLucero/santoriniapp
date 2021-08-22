
package com.jadblack;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.util.Date;

@SuppressWarnings("unused")
public class DeviceLocation{
	//FOR DEBUGGING
	private static final String TAG = "DeviceLocation";
	
	//LOCK OBJECT
	private final Object lock = new Object();
	
	//LOCATION MANAGER
	public LocationManager locationManager;
	//GPS STATUS LISTENER AND GPS STATUS
	public GpsStatus gpsStatus;
	public GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event){
			switch(event){
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					gpsStatus = locationManager.getGpsStatus(gpsStatus);
					satellites = 0;
					for( GpsSatellite satellite : gpsStatus.getSatellites() ){
						if( satellite.usedInFix() ) satellites += 1;
					}
					//Log.d(TAG, "Satellites: " + satellites);
					if( satellites > mMinSatellites && locationFound ){
						locationFoundPrecise = true;
						interruptTimeout();											
					}
					break;
			}
		}
	};	
	//LOCATION LISTENER
	public LocationListener locationListener = new LocationListener(){
		public void onLocationChanged(Location location){
			latitude = location.getLatitude();
			longitude = location.getLongitude();				
			accuracy = location.getAccuracy();
			altitude = location.getAltitude();
			speed = location.getSpeed() * 60 * 60 / 1000;
			bearing = location.getBearing();
			time = location.getTime();
			datetime = new Date(time);
			
			Log.v(TAG, "Location:" + latitude + "," + longitude + "(Acc:" + accuracy + ",Sat:" + satellites + ")");
			if(accuracy <= mMaxAllowedAccuracy){
				locationFound = true;
			}else{
				locationFound = false;
			}
			interruptTimeout();
		}
		public void onProviderDisabled(String arg0){
			interruptTimeout();
		}
		public void onProviderEnabled(String arg0) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}				
	};
	
	//LOCATION VALUE FIELDS
	public double latitude = 0;
	public double longitude = 0;
	public double accuracy = 0;
	public double altitude = 0;
	public float speed = 0;
	public float bearing = 0;
	public short satellites = 0;
	public long time = 0;
	public Date datetime = new Date();
	public String locationStatus = "I";
	
	//LOCATION CRITERIA
	public double mMaxAllowedAccuracy = 60;
	public void setMaxAllowedAccuracy(double maxAllowedAccuracy){
		if(maxAllowedAccuracy <= 0)
			return;

		if(mRequestedProvider.equalsIgnoreCase(LocationManager.GPS_PROVIDER))
			mMaxAllowedAccuracy = maxAllowedAccuracy;
		else
			mMaxAllowedAccuracy = 1000;

		Log.i(TAG, "Max Allowed Accuracy: " + mMaxAllowedAccuracy);
	}
	public short mMinSatellites = 3;
	public void setMinSatellites(short minSatellites){
		if(mRequestedProvider.equalsIgnoreCase(LocationManager.GPS_PROVIDER))
			mMinSatellites = minSatellites;
		else
			mMinSatellites = 0;
	}
	
	//LOCATION FOUND FLAG
	public boolean locationFound = false;
	public boolean locationFoundPrecise = false;
	
	//THREAD FOR LOCATION
	private Thread locationThread = null;
	
	//CONFIGURATION
	public String mRequestedProvider = LocationManager.NETWORK_PROVIDER;
	
	//DEFAULT CONSTRUCTOR
	public DeviceLocation(Context context){
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		setMaxAllowedAccuracy(1000);
		setMinSatellites((short)3);
	}
	
	public void setProvider(String provider){
		if( provider.equalsIgnoreCase("GPS") ){
			mRequestedProvider = LocationManager.GPS_PROVIDER;
			provider = "GPS";
		}else{
			mRequestedProvider = LocationManager.NETWORK_PROVIDER;
			provider = "NETWORK";
		}
	}
	
	//START
	public void start(int locationTimeoutSeconds){
		//VERIFY PROVIDER STATUS
		if( !isProviderAvailable() ){
			return;
		}
		
		//DEPLOY LOCATION THREAD
		Log.d(TAG, "Deploying Location updates for provider " + mRequestedProvider);
		deployLocationThread();
		
		//START TIME OUT
		int timeout = locationTimeoutSeconds;
		//if(mRequestedProvider.equalsIgnoreCase(LocationManager.NETWORK_PROVIDER))
			//timeout = 20;
			
		Log.d(TAG, "Timing out in " + timeout + " seconds");
		startTimeout(timeout);				
	}
	
	public boolean isProviderAvailable(){
		boolean providerAvailable = false;
		try{			
			providerAvailable = locationManager.isProviderEnabled(mRequestedProvider);
			Log.i(TAG, "Location Provider enabled?: " + providerAvailable);
		}catch(Exception e){			
		}
		return providerAvailable;
	}
	
	public synchronized void interruptTimeout(){
		Log.d(TAG, "Requesting Timeout exit");
		if( locationReady() ){
			Log.d(TAG, "Location is Ready");
			notifyAll();
		}
	}
	
	private synchronized void startTimeout(int locationTimeoutSeconds){
		try {
			Log.d(TAG, "Waiting for Location");
			wait(locationTimeoutSeconds * 1000);
			Log.d(TAG, "Stopped waiting");
			
			Log.d(TAG, "Requesting updates stop");
			stopLocationUpdates();
			Log.d(TAG, "Updates stopped");
		}catch(InterruptedException e){
			Log.d(TAG, "Interrupted Exception waiting: " + e);
		}			
	}
	
	public void deployLocationThread(){
		locationThread = new Thread(
			new Runnable(){
				public void run(){
					Looper.prepare();					
					//LOCATION LISTENER					
					locationManager.requestLocationUpdates(
							mRequestedProvider,
						0,
						0,
						locationListener
					);
					
					//GPS STATUS LISTENER FOR GPS_PROVIDER ONLY
					if( mRequestedProvider.equals(LocationManager.GPS_PROVIDER) ){
						locationManager.addGpsStatusListener(gpsStatusListener);
					}
					Looper.loop();
				}
			}
		);	
		locationThread.start();	
	}
	
	public synchronized void stopLocationUpdates(){
		Log.d(TAG, "Requesting Stop of Location Updates");
		try{
			//REMOVE LOCATION LISTENER
			locationManager.removeUpdates(locationListener);
			//REMOVE GPS STATUS LISTENER FOR GPS_PROVIDER ONLY
			if( mRequestedProvider.equals(LocationManager.GPS_PROVIDER) ){
				locationManager.removeGpsStatusListener(gpsStatusListener);
			}
		}catch(Exception e){
			Log.e(TAG, "Exception while stopping Location updates: " + e);
		}
		Log.d(TAG, "Location Updates Stopped");
	}	
	
	public synchronized boolean locationReady(){
		//FOR GPS_PROVIDER
		if(mRequestedProvider.equalsIgnoreCase(LocationManager.GPS_PROVIDER )){
			//IF LOCATION FOUND
			if( locationFound && locationFoundPrecise){
				return true;
			}
			//IF PROVIDER ENABLED
			if(isProviderAvailable() == false){
				return true;
			}
			return false;
		}
		
		//FOR NETWORK PROVIDER
		if(mRequestedProvider.equalsIgnoreCase(LocationManager.NETWORK_PROVIDER )){
			//IF LOCATION FOUND
			if( locationFound ){
				locationFoundPrecise = true;
				return true;
			}
			//IF PROVIDER ENABLED
			if(isProviderAvailable() == false){
				return true;
			}
			return false;
		}	
		return true;			
	}
	
	public static double distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude){
		float[] results = new float[3];
		//Log.d(TAG, "Start: " + startLatitude + " | " + startLongitude );
		//Log.d(TAG, "End: " + endLatitude + " | " + endLongitude );
		Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);		
		if( results != null && results.length > 0){
			//Log.d(TAG, "Distance: " + results[0]);
			return results[0];
		}
		else{
			return 0;
		}
	}
}
