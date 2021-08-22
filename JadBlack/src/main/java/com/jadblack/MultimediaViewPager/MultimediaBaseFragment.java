package com.jadblack.MultimediaViewPager;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jadblack.MultimediaViewPager.MultimediaItemManager.MultimediaItem;
import com.jadblack.R;

public class MultimediaBaseFragment extends Fragment {
	
	public final static String tag = MultimediaBaseFragment.class.getName();
	
	public MultimediaItem mItem;	
	
	public View content;	
	public RelativeLayout statusIndicator;
	public ProgressBar loadingIndicator;
	public LinearLayout errorIndicator;
	public Button retryWork;
		
	public void setItem(MultimediaItem item) {
		mItem = item;
	}
	
	public void getControlViews(View v){
		content = (View) v.findViewById(R.id.multimedia_content);
		statusIndicator = (RelativeLayout) v.findViewById(R.id.multimedia_status_indicator);
		
		loadingIndicator = (ProgressBar) v.findViewById(R.id.loading_indicator);
		errorIndicator = (LinearLayout) v.findViewById(R.id.error_indicator);
		retryWork = (Button) v.findViewById(R.id.retryWork);
		
		retryWork.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doWork();
			}
		});			
	}
	
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle("item", mItem.getBundle());		
	}
	
	public void loadState(Bundle state){
		if(state != null){
			mItem = new MultimediaItemManager.MultimediaItem();
			mItem.setFromBundle(state.getBundle("item"));			
		}
	}
	
	public void showContent(){
		content.setVisibility(View.VISIBLE);
		statusIndicator.setVisibility(View.GONE);
	}
	
	public void showStatusIndicator(){
		content.setVisibility(View.GONE);
		statusIndicator.setVisibility(View.VISIBLE);
	}
	
	public void showError(){
		showStatusIndicator();
		loadingIndicator.setVisibility(View.GONE);
		errorIndicator.setVisibility(View.VISIBLE);		
	}
	
	public void showLoading(){
		showStatusIndicator();
		loadingIndicator.setVisibility(View.VISIBLE);
		errorIndicator.setVisibility(View.GONE);		
	}
	
	public void doWork(){
		showLoading();
	}	
	
	public void onDestroy() {
		super.onDestroy();
		Log.d(tag, "BaseFragment destroyed");
	}
}
