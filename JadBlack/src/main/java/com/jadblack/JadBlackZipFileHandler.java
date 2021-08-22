
package com.jadblack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.util.Log;

@SuppressWarnings("unchecked")
public class JadBlackZipFileHandler {

	public final String LOG_TAG = JadBlackZipFileHandler.class.getSimpleName();
	public String filePath;
	public String outputDirectoryPath;
	
	public boolean outputDirectoryOk = false;
	
	public ZipFile zipHandler;
	
	public JadBlackZipFileHandler(){
		outputDirectoryPath = "";
		filePath = "";		
	}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;

		outputDirectoryPath = this.filePath;
		outputDirectoryPath = outputDirectoryPath.replaceAll(".zip","");
		Log.d(LOG_TAG, "File Path: " + filePath);
		Log.d(LOG_TAG, "Output Directory: " + outputDirectoryPath);
	}
	
	public void open(String filePath){
		setFilePath(filePath);
		verifyOutputDirectory();
	}
	
	public void verifyOutputDirectory(){
		Log.d(LOG_TAG, "Checking directory " + outputDirectoryPath);

		File file = new File(outputDirectoryPath);
		if( file != null && file.exists() == false ){
			outputDirectoryOk = file.mkdirs();
			Log.d(LOG_TAG, "Directory created");
		}else{
			outputDirectoryOk = true;
			Log.d(LOG_TAG, "Directory already available");
		}
	}
	
	public boolean isZipReady(){
		ErrorHandler.getInstance().clean();
		
		File file = new File(outputDirectoryPath);
		if( file != null && file.exists() == true ){
			try {
				zipHandler = new ZipFile(filePath);
				return true;
			}catch(Exception e){
				ErrorHandler.getInstance().setErrorMessage(e.toString());
				zipHandler = null;
				return false;
			}			
		}else{
			ErrorHandler.getInstance().setErrorMessage("Could not create Directory for files.");
			return false;
		}
	}
	
	public void unzip(){
		String innerFilePath;
		int BUFFER = 2048;
		Enumeration<ZipEntry> filesInZip = (Enumeration<ZipEntry>) zipHandler.entries();
		while( filesInZip.hasMoreElements() ){
			ZipEntry fileInZip = filesInZip.nextElement();
			
			innerFilePath = outputDirectoryPath + File.separator + fileInZip.getName();
			File f = new File(innerFilePath);
			if( f != null && f.exists() == true ){
				f.delete();
				Log.d(LOG_TAG, "Deleting previous file " + fileInZip.getName());
			}
				
			try{
				Log.d(LOG_TAG, "Unzipping " + fileInZip.getName());
				
				InputStream is = zipHandler.getInputStream(fileInZip);
				FileOutputStream os = new FileOutputStream(innerFilePath);
				BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER);	            	           
	            
	            int count = 0;
	            byte[] data = new byte[BUFFER];
	            while ((count = is.read(data, 0, BUFFER)) != -1){
	                bos.write(data, 0, count);
	            }	
	            bos.flush();
	            bos.close();
	            os.close();
	            is.close();	            	           
	            
			}catch(Exception e){
				e.printStackTrace();
				Log.d(LOG_TAG, "Error " + e);
			}
		}
		
		try{
			zipHandler.close();
		}catch(Exception e){}
		
		Log.d(LOG_TAG, "Unzipping complete");
	}
}
