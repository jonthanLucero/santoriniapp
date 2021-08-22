
package com.jadblack;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class DeviceNetworkInfo{
	public int cellId;
	public int lac;
	public int mcc;
	public int mnc;
	public String networkType;
	
	public DeviceNetworkInfo(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		cellId = 0;
		lac = 0;
		mcc = 0;
		mnc = 0;
		networkType = "";
		
		try{
			GsmCellLocation cellLocation = (GsmCellLocation)tm.getCellLocation();
	    
		    //CELL ID
		    cellId = cellLocation.getCid();
		    //LAC
		    lac = cellLocation.getLac();
		    
		    //NETWORK TYPE
		    switch(tm.getNetworkType()){
		    	case TelephonyManager.NETWORK_TYPE_EDGE:
		    		networkType = "EDGE";
		    		break;
		    	case TelephonyManager.NETWORK_TYPE_GPRS:
		    		networkType = "GPRS";
		    		break;
		    	case TelephonyManager.NETWORK_TYPE_HSDPA:
		    		networkType = "3G";
		    		break;
		    }
		    
		    //MNC AND MCC
		    String networkOperator = tm.getNetworkOperator();
		    if (networkOperator != null) {
		        mcc = Integer.parseInt(networkOperator.substring(0, 3));
		        mnc = Integer.parseInt(networkOperator.substring(3));
		    }		    
		}catch(Exception e){
			
		}
	}
}
