
package com.jadblack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class JadBlackCodeScannerField extends LinearLayout {
	public final String tag = "JadBlackCodeScannerField";
	
	//State Keys
	public static final String KEY_CODE_STRING = "code_string";
	public static final String KEY_CODE_FORMAT = "code_format";
	
	//Variables
	public String codeString;
	public String codeFormat;
	public int requestCode;
	
	//Controls
	public TextView codeInput;
	public Button scanButton;
	
	//CodeScannerData
	public static class JadBlackCodeScannerFieldData{
		String codeString = "";
		String codeFormat = "";
		
		public void setFromBundle(Bundle data){
			if( data.containsKey(KEY_CODE_STRING) ) codeString = data.getString(KEY_CODE_STRING);
			if( data.containsKey(KEY_CODE_FORMAT) ) codeFormat = data.getString(KEY_CODE_FORMAT);
		}
		
		public String getCodeString(){
			return codeString;
		}
	}
	
	//View Constructors
	public JadBlackCodeScannerField(Context context){ super(context); }
	public JadBlackCodeScannerField(Context context, AttributeSet attrs){ super(context, attrs); }
		
	//Initialize
	public void initialize(final Activity mActivity){
		//Inflate View
		inflate(mActivity, R.layout.jadblack_code_scanner_field, this);
		
		//Init variables
		codeString = "";
		codeFormat = "";
		
		//Get Controls
		codeInput = (TextView) findViewById(R.id.codeInput);
		scanButton = (Button) findViewById(R.id.scanButton);
		scanButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				(new IntentIntegrator(mActivity)).initiateScan();				
			}
		});
	}
	
	//Get codeString
	public String getCodeString(){
		return codeString;
	}
	
	
	//Set requestCode
	public void setRequestCode(int r){
		requestCode = r;
	}
	
	//Get requestCode
	public int getRequestCode(){
		return requestCode;
	}
	
	//Handle request
	public boolean handleRequest(int requestCode, int resultCode, Intent intent){
		IntentResult scan = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		//Scanner not invoked
		if(scan == null) return false;
		
		String content = scan.getContents();
		String format = scan.getFormatName();
		
		if(content != null && !content.equalsIgnoreCase("") ){
			codeString = content;
			codeFormat = format;
			updateUi();
			return true;
		}
		return false;
	}
	
	public void updateUi(){
		if(codeString.equalsIgnoreCase("") == true) return;
		
		codeInput.setText(codeString);
	}
	
	public Bundle getBundle(){
		Bundle data = new Bundle();
		data.putString(KEY_CODE_STRING, codeString);
		data.putString(KEY_CODE_FORMAT, codeFormat);
		return data;
	}
	
	public void restoreSelf(Bundle stateData){
		if( stateData.containsKey(KEY_CODE_STRING) ) codeString = stateData.getString(KEY_CODE_STRING);
		if( stateData.containsKey(KEY_CODE_FORMAT) ) codeFormat = stateData.getString(KEY_CODE_FORMAT);
		updateUi();
	}	
	
	//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	//Save State Methods
	public Parcelable onSaveInstanceState(){
		Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);
	    ss.stateData = getBundle();
	    return ss;
	}
	
	public void onRestoreInstanceState(Parcelable state){
		if(!(state instanceof SavedState)){ super.onRestoreInstanceState(state); return; }
	    SavedState ss = (SavedState) state;
	    super.onRestoreInstanceState(ss.getSuperState());
	    restoreSelf(ss.stateData);	    
	}
	
	static class SavedState extends BaseSavedState{
		Bundle stateData;	
		SavedState(Parcelable superState){  super(superState);}		
		private SavedState(Parcel in){ super(in); stateData = in.readBundle(); }
	    public void writeToParcel(Parcel out, int flags){ super.writeToParcel(out, flags); out.writeBundle(stateData); }	    
	    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){
	    	public SavedState createFromParcel(Parcel in) { return new SavedState(in);}
	    	public SavedState[] newArray(int size) { return new SavedState[size]; }
	    };
	}
}
