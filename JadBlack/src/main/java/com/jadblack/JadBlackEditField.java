package com.jadblack;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.EditText;

public class JadBlackEditField extends EditText{
	boolean enabled = true;
	
	public JadBlackEditField(Context context){ super(context); }
	public JadBlackEditField(Context context, AttributeSet attrs){ super(context, attrs); }
	public JadBlackEditField(Context context, AttributeSet attrs, int defStyle){ super(context, attrs, defStyle); }
	
	public Bundle getBundle(){
		Bundle data = new Bundle();		
		data.putBoolean("enabled", isEnabled());
		return data;
	}
	
	public void updateSelf(Bundle stateData){
		enabled = isEnabled();
		if(stateData.containsKey("enabled")) enabled = stateData.getBoolean("enabled");
		handleChange();
	}
	
	public void handleChange(){
		setEnabled(enabled);		
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
