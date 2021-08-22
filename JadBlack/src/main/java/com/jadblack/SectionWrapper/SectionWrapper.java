package com.jadblack.SectionWrapper;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by DESARROLLO on 12/2/2015.
 */
public class SectionWrapper extends LinearLayout {

    /**
     * Constructors
     */
    public SectionWrapper(Context context) {
        super(context);
    }

    public SectionWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SectionWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * State save/restore
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.setIsVisible(getVisibility() == VISIBLE ? 1 : 0);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        setVisibility(savedState.getIsVisible() == 1 ? VISIBLE : GONE);
    }


    /**
     * Parcelable for State
     */
    protected static class SavedState extends BaseSavedState {
        private int isVisible;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel in) {
            super(in);
            isVisible = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isVisible);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public void setIsVisible(int isVisible) {
            this.isVisible = isVisible;
        }

        public int getIsVisible() {
            return isVisible;
        }
    }


    /**
     * Custom implementation
     */
    public void handleChange(Bundle data) {
        if(data == null)
            return;

        //Set visibility
        if(data.containsKey("visibility")) {
            int isVisible = data.getInt("visibility");
            setVisibility(isVisible == 1 ? VISIBLE : GONE);
        }
    }

}
