
package com.jadblack;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class JadBlackLabel extends TextView {
	public final static String tag = "JadBlackLabel";
	
	public final static int NO_COLOR = -99999;
	
	public int visibility = View.VISIBLE;
	public String label;
	public int foreColor = NO_COLOR;
	public int backColor = NO_COLOR;
	
	//SUPER IMPLEMENTATIONS
	public JadBlackLabel(Context context){ super(context); }	
	public JadBlackLabel(Context context, AttributeSet attrs){ super(context, attrs); }	
	public JadBlackLabel (Context context, AttributeSet attrs, int defStyle){ super(context, attrs, defStyle); }
	
	public void updateSelf(Bundle stateData){
		this.visibility = getVisibility();
		this.label = getText().toString();
		this.foreColor = getCurrentTextColor();
		
		if( stateData.containsKey("visibility") ) this.visibility = stateData.getInt("visibility");		
		if( stateData.containsKey("label") ) this.label = stateData.getString("label");
		if( stateData.containsKey("fore_color") ) this.foreColor = stateData.getInt("fore_color");
		if( stateData.containsKey("back_color") ) this.backColor = stateData.getInt("back_color");
				
		handleChange();		
	}
	
	public void handleChange(){
		setVisibility(visibility);
		setText(label);

		if(foreColor != NO_COLOR) setTextColor(foreColor);
		if(backColor != NO_COLOR) setBackgroundColor(backColor);			
	}
	
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);
	    ss.visibility = getVisibility();
	    ss.label = getText().toString();	  
	    
	    ss.foreColor = foreColor;
	    ss.backColor = backColor;	    	   	  
	    
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
	    this.foreColor = ss.foreColor;
	    this.backColor = ss.backColor;
	    
	    handleChange();
	  }
	
	static class SavedState extends BaseSavedState{
		int visibility;
		String label;
		int foreColor;
		int backColor;
		
		SavedState(Parcelable superState){
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			this.visibility = in.readInt();
			this.label = in.readString();
			this.foreColor = in.readInt();
			this.backColor = in.readInt();
		}

	    public void writeToParcel(Parcel out, int flags) {
	      super.writeToParcel(out, flags);
	      out.writeInt(this.visibility);
	      out.writeString(this.label);
	      out.writeInt(this.foreColor);
	      out.writeInt(this.backColor);
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
