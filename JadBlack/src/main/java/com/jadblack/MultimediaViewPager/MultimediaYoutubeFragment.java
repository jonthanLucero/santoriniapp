
package com.jadblack.MultimediaViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jadblack.R;

public class MultimediaYoutubeFragment extends MultimediaBaseFragment {
	
	public final static String tag = MultimediaYoutubeFragment.class.getName(); 
	
	public TextView title;
	public TextView description;
	public Button playVideo;		 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Get fragment layout and views
        View rootView = inflater.inflate(R.layout.fragment_multimedia_youtube, container, false);
		getControlViews(rootView);
		
		//Load State
		loadState(savedInstanceState);
		
		//Get custom controls
		title = (TextView) rootView.findViewById(R.id.title);
		title.setText(mItem.title);
		
		description = (TextView) rootView.findViewById(R.id.description);
		description.setText(mItem.description);
		
		playVideo = (Button) rootView.findViewById(R.id.play_video);
		playVideo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent youtube = new Intent(Intent.ACTION_VIEW);
				youtube.setData(Uri.parse("http://" + mItem.url));
				startActivity(Intent.createChooser(youtube, "Ver Video"));			
			}
		});	
		
		showContent();
		
		//Return layout
        return rootView;
    }	
}
