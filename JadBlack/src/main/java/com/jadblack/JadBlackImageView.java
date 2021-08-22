package com.jadblack;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class JadBlackImageView extends ImageView
	implements OnClickListener{
	public static final String tag = "JadBlackImageView";
	int visibility = View.VISIBLE;
	String filePath = "";
	Bitmap mBitmap = null;
	
	//Supers
	public JadBlackImageView(Context context){ super(context); }
	public JadBlackImageView(Context context, AttributeSet attrs){ super(context, attrs); }
	public JadBlackImageView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
	
	public Bundle getBundle(){
		Bundle data = new Bundle();	
		data.putInt("visibility", getVisibility());
		data.putString("file_path", filePath);
		return data;
	}
	
	public void updateSelf(Bundle stateData){
		visibility = getVisibility();
		if(stateData.containsKey("visibility")) visibility = stateData.getInt("visibility");
		if(stateData.containsKey("file_path")) filePath = stateData.getString("file_path");
		handleChange();
	}
	
	public void handleChange(){
		setVisibility(visibility);
		
		Log.d(tag, filePath);
		if( filePath.length() > 0 ){
			mBitmap = com.jadblack.ImageFunctions.getImageFromFile(filePath);
			if(mBitmap != null){
				new Handler().post(new updateBitmap());
				setOnClickListener(JadBlackImageView.this);
				Log.d(tag, "Image loaded.");
			}else{
				Log.d(tag, "Image not decoded.");
			}
		}
	}
	
	public class updateBitmap implements Runnable{
		public void run() {
			setImageBitmap(mBitmap);
			setScaleType(ImageView.ScaleType.CENTER_CROP);
		}		
	}
	
	public void onClick(View v) {
		if(filePath != null && filePath.length() > 0) {
			try {
				Intent viewImageIntent = new Intent();
				viewImageIntent.setAction(Intent.ACTION_VIEW);
				viewImageIntent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
				getContext().startActivity(viewImageIntent);
			} catch(Exception e) {
				e.printStackTrace();
				Toast.makeText(getContext(), "No se pudo abrir imagen en Galería.", Toast.LENGTH_LONG).show();
			}
		}
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
		if(!(state instanceof SavedState)){ 
			super.onRestoreInstanceState(state); 
			return;
		}
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
