package com.jadblack;

import java.util.Date;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class JadBlackTimeField extends Button {
	private int visibility;
	private boolean enabled;
	private Date time;
	
	//SUPER CONSTRUCTORS
	public JadBlackTimeField(Context context) {
		super(context);
		initialize((Activity) context);
	}
	public JadBlackTimeField(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize((Activity) context);
	}
	public JadBlackTimeField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize((Activity) context);
	}
	
	//INITIALIZE
	private void initialize(final Activity activity){
		time = DateFunctions.now();
		updateUI();
		
		setOnClickListener( new Button.OnClickListener(){
			public void onClick(View view){
				TimePickerDialog d = new TimePickerDialog(
						activity,
						getTimeSetListener(),
						DateFunctions.getHour(time),
						DateFunctions.getMinute(time),
						true
				);
				d.setOwnerActivity(activity);
				d.show();
			}});
	}
	//SET DATE STRING
	private void updateUI(){
		String timeString = DateFunctions.getHourString(time) + " : " +  DateFunctions.getMinute(time);
		setText(" " + timeString + " ");
	}
	//SET TIME
	public void setTime(Date t){
		time = t;
		updateUI();
	}
	
	//GET TIME
	public Date getTime(){
		return time;
	}
	
	//CALLBACK METHOD WHEN SETTING DATE ON DATE PICKER DIALOG
	public TimePickerDialog.OnTimeSetListener getTimeSetListener(){
		TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener(){
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				if( view.isInEditMode() == false ){
		            setTime( 
		            	com.jadblack.DateFunctions.setDateTime(
	            			com.jadblack.DateFunctions.getYear(time),
	            			com.jadblack.DateFunctions.getMonth(time),
	            			com.jadblack.DateFunctions.getDay(time),
		            		hourOfDay,
		            		minute,
		            		0
		            	)	            		
		            );
		            updateUI();
				}
	        }
		};
		return timeSetListener;		
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
	    ss.timeLong = time.getTime();
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
	    this.time = new Date(ss.timeLong);
	    
	    handleChange();
	  }
	
	static class SavedState extends BaseSavedState{
		int visibility;
		boolean enabled;
		long timeLong;
		
		SavedState(Parcelable superState){
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			this.visibility = in.readInt();
			
			int enabledInt = in.readInt();			
			this.enabled = enabledInt > 0 ? true : false;
			
			this.timeLong = in.readLong();
		}

	    public void writeToParcel(Parcel out, int flags) {
	      super.writeToParcel(out, flags);
	      out.writeInt(this.visibility);
	      
	      if(this.enabled){
	    	  out.writeInt(1);
	      }else{
	    	  out.writeInt(0);
	      }
	      out.writeLong(timeLong);
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