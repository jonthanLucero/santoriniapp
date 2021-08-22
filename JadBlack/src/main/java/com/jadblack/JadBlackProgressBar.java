package com.jadblack;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

public class JadBlackProgressBar extends ProgressBar {
	public int visibility = View.VISIBLE;
	public int value = 0;
	
	public JadBlackProgressBar(Context context){ super(context); init();}
	public JadBlackProgressBar(Context context, AttributeSet attrs){ super(context, attrs); init();}
	public JadBlackProgressBar(Context context, AttributeSet attrs, int defStyle){ super(context, attrs, defStyle); init();}

	public void init(){
		setMax(100);
		value = 0;
	}
	
	public void updateSelf(Bundle stateData){
		this.visibility = getVisibility();
		if( stateData.containsKey("visibility") ) this.visibility = stateData.getInt("visibility");		
		if( stateData.containsKey("value") ) this.value = stateData.getInt("value");
		handleChange();		
	}
	
	public void handleChange(){
		setVisibility(visibility);
		setProgress(value);		
	}
	
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);
	    ss.visibility = getVisibility();
	    ss.value = value;
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
	    this.value = ss.value;
	    
	    handleChange();
	  }
	
	static class SavedState extends BaseSavedState{
		int visibility;
		int value;
		
		SavedState(Parcelable superState){ super(superState); }

		private SavedState(Parcel in) {
			super(in);
			this.visibility = in.readInt();
			this.value = in.readInt();			
		}

	    public void writeToParcel(Parcel out, int flags) {
	      super.writeToParcel(out, flags);
	      out.writeInt(this.visibility);
	      out.writeInt(this.value);
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
