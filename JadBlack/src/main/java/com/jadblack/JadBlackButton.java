package com.jadblack;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class JadBlackButton extends Button{
	public int visibility = View.VISIBLE;
	public String label = "";
	public boolean enabled = true;
	
	public JadBlackButton(Context context){ super(context); }
	public JadBlackButton(Context context, AttributeSet attrs){ super(context, attrs); }
	public JadBlackButton(Context context, AttributeSet attrs, int defStyle){ super(context, attrs, defStyle); }
	
	public void updateSelf(Bundle stateData){
		this.visibility = getVisibility();
		this.label = getText().toString();
		this.enabled = isEnabled();
		if( stateData.containsKey("visibility") ) this.visibility = stateData.getInt("visibility");		
		if( stateData.containsKey("label") ) this.label = stateData.getString("label");
		if( stateData.containsKey("enabled") ) this.enabled = stateData.getBoolean("enabled");
		handleChange();		
	}
	
	public void handleChange(){
		setVisibility(visibility);
		setText(label);	
		setEnabled(enabled);		
	}
	
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);
	    ss.visibility = getVisibility();
	    ss.label = getText().toString();
	    ss.enabled = isEnabled();
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
	    this.label = ss.label;
	    this.enabled = ss.enabled;
	    
	    handleChange();
	  }
	
	static class SavedState extends BaseSavedState{
		int visibility;
		String label;
		boolean enabled;
		
		SavedState(Parcelable superState){
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			this.visibility = in.readInt();
			this.label = in.readString();
			
			int enabledInt = in.readInt();			
			this.enabled = enabledInt > 0 ? true : false;
		}

	    public void writeToParcel(Parcel out, int flags) {
	      super.writeToParcel(out, flags);
	      out.writeInt(this.visibility);
	      out.writeString(this.label);
	      if(this.enabled){
	    	  out.writeInt(1);
	      }else{
	    	  out.writeInt(0);
	      }	      
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
