package com.jadblack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JadBlackSignatureField extends LinearLayout {
	public static final String tag = JadBlackSignatureField.class.getName();
	
	public String filePath;
	
	//Activity 
	public Activity mActivity;
	
	//Views
	public Button pressToSign;
	public TextView pressSignatureToSignAgain;
	public ImageView signatureViewer;
	
	//Request Code
	public int requestCode = 0;
	private boolean receivedCallback = false;
	
	public static class JadBlackSignatureFieldData{
		String filePath = "";
		
		public void setFromBundle(Bundle data){
			if( data.containsKey("file_path") ) filePath = data.getString("file_path");
		}
		
		public String getFilePath(){
			if(filePath == null) filePath = "";
			return filePath;
		}
	}

	//Super methods
	public JadBlackSignatureField(Context context){ super(context); }
	public JadBlackSignatureField(Context context, AttributeSet attrs){ super(context, attrs); }
	
	//Request Code Getter/Setter
	public void setRequestCode(int r){
		requestCode = r;		
	}
	public int getRequestCode(){
		return requestCode;
	}
	
	public void start(final Activity mActivity){
		if(mActivity != null)
			launchSignatureApp(mActivity);
	}

	//Initialize
	public void initialize(final Activity mActivity){
		//Inflate Views
		inflate(mActivity, R.layout.jadblack_signaturecapturefield, this);	
		
		//Get Activity
		this.mActivity = mActivity;
		
		//Get controls
		pressToSign = (Button) findViewById(R.id.pressToSign);
		pressSignatureToSignAgain = (TextView) findViewById(R.id.pressSignatureToSignAgain);
		signatureViewer = (ImageView) findViewById(R.id.signatureViewer);
		
		//Add touch functionality
		pressToSign.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				launchSignatureApp(mActivity);
			}
		});
		signatureViewer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				launchSignatureApp(mActivity);
			}
		});			
	}
	
	public void launchSignatureApp(final Activity mActivity){
		try{
			receivedCallback = false;
			//filePath = "";
			
			Intent signature = new Intent();
			signature.setPackage("ec.com.inalambrik.signature");
			signature.setAction(Intent.ACTION_SEND);
			mActivity.startActivityForResult(signature, requestCode);
		}catch(Exception e){
			String url = "http://www.itracknow.com/downloads/signature.apk";
			Intent browserIntent = new Intent(Intent.ACTION_VIEW);
			browserIntent.setData(Uri.parse(url));
			mActivity.startActivity(browserIntent);			
		}
	}
	
	//Handle Request
	public boolean handleRequest(int requestCode, int resultCode, Intent data){
		if(this.requestCode == requestCode){
			receivedCallback = true;
			//filePath = "";
			if(resultCode == Activity.RESULT_OK) 
				filePath = data.getStringExtra("filename");			
			
			loadPicture();
			return true;
		}
		return false;
	}
	
	
	public void loadPicture(){
		if(receivedCallback == false) return;
		if(filePath == null){
			filePath = "";
		}
		
		Log.d(tag, "Previewing picture: " + filePath);
		
		final Handler mHandler = new Handler();
		
		if(filePath.length() > 0 ){
			try{
				Thread t = new Thread(){
					public void run(){
						try{
							final Bitmap b = BitmapFactory.decodeFile(filePath);							
							mHandler.post(new Runnable(){
								public void run(){
									signatureViewer.setImageBitmap(b);
									signatureViewer.setVisibility(View.VISIBLE);
									pressSignatureToSignAgain.setVisibility(View.VISIBLE);
									pressToSign.setVisibility(View.GONE);
								}
							});																					
						}catch(Exception e){
							receivedCallback = false;
							filePath = "";
						}
					}
				};
				t.start();        		        
	        }catch(Exception e){}
		}else{
			mHandler.post(new Runnable(){
				public void run(){
					pressToSign.setVisibility(View.VISIBLE);
					signatureViewer.setVisibility(View.GONE);
					pressSignatureToSignAgain.setVisibility(View.GONE);
					
				}
			});
		}
	}
	
	public Bundle getBundle(){
		Bundle data = new Bundle();
		data.putString("file_path", filePath);
		data.putBoolean("received_callback", receivedCallback);		
		return data;
	}
	
	public void updateSelf(Bundle stateData){
		if( stateData.containsKey("start") ){
			launchSignatureApp(mActivity);
			return;
		}
		if( stateData.containsKey("file_path") ) filePath = stateData.getString("file_path");
		if( stateData.containsKey("received_callback") ) receivedCallback = stateData.getBoolean("received_callback");
		handleChange();
	}
	
	public void handleChange(){
		loadPicture();
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
	    updateSelf(ss.stateData);	    
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
