
package com.jadblack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Character.UnicodeBlock;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

@SuppressWarnings("unused")
public class JadBlackCSVReader {
	public final String tag = "JadBlackCSVReader";
	
	private String filePath;
	
	private InputStream fis;
	private InputStreamReader isr;
	private BufferedReader br;
	
	private boolean fileReadyToRead = false;
	
	private final String COMMA = ",";
	private final String SEMICOLON = ";";
	private final String COLON = ":";
	private final String PIPE = "\\|";
	
	private String fieldDelimeter = PIPE;
	private String[] fieldValues;
	private int currentPosition = 0;
	private int maxPosition;
		
	public JadBlackCSVReader(){}
	
	public boolean open(String f){
		filePath = f;
		
		//VERIFY FILE
		File file = new File(filePath);
		if( file == null || file.exists() == false ){
			Log.e(tag, "File does not exist");
			return false;
		}
		
		//OPEN FILE
		try{
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			//Log.d("ENCONDING", "AHHHH");
		    br = new BufferedReader(isr);
		    
		    fileReadyToRead = true;
		    Log.i(tag, "File ready");
		    return true;
		}catch(Exception e){
			Log.e(tag, "Error while Opening: " + e);
			return false;
		}
	}
	
	public boolean read(){
		if( fileReadyToRead == false ) return false;
		
		String line;
		try{
			line = br.readLine();
			if( line == null )
				return false;			
			
			//SPLIT LINE ACCORDING TO FIELD DELIMITER
			fieldValues = line.split(fieldDelimeter);
			currentPosition = 0;
			maxPosition = fieldValues.length;
			
			return true;
		}catch (Exception e) {
			Log.e(tag, "Line read error: " + e);
			return false;
		}
	}
	
	public void close(){
		try{
			if(br != null) br.close();
			if(isr != null) isr.close();
			if(fis != null) fis.close();
		}catch(Exception e){			
		}
	}
	
	//RETURN VALUES METHODS
	public String getstring(){
		String result = "";
		if( currentPosition < maxPosition){
			try{
				result = fieldValues[currentPosition];
			}catch(Exception e){
				result = "";
			}
		}
		currentPosition += 1;
		return result;
	}
	
	public Date getdate(){
		Date result = new Date(0);
		if( currentPosition < maxPosition){
			try{
				String auxResult = fieldValues[currentPosition];
				result = DateFunctions.stringToDateTime(auxResult);
			}catch(Exception e){
				result = new Date(0);
			}
		}
		currentPosition += 1;
		return result;
	}
	
	public short getshort(){
		short result = 0;
		if( currentPosition < maxPosition){
			try{
				String auxResult = fieldValues[currentPosition];
				result = NumericFunctions.toShort(auxResult);
			}catch(Exception e){
				result = 0;
			}
		}
		currentPosition += 1;
		return result;
	}
	
	public int getinteger(){
		int result = 0;
		if( currentPosition < maxPosition){
			try{
				String auxResult = fieldValues[currentPosition];
				result = NumericFunctions.toInteger(auxResult);
			}catch(Exception e){
				result = 0;
			}
		}
		currentPosition += 1;
		return result;
	}

	public long getlong(){
		long result = 0;
		if( currentPosition < maxPosition){
			try{
				String auxResult = fieldValues[currentPosition];
				result = NumericFunctions.toLong(auxResult);
			}catch(Exception e){
				result = 0;
			}
		}
		currentPosition += 1;
		return result;
	}
	
	public float getfloat(){
		float result = 0;
		if( currentPosition < maxPosition){
			try{
				String auxResult = fieldValues[currentPosition];
				result = NumericFunctions.toFloat(auxResult);
			}catch(Exception e){
				result = 0;
			}
		}
		currentPosition += 1;
		return result;
	}
	
	public double getdouble(){
		double result = 0;
		if( currentPosition < maxPosition){
			try{
				String auxResult = fieldValues[currentPosition];
				result = NumericFunctions.toDouble(auxResult);
			}catch(Exception e){
				result = 0;
			}
		}
		currentPosition += 1;
		return result;
	}
	
	public boolean getboolean(){
		boolean result = false;
		if( currentPosition < maxPosition){
			try{
				String auxResult = fieldValues[currentPosition];
				if(NumericFunctions.toInteger(auxResult) == 1 || Boolean.parseBoolean(auxResult)){
					result = true;
				}
			}catch(Exception e){
				result = false;
			}
		}
		currentPosition += 1;
		return result;
	}
}
