package com.jadblack;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class JadBlackCameraField extends LinearLayout {
	public final String tag = "JadBlackCameraField";
	
	private int selectPictureRequestCode = 0;
	private int takePictureRequestCode = 0;
	private int editSettingRequestCode = 1000;
	public String requestingAppName = "";

	// General Containers to Attach/Take Photo or just simply show photo.
	private FrameLayout attachOrTakePhotoContainer;
	private FrameLayout picturePreviewContainer;

	// Action Buttons
	private Button selectButton;
	private Button cameraButton;
	private LinearLayout erasePhotoButton;
	private LinearLayout zoomButton;
	private LinearLayout grantPermissionsContainer, takeOrAttachPicsContainer;

	// Image Details.
	private ImageView picturePreview;
	private TextView pictureFileName;
	private TextView pictureFileSize;
	private TextView problemLoadingImageTextView;
	private TextView actionButtonGrantPermissions;
	private TextView actionButtonGoToPermissionSettings;
	private TextView permissionSpecialInstructionsTextView;

	// Internal Variables
	private String fileName = "";
	private String filePath = "";
	private boolean receivedCallback = false;
	private boolean isHttpUrlPath = false;
	private Context mContext;
	private boolean photoWasChanged;
	private boolean isDisplayMode;
	ScopedStorage scopedStorage = new ScopedStorage();
	String mPermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
	PermissionListener mPermissionListener;


	public static class JadBlackCameraFieldData{
		String fileName = "";
		String filePath = "";
		
		public void setFromBundle(Bundle data){
			if( data.containsKey("file_name") ) fileName = data.getString("file_name");
			if( data.containsKey("file_path") ) filePath = data.getString("file_path");
		}
		
		public String getFilePath(){
			return filePath;
		}
	}
	
	public JadBlackCameraField(Context context){ super(context); }
	public JadBlackCameraField(Context context, AttributeSet attrs){ super(context, attrs); }

	public void setControlRequestsCodes(int selectPicture, int takePicture){
		selectPictureRequestCode = selectPicture;
		takePictureRequestCode = takePicture;
	}
	
	public int getSelectPictureRequestCode(){
		return selectPictureRequestCode;
	}
	
	public int getTakePictureRequestCode(){
		return takePictureRequestCode;
	}
	
	public void setRequestingAppName(String appName){
		requestingAppName = appName;
	}
	
	public void generateOutputFileName(){
		//PREPARE FILE NAME
		fileName = com.jadblack.StringFunctions.toString(
				com.jadblack.NumericFunctions.toLong(
				com.jadblack.DateFunctions.now()
			)
		) + ".jpg";
		Log.d(getClass().getSimpleName(), "Generated File Name: " + fileName);
	}
	
	public String getFilePath(){
		return filePath;
	}

	public String getFilePathIfWasChanged(){
		return photoWasChanged ? filePath.trim() : "";
	}
	
	public File getOutputDirectory(){
		//JADBLACK CAMERA FIELD FOLDER FOR PICTURES
		File mediaStorageDir = new File(
			Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES
			),
			requestingAppName
		);
		//CREATE IF DIRECTORY DOES NOT EXIST
		if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs() ){
	        	Log.d(getClass().getSimpleName(), "Cannot create directory");
	            return null;
	        }
	    }
		Log.d(getClass().getSimpleName(), "Directory found: " + mediaStorageDir.getAbsolutePath());
		return mediaStorageDir;		
	}
	
	public Uri getOutputUri(){
		//CHECK DIRECTORY
		File directory = getOutputDirectory();
		if(directory == null){
			return null;
		}
		//CONSTRUCT ABSOLUTE FILE PATH
		generateOutputFileName();
		File picture = new File(directory, fileName);
		filePath = picture.getAbsolutePath();
		Log.d(getClass().getSimpleName(), "File Path: " + filePath);
		
		return Uri.fromFile(picture);			
	}

	public void initialize(final Activity mActivity){

		mContext = mActivity;

		inflate(mActivity, R.layout.jadblackcamerafield_layout, this);

		// --------------------------------
		// Get Views from the Layout
		// --------------------------------
		picturePreviewContainer 	= (FrameLayout) findViewById(R.id.picturePreviewWrapper);
		attachOrTakePhotoContainer 	= (FrameLayout) findViewById(R.id.attachOrTakePhotoSignContainer);
		pictureFileName 			= (TextView) findViewById(R.id.pictureFileName);
		pictureFileSize 			= (TextView) findViewById(R.id.pictureFileSize);
		erasePhotoButton 			= (LinearLayout) findViewById(R.id.delete_picture);
		picturePreview 				= (ImageView) findViewById(R.id.picturePreview);
		selectButton 				= (Button) findViewById(R.id.selectButton);
		cameraButton 				= (Button) findViewById(R.id.cameraButton);
		zoomButton 					= (LinearLayout) findViewById(R.id.zoom_picture);
		problemLoadingImageTextView = (TextView) findViewById(R.id.image_loading_error_message);
		grantPermissionsContainer   = (LinearLayout) findViewById(R.id.grant_permissions_container);
		takeOrAttachPicsContainer   = (LinearLayout) findViewById(R.id.take_or_attach_pics_container);
		actionButtonGrantPermissions= (TextView) findViewById(R.id.action_button_grant_permission);
		actionButtonGoToPermissionSettings = (TextView) findViewById(R.id.action_button_go_config);
		permissionSpecialInstructionsTextView = (TextView) findViewById(R.id.permission_special_instructions_text_view);

		// --------------------------------
		// Set OnClickListeners
		// --------------------------------
		selectButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
                if (!PermissionCheckFunctions.checkWriteExternalStoragePermission(mActivity)) {
                    Toast.makeText(mActivity, "Permiso de Almacenamiento está deshabilitado. Por favor intentar nuevamente.", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    receivedCallback = false;
                    scopedStorage.fetchMediaLocation(mActivity,selectPictureRequestCode);
                } catch (Exception e) {
                    Toast.makeText(mActivity, "Memoria Multimedia no Disponible", Toast.LENGTH_LONG).show();
                }
			}
		});
		cameraButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view){

                if (!PermissionCheckFunctions.checkWriteExternalStoragePermission(mActivity)) {
                    Toast.makeText(mActivity, "Permiso de Almacenamiento está deshabilitado. Por favor intentar nuevamente.", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    receivedCallback = false;

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // Create the File where the photo should go
                    File photoFile = createImageFile(mContext);

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        // Getting the name and path.
                        fileName = photoFile.getName().trim();
                        filePath = photoFile.getAbsolutePath().trim();

                        Log.d(getClass().getSimpleName(),"File created for Camera=> Name: " + fileName + " - Path: " + filePath);

                        //
                        Uri photoURI = FileProvider.getUriForFile(mContext,
                                "ec.com.inalambrik.sfaplus.fileprovider", // Cant be renamed. Check the provider in Manifest.xml of the NativeCode.
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                                Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                            takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                        mActivity.startActivityForResult(takePictureIntent, takePictureRequestCode);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mActivity, "Ha ocurrido un error. Por favor intentar nuevamente.\nSi el problema persiste, intente adjuntar la imagen.", Toast.LENGTH_LONG).show();
                }
			}

		});
		erasePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				erasePhoto();
			}
		});

		zoomButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// If the photo is locally then ask for the permission to access disk.
				if(!isHttpUrlPath && !PermissionCheckFunctions.checkWriteExternalStoragePermission(mActivity))
					return;

				mContext.startActivity(JadblackPhotoViewer.launchIntent(mContext,isHttpUrlPath ? filePath : "file://" + filePath));
			}
		});

		actionButtonGrantPermissions.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				askForPermissions();
			}
		});
		actionButtonGoToPermissionSettings.setOnClickListener(new OnClickListener(){
			public void onClick(View view){

				if (!PermissionsUtils.permissionGranted(mContext,mPermission))
					PermissionsUtils.openSettings(mActivity,editSettingRequestCode);
				else
					refreshControlForPermissions();

			}
		});

		problemLoadingImageTextView.setVisibility(View.GONE);

		refreshControlForPermissions();

		// Set Initially the control as NOT-DISPLAY mode.
		setIsDisplayMode(false);
	}


	private void askForPermissions() {

		PermissionsUtils.setPermissionWasAlreadyAskedOneTime(mContext,mPermission,true);

		//Setup permissions listener
		mPermissionListener = new PermissionListener() {
			@Override
			public void onPermissionGranted() {
				refreshControlForPermissions();
			}

			@Override
			public void onPermissionDenied(List<String> deniedPermissions){
				refreshControlForPermissions();
			}
		};
		PermissionsUtils.TedPermissionCheck(mContext,mPermission,mPermissionListener);

	}
	public void refreshControlForPermissions(){

		if(mContext == null) return;

		if (!PermissionsUtils.permissionGranted(mContext,mPermission)){

			boolean permissionWasAlreadyAskedOneTime = PermissionsUtils.permissionWasAlreadyAskedOneTime(mContext,mPermission);

			if (permissionWasAlreadyAskedOneTime){
				actionButtonGoToPermissionSettings.setVisibility(View.VISIBLE);
				actionButtonGrantPermissions.setVisibility(View.GONE);
			}else{
				actionButtonGoToPermissionSettings.setVisibility(View.GONE);
				actionButtonGrantPermissions.setVisibility(View.VISIBLE);
			}

			grantPermissionsContainer.setVisibility(View.VISIBLE);
			takeOrAttachPicsContainer.setVisibility(View.GONE);

			permissionSpecialInstructionsTextView.setVisibility(permissionWasAlreadyAskedOneTime ? View.VISIBLE : View.GONE);

		}else{

			actionButtonGoToPermissionSettings.setVisibility(View.GONE);
			actionButtonGrantPermissions.setVisibility(View.VISIBLE);
			grantPermissionsContainer.setVisibility(View.GONE);
			takeOrAttachPicsContainer.setVisibility(View.VISIBLE);

		}

	}
	@Override
	protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (visibility == View.VISIBLE) //onResume called
			refreshControlForPermissions();
	}
	public void setIsDisplayMode(boolean isDisplayMode){
		if(attachOrTakePhotoContainer== null)
			return;

		// Mark the Display Mode in what are we sending.
		this.isDisplayMode = isDisplayMode;

		// Refresh the control.
		loadPicture();
	}


	// ----------------------------------------------------------
	//	Set initial photo (if needed)
	// ----------------------------------------------------------
	public void setDefaultData(String imagePath, boolean isHttpUrlPath){
		Log.d(VIEW_LOG_TAG,"setDefaultData is called...");
		receivedCallback = true;
		fileName = "Imagen cargada";
		filePath = isHttpUrlPath ? "https://" + imagePath : imagePath;
		this.isHttpUrlPath = isHttpUrlPath;
		Log.d(tag,"CAMERA CONTROL::: INITIAL IMAGE ::: imagePath: " + imagePath + " - isHttpUrlPath:" +isHttpUrlPath );

		// If initially we have a local image, then we mark as an image has already been taken.
		if(!isHttpUrlPath) photoWasChanged = true;

		// Refresh the ImageView.
		loadPicture();
	}
	private void setVisibilityToProblemLoadingImage(boolean showError, String message){
		problemLoadingImageTextView.setVisibility(showError ? View.VISIBLE : View.GONE);
		problemLoadingImageTextView.setText(message);
		picturePreview.setVisibility(!showError ? View.VISIBLE : View.GONE);
		zoomButton.setVisibility(!showError ? View.VISIBLE : View.GONE);

	}

	
	public boolean handleRequest(int requestCode, int resultCode, Intent data){
		if(selectPictureRequestCode != requestCode && takePictureRequestCode != requestCode)
			return false;
			
		if(resultCode == Activity.RESULT_OK && selectPictureRequestCode == requestCode){
			receivedCallback = true;
			handleSelectedFile(data);
			isHttpUrlPath = false;
			photoWasChanged = true;
			loadPicture();			
		}
		
		if(resultCode == Activity.RESULT_OK && takePictureRequestCode == requestCode){
			receivedCallback = true;
			isHttpUrlPath = false;
			photoWasChanged = true;
			loadPicture();			
		}
		return true;
	}

	// ------------------------------------------
	// Handle the Attached photo...
	// ------------------------------------------
	private void handleSelectedFile(Intent data){
		Log.d(tag, "Handling selected file:" + data);


		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			Uri selectedImage = data.getData();
			String[] columns = {MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};

			Cursor cursor = getContext().getContentResolver().query(selectedImage, columns, null, null, null);
			if( cursor != null ){
				cursor.moveToFirst();
				//Name
				fileName = cursor.getString(cursor.getColumnIndex(columns[0]));
				Log.d(tag, "Name: " + fileName);
				//Path
				filePath = cursor.getString(cursor.getColumnIndex(columns[1]));
				Log.d(tag, "Path: " + filePath);

				cursor.close();
			}
		}else{
				File file =scopedStorage.handleImageRequest(data, (Activity) mContext);
				filePath = file.getPath();
				fileName = file.getName();

		}
	}

	private void erasePhoto(){
		filePath = "";
		fileName = "";
		loadPicture();
	}
	
	public void loadPicture(){
		if(receivedCallback == false){
			Log.d(tag, "Callback not received. Activity destroy maybe?");
			return;
		}

		// Reset the Error loading message.
        setVisibilityToProblemLoadingImage(false,"");

        // Began to preview the photo.
		Log.d(tag, "Previewing picture.");
		if( filePath.length() > 0 ){
			try{
                // -----------------------------
                // If is Http Path, then...
                // -----------------------------
                if(isHttpUrlPath){

                    //ADD NAME
                    pictureFileName.setText("Imagen en Servidor");
                    pictureFileSize.setText("");
					Log.d(tag, "loadPicture: Mostrando en Picasso path: '" + filePath + "'");

					// Setting Picasso Listener
					Picasso.Builder picassoBuilder = new Picasso.Builder(mContext);
					picassoBuilder.listener(
							new Picasso.Listener(){
								@Override
								public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
									Log.d(tag,"onImageLoadFailed");
									e.printStackTrace();
								}
							}
					);
                    picassoBuilder.build().load(filePath)
											.into(picturePreview, new Callback() {

												public void onSuccess() {
													setVisibilityToProblemLoadingImage(false,"");
												}

												public void onError() {
													setVisibilityToProblemLoadingImage(true,"Error al cargar foto.\nDebe estar conectado a la red para visualizar correctamente la imagen.");
												}
											});
                }else
                // -----------------------------
                // If its a local image...
                // -----------------------------
                {

                    //ADD NAME
                    pictureFileName.setText(fileName);
                    //Create Image File.
                    File localImageFile = new File(filePath);

                    // Get File size.
                    float fileSize = localImageFile.length() / 1024;
                    pictureFileSize.setText(fileSize + " KB");

					//ADD PREVIEW
					Log.d(VIEW_LOG_TAG, "Previewing filePath:" + filePath);
					picturePreview.setImageBitmap(
							decodeSampledBitmapFromFile(filePath,   // File Path
									200,        // Width
									200));      // Height
					setVisibilityToProblemLoadingImage(false, "");
                }
	        }catch(Exception e){
                resetImage();
				setVisibilityToProblemLoadingImage(true,"Error al cargar foto.\nIntente nuevamente.");
	        }
		}else
            resetImage();

		// ----------------------------------------------------------------------------------------------------
		// Depending if it isnt Display or not we show the Image or Attach/Take photo button in the screen
		// ----------------------------------------------------------------------------------------------------
		if(this.isDisplayMode){
			picturePreviewContainer.setVisibility(View.VISIBLE);
			attachOrTakePhotoContainer.setVisibility(View.GONE);
			erasePhotoButton.setVisibility(View.GONE);
		}else{
			if(!filePath.isEmpty()){
				attachOrTakePhotoContainer.setVisibility(View.GONE);
				picturePreviewContainer.setVisibility(View.VISIBLE);
			}else{
				picturePreviewContainer.setVisibility(View.GONE);
				attachOrTakePhotoContainer.setVisibility(View.VISIBLE);
			}
			erasePhotoButton.setVisibility(View.VISIBLE);
		}


	}

	private void resetImage(){
		// Empty the FilePath.
		fileName = "";
		filePath = "";

		// Set the
        picturePreview.setImageDrawable(
                ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.picture_preview,
                        null)
        );
        pictureFileName.setText("Nombre: Sin Foto");
        pictureFileSize.setText("Tamaño: 0 KB");

		// No zoom.
		zoomButton.setVisibility(View.GONE);
    }
	
	public Bundle getBundle(){
		Bundle data = new Bundle();
		data.putString("file_name", fileName);
		data.putString("file_path", filePath);
		data.putBoolean("received_callback", receivedCallback);
		data.putBoolean("photo_was_changed", photoWasChanged);
		return data;
	}
	
	public void updateSelf(Bundle stateData){
		if( stateData.containsKey("file_name") ) fileName = stateData.getString("file_name");
		if( stateData.containsKey("file_path") ) filePath = stateData.getString("file_path");
		if( stateData.containsKey("received_callback") ) receivedCallback = stateData.getBoolean("received_callback");
		if( stateData.containsKey("photo_was_changed") ) photoWasChanged = stateData.getBoolean("photo_was_changed");
		
		handleChange();
	}
	
	public void handleChange(){
		loadPicture();
	}

	public static Bitmap decodeSampledBitmapFromFile(String imagePath, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imagePath, options);
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
	//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	//Save State Methods
	public Parcelable onSaveInstanceState(){
		Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);
	    ss.stateData = getBundle();
	    return ss;
	}
	
	public void onRestoreInstanceState(Parcelable state){
		if(!(state instanceof SavedState)){ super.onRestoreInstanceState(state); return; }
	    SavedState ss = (SavedState) state;
	    super.onRestoreInstanceState(ss.getSuperState());
	    updateSelf(ss.stateData);	    
	}

	public static File createImageFile(Context context) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMddHmmss").format(new Date());
		String imageFileName = "IMG" + timeStamp;
		File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName,
				".jpg",
				storageDir);
		return image;
	}
	
	static class SavedState extends BaseSavedState{
		Bundle stateData;	
		SavedState(Parcelable superState){  super(superState);}		
		private SavedState(Parcel in){ super(in); stateData = in.readBundle(); }
	    public void writeToParcel(Parcel out, int flags){ super.writeToParcel(out, flags); out.writeBundle(stateData); }	    
	    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){
	    	public SavedState createFromParcel(Parcel in) { return new SavedState(in);}
	    	public SavedState[] newArray(int size) { return new SavedState[size]; }
	    };
	}
}
