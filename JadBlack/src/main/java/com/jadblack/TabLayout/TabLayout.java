package com.jadblack.TabLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import java.util.ArrayList;

public class TabLayout extends com.google.android.material.tabs.TabLayout implements
        com.google.android.material.tabs.TabLayout.OnTabSelectedListener {

    public final static String TAG = TabLayout.class.getSimpleName();

    private ArrayList<String> tabsTitles;
    private ArrayList<Tab> mTabs;
    private int mSelectedTabPosition;
    private TabLayoutInteractionListener mListener;

    /**
     * Required constructors
     */
    public TabLayout(Context context) {
        super(context);
        init();
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     * Saving State
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.setSelectedTabPosition(mSelectedTabPosition);
        savedState.setTabsTitles(tabsTitles);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        ArrayList<String> tabsTitles = savedState.getTabsTitles();
        mSelectedTabPosition = savedState.getSelectedTabPosition();

        //Restore tabs
        for (String title : tabsTitles) {
            addTab(title);
        }

        //Restore selection
        if (mSelectedTabPosition > 0)
            getTabAt(mSelectedTabPosition - 1).select();
    }

    protected static class SavedState extends BaseSavedState {
        private ArrayList<String> mTabsTitles;
        private int mSelectedTabPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel in) {
            super(in);
            mTabsTitles = in.readArrayList(String.class.getClassLoader());
            mSelectedTabPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeList(mTabsTitles);
            out.writeInt(mSelectedTabPosition);
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

        public void setSelectedTabPosition(int selectedTabPosition) {
            mSelectedTabPosition = selectedTabPosition;
        }

        public void setTabsTitles(ArrayList<String> tabsTitles) {
            mTabsTitles = tabsTitles;
        }

        public ArrayList<String> getTabsTitles() {
            return mTabsTitles;
        }

        public int getSelectedTabPosition() {
            return mSelectedTabPosition;
        }
    }


    /**
     * Custom implementation
     */
    //Init
    public void init() {
        mTabs = new ArrayList<>();
        tabsTitles = new ArrayList<>();

        mSelectedTabPosition = 1;

        setOnTabSelectedListener(this);
    }

    //SetListener
    public void setListener(TabLayoutInteractionListener listener) {
        mListener = listener;
    }

    //Shortcut for adding tabs with String title
    public void addTab(String title) {
        Tab tab = newTab().setText(title);

        int tabCount = getTabCount() + 1;
        boolean selected = tabCount == mSelectedTabPosition ? true : false;
        addTab(tab, selected);

        mTabs.add(tab);
        tabsTitles.add(title);
    }

    //Handle change
    public void handleChange(Bundle data) {
        if(data == null)
            return;

        //Add tab
        if(data.containsKey("add_tab")) {
            addTab(data.getString("add_tab"));
        }
    }

    //Get bundle
    public Bundle getBundle() {
        Bundle data = new Bundle();
        data.putInt("selected_tab_position", mSelectedTabPosition);

        return data;
    }

    /**
     * TabSelectedListener implementation
     */
    @Override
    public void onTabSelected(Tab tab) {
        mSelectedTabPosition = mTabs.indexOf(tab) + 1;
        if(mListener != null && mSelectedTabPosition > 0)
            mListener.onTabSelected();
    }

    @Override
    public void onTabUnselected(Tab tab) {

    }

    @Override
    public void onTabReselected(Tab tab) {

    }

    /**
     * Callbacks Interface
     */
    public interface TabLayoutInteractionListener {
        public void onTabSelected();
    }
}
