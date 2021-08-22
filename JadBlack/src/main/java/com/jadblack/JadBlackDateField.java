package com.jadblack;

import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class JadBlackDateField extends Button {
	public Date date;
	public int visibility = View.VISIBLE;
	public boolean enabled = true;
	
	//SUPER CONSTRUCTORS
	public JadBlackDateField(Context context) {
		super(context);
		initialize();
	}
	public JadBlackDateField(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	public JadBlackDateField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}
	
	//INITIALIZE
	private void initialize(){
		date = DateFunctions.today();
		updateUI();		
	}
	
	//SET DATE STRING
	private void updateUI(){
		String dateString = DateFunctions.getYYYYMMDDString(date);
		setText(" " + dateString + " ");
	}
	
	//SET DATE
	public void setDate(Date d){
		date = d;
		updateUI();
	}
	
	//SET DATE
	public void setDate(int year, int month, int day){
		setDate(com.jadblack.DateFunctions.setDate(year, month + 1, day));
	}
	
	//GET DATE
	public Date getDate(){
		return date;
	}
	
	//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	//STATE HANDLING METHODS
	public void updateSelf(Bundle stateData){
		this.visibility = getVisibility();
		this.enabled = isEnabled();
		if( stateData.containsKey("visibility") ) this.visibility = stateData.getInt("visibility");		
		if( stateData.containsKey("enabled") ) this.enabled = stateData.getBoolean("enabled");
		handleChange();		
	}
	
	public void handleChange(){
		setVisibility(visibility);
		setEnabled(enabled);
		setFocusable(enabled);
		updateUI();
	}
	
	public Parcelable onSaveInstanceState() {		
		Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);
	    ss.visibility = getVisibility();
	    ss.enabled = isEnabled();
	    ss.dateLong = date.getTime();
	    return ss;
	}
	
	public void onRestoreInstanceState(Parcelable state) {
		if(!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

	    SavedState ss = (SavedState) state;
	    super.onRestoreInstanceState(ss.getSuperState());
	    
	    this.visibility = ss.visibility;
	    this.enabled = ss.enabled;
	    this.date = new Date(ss.dateLong);
	    
	    handleChange();
	  }
	
	static class SavedState extends BaseSavedState{
		int visibility;
		boolean enabled;
		long dateLong;
		
		SavedState(Parcelable superState){
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			this.visibility = in.readInt();
			
			int enabledInt = in.readInt();			
			this.enabled = enabledInt > 0 ? true : false;
			
			this.dateLong = in.readLong();
		}

	    public void writeToParcel(Parcel out, int flags) {
	      super.writeToParcel(out, flags);
	      out.writeInt(this.visibility);
	      
	      if(this.enabled){
	    	  out.writeInt(1);
	      }else{
	    	  out.writeInt(0);
	      }
	      out.writeLong(dateLong);
	    }

	    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){
	    	public SavedState createFromParcel(Parcel in) {
	    		return new SavedState(in);
	    	}
	    	public SavedState[] newArray(int size) {
	    		return new SavedState[size];
	    	}
	    };
	}
}
