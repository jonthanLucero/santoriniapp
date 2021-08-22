
package com.jadblack.MultimediaViewPager;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jadblack.R;
import com.jadblack.MultimediaViewPager.MultimediaItemManager.MultimediaItem;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MultimediaFileFragment extends MultimediaBaseFragment {
	
	public final static String tag = MultimediaFileFragment.class.getName(); 
	
	public ImageView extension;
	public TextView title;
	public TextView description;
	public Button viewFile;		 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Get fragment layout and views
        View rootView = inflater.inflate(R.layout.fragment_multimedia_file, container, false);
		getControlViews(rootView);
		
		//Load State
		loadState(savedInstanceState);
		
		//Get custom controls
		extension = (ImageView) rootView.findViewById(R.id.extension);
		switch(mItem.type){
		case MultimediaItem.Type.PDF:
			extension.setImageResource(R.drawable.ic_app_pdf);
			break;			
		case MultimediaItem.Type.WORD:
			extension.setImageResource(R.drawable.ic_app_word);
			break;
		}		
		
		title = (TextView) rootView.findViewById(R.id.title);
		title.setText(mItem.title);
		
		description = (TextView) rootView.findViewById(R.id.description);
		description.setText(mItem.description);
		
		viewFile = (Button) rootView.findViewById(R.id.view_file);
		viewFile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doWork();			
			}
		});	
		
		showContent();
		
		//Return layout
        return rootView;
	}
		
	public void doWork() {
		super.doWork();

		Ion.with(this)
				.load("http://" + mItem.url)
				.write(new File(mItem.path))
				.setCallback(new FutureCallback<File>() {
					public void onCompleted(Exception ex, File f) {
						if(ex == null){
							switch(mItem.type){
							case MultimediaItem.Type.PDF:
								Intent pdf = new Intent(Intent.ACTION_VIEW);
								pdf.setDataAndType(Uri.fromFile(new File(mItem.path)), "application/pdf");
								startActivity(Intent.createChooser(pdf, "Abrir PDF"));
								break;

							case MultimediaItem.Type.WORD:
								Intent word = new Intent(Intent.ACTION_VIEW);
								word.setDataAndType(Uri.fromFile(new File(mItem.path)), "application/word");
								startActivity(Intent.createChooser(word, "Abrir Word"));
								break;
							}
							showContent();
						}else{
							showError();
						}
					}
				});
	}
}
