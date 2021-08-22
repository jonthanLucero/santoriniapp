package com.jadblack;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class JadBlackThrobber extends LinearLayout {
	public final String tag = "JadBlackThrobber";
	
	private ProgressBar throbber;
	private TextView throbberMessage;
	
	private String messageToShow = "";
	private int visibleStatus = LinearLayout.GONE;

	public JadBlackThrobber(Context context) {
		super(context);
		initialize(context);
	}
	
	public JadBlackThrobber(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public void initialize(Context context){
		//Layout Parms
		LinearLayout.LayoutParams layoutParams;
		
		//Throbber
		layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER_VERTICAL;
		
		throbber = new ProgressBar(context);
		addView(throbber, layoutParams);
		
		//Throbber Message
		layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(5, 0, 0, 0);
		layoutParams.gravity = Gravity.CENTER_VERTICAL;
		
		throbberMessage = new TextView(context);
		throbberMessage.setText(messageToShow);
		throbberMessage.setTextColor(Color.BLACK);
		throbberMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		addView(throbberMessage, layoutParams);
		
		//SET VISIBILITY
		setVisibility(visibleStatus);
	}

	public void show(){
		visibleStatus = LinearLayout.VISIBLE;
		messageToShow = "";
		
		setVisibility(LinearLayout.VISIBLE);
	}
	
	public void show(String message){
		visibleStatus = LinearLayout.VISIBLE;
		messageToShow = message;
		
		throbberMessage.setText(message);
		setVisibility(LinearLayout.VISIBLE);
	}
	
	public void hide(){
		visibleStatus = LinearLayout.GONE;
		messageToShow = "";
		
		setVisibility(LinearLayout.GONE);
	}
	
	public void updateSelf(Bundle stateData){
		if( stateData.containsKey("visibility") ) visibleStatus = stateData.getInt("visibility");		
		if( stateData.containsKey("message") ) messageToShow = stateData.getString("message");
		handleConfigChange();		
	}
	
	public void handleConfigChange(){
		if( visibleStatus == LinearLayout.GONE){
			hide();
		}else{
			show(messageToShow);
		}
	}
		
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);
	    ss.messageToShow = this.messageToShow;
	    ss.visibleStatus = this.visibleStatus;	   	  
	    return ss;
	}
	
	public void onRestoreInstanceState(Parcelable state) {
		if(!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

	    SavedState ss = (SavedState) state;
	    super.onRestoreInstanceState(ss.getSuperState());
	    
	    this.messageToShow = ss.messageToShow;
	    this.visibleStatus = ss.visibleStatus;
	    
	    handleConfigChange();
	  }
	
	static class SavedState extends BaseSavedState{
		String messageToShow;
		int visibleStatus;
		
		SavedState(Parcelable superState){
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			this.messageToShow = in.readString();
			this.visibleStatus = in.readInt();
		}

	    public void writeToParcel(Parcel out, int flags) {
	      super.writeToParcel(out, flags);
	      out.writeString(this.messageToShow);
	      out.writeInt(this.visibleStatus);
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
