
package com.jadblack.MultimediaViewPager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jadblack.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.matabii.dev.scaleimageview.ScaleImageView;

import java.io.File;

public class MultimediaImageFragment extends MultimediaBaseFragment {
	
	public final static String tag = MultimediaImageFragment.class.getName(); 
	
	public Bitmap image;
	public ScaleImageView imageContent;
	public TextView imageTitle;	 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Get fragment layout and views
        View rootView = inflater.inflate(R.layout.fragment_multimedia_image, container, false);
		getControlViews(rootView);
		
		//Load state
		loadState(savedInstanceState);
				
		//Get custom controls
		imageContent = (ScaleImageView) rootView.findViewById(R.id.image_content);
		imageTitle = (TextView) rootView.findViewById(R.id.image_title);
		
		//Do work
		doWork();
		
		//Return layout
        return rootView;
    }
	
	public void doWork(){
		super.doWork();
		
		//Set Title
		imageTitle.setText(mItem.title);
		
		//Get screen size
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		final int totalWidth = dm.widthPixels;
		final int totalHeight = dm.heightPixels;	
			
		Ion.with(this)
				.load("http://" + mItem.url)
				//.setLogging(tag, Log.DEBUG)
				.write(new File(mItem.path))
				.setCallback(new FutureCallback<File>() {
					public void onCompleted(Exception ex, File f) {
						if(ex == null){
							image = getScaledBitmap(mItem.path, totalWidth, totalHeight);
							imageContent.setImageBitmap(image);
							showContent();
						}else{
							showError();
						}
					}
				});
	}
	
	public Bitmap getScaledBitmap(String imagePath, int reqWidth, int reqHeight){
		//Options for Bitmap. inJustDecodeBounds = true to load file properties only
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);		
		
		//inSampleSize = 2 to reduce image loading by half
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(imagePath, options);			
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	
	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

		return inSampleSize;
	}	
	
	//Menu	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_multimedia_image, menu);
	}
		
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.show_description){
			MultimediaInfoDialogFragment infoDialog = MultimediaInfoDialogFragment.newInstance(mItem.title, mItem.description);
			infoDialog.show(getActivity().getSupportFragmentManager(), "dialog");	
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
