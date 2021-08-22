package com.jadblack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

import android.util.Log;

import javax.net.ssl.HttpsURLConnection;

public class FileFunctions {
	public static final String tag = "FileFunctions";
	
	public static boolean filedelete(String filePath){
		File file = new File(filePath);
		if( file != null && file.exists() )
			return file.delete();
		else
			return false;			
	}
	
	public static boolean fileexists(String filePath){
		File file = new File(filePath);
		if( file!= null && file.exists() ){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean copyFile(String src, String dst){
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try
	    {
			inChannel = new FileInputStream(src).getChannel();
		    outChannel = new FileOutputStream(dst).getChannel();
	        inChannel.transferTo(0, inChannel.size(), outChannel);
	        Log.d(tag, "Copy File OK");	        
	        return true;
	    }catch(Exception e){
	    	if (inChannel != null) try{ inChannel.close(); }catch(IOException e1){}
	        if (outChannel != null) try{ outChannel.close(); }catch(IOException e1){}
	        Log.d(tag, "Copy File Error: " + e);
	    }	 
		return false;
	}
	
	public static boolean downloadFile(String fileURL, String filePath){
		ErrorHandler.getInstance().clean();
		boolean isInProduction = !(fileURL.contains("192.168.1.214")  || fileURL.contains("192.168.1.164"));

		HttpsURLConnection connSecure = null;
		HttpURLConnection conn = null;

		try{

			// Avoid for Local using HTTPs.
			if(isInProduction) {

				// Set file Url.
				fileURL = "https://" + fileURL;

				//Open HTTP connection
				URL url = new URL(fileURL);
				Log.d("DESCARGA", "Descargando desde " + url.toString());
				connSecure = (HttpsURLConnection) url.openConnection();
				connSecure.setRequestMethod("GET");
				//conn.setDoOutput(true);
				connSecure.connect();

				Log.d(tag, "Content Length for Download: " + connSecure.getContentLength());

				//Open new file for download
				File file = new File(filePath);
				FileOutputStream fos = new FileOutputStream(file);
				InputStream is = connSecure.getInputStream();
				byte[] buffer = new byte[1024];
				int bufferLength = 0;
				while ((bufferLength = is.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, bufferLength);
					//Log.d(tag, "Downloaded: " + bufferLength);
				}
				fos.close();
				is.close();
				connSecure.disconnect();

				Log.d(tag, "File Downloaded.");

			}else{

				// Set file Url.
				fileURL = "http://" + fileURL;

				//Open HTTP connection
				URL url = new URL(fileURL);
				Log.d("DESCARGA", "Descargando desde " + url.toString());
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				//conn.setDoOutput(true);
				conn.connect();

				Log.d(tag, "Content Length for Download: " + conn.getContentLength());

				//Open new file for download
				File file = new File(filePath);
				FileOutputStream fos = new FileOutputStream(file);
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int bufferLength = 0;
				while ((bufferLength = is.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, bufferLength);
					//Log.d(tag, "Downloaded: " + bufferLength);
				}
				fos.close();
				is.close();
				conn.disconnect();

				Log.d(tag, "File Downloaded.");

			}

			return true;
		}catch(Exception e){
			int responseCode;
			try{
				if(isInProduction)
					responseCode = ((HttpsURLConnection) connSecure).getResponseCode();
				else
					responseCode = ((HttpURLConnection) connSecure).getResponseCode();
			}catch(Exception ex){
				responseCode = -1;
			}
			Log.d(tag, "Downloaded failed: " + e + " (" + responseCode + ")");
			
			ErrorHandler.getInstance().setErrorMessage(e.toString() + " (Response: " + responseCode + ")");
			return false;
		}
	}
}
