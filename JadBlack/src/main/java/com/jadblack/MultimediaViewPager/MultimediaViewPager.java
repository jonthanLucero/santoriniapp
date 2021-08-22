
package com.jadblack.MultimediaViewPager;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class MultimediaViewPager extends ViewPager { 

	Context mContext;
	MultimediaItemManager mItemManager;	
	FragmentManager mFragmentManager;
	
	public class Parms{
		public static final String DATA = "data";
		public static final String POSITION = "position";
	}
	
	public MultimediaViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}
	
	public void init(Context context, FragmentManager fm){
		mContext = context;
		mFragmentManager = fm;
		mItemManager = new MultimediaItemManager();
		setVisibility(View.GONE);
	}
	
	public Bundle getBundle(){
		Bundle data = new Bundle();
		data.putBundle(Parms.DATA, mItemManager.getBundle());
		data.putInt(Parms.POSITION, getCurrentItem());		
		return data;
	}
	
	public void load(){
		MultimediaPagerAdapter mAdapter = new MultimediaPagerAdapter(mFragmentManager);
		mAdapter.setItemManager(mItemManager);
		setAdapter(mAdapter);		
		setVisibility(View.VISIBLE);
	}
	
	public void setFromBundle(Bundle data){
		if(data.containsKey(Parms.DATA)){
			mItemManager = new MultimediaItemManager();
			mItemManager.setFromBundle(data.getBundle(Parms.DATA));
			load();
		}
		if(data.containsKey(Parms.POSITION)){
			int position = data.getInt(Parms.POSITION);
			setCurrentItem(position);
		}
	}
}
