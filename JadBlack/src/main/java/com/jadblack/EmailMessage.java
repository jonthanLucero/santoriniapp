package com.jadblack;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class EmailMessage {
	private String _toAddress;
    private String _messageSubject;
    private String _messageBody;
    private String _attachmentPath;
    private String _mimeType;
    
    public EmailMessage(){
        _toAddress = "";
        _messageSubject = "";
        _messageBody = "";
        _attachmentPath = "";
        _mimeType = "text/plain";
    }
    
    public void setToAddress(String toAddress){
        _toAddress = toAddress;
    }
    
    public void setSubject(String messageSubject){
        this._messageSubject = messageSubject;
    }
    
    public void setBody(String messageBody){
        this._messageBody = messageBody;
    }
    
    public void setAttachment(String filePath){
    	_attachmentPath = filePath;    	  
    }
    
    public void setMimeType(String mimeType){
    	_mimeType = mimeType;
    }
    
    public Bundle getBundle(){
    	Bundle data = new Bundle();
    	data.putString("to", _toAddress);
    	data.putString("subject", _messageSubject);
    	data.putString("text", _messageBody);
    	data.putString("mime_type", _mimeType);
    	data.putString("attachment_path", _attachmentPath);    	
    	return data;
    }
    
    public static void openClient(Activity mActivity, Bundle emailData){
    	//Get Data from Bundle
    	String toAddress = emailData.getString("to");
    	String subject = emailData.getString("subject");
    	String text = emailData.getString("text");
    	String attachmentPath = emailData.getString("attachment_path");
    	String mimeType = emailData.getString("mime_type");
    	
    	//Handle Email Intent
    	Intent intent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
    	intent.setType(mimeType);    	
    	//Attach Address
    	String[] to = new String[]{ toAddress, ""};
    	intent.putExtra(android.content.Intent.EXTRA_EMAIL, to);
    	//Attach Subject
    	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
    	//Attach Text
    	intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
    	//Attachments
    	if( attachmentPath.length() > 0){
    		ArrayList<Uri> attachments = new ArrayList<Uri>();
    		try{
        		File f = new File(attachmentPath);
        		if( f.length() > 0 ){
        			Uri u = Uri.fromFile(f);
        			attachments.add(u);        			
        		}
        	}catch(Exception e){}
    		
        	if( attachments.size() > 0){
        		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
        	}    
    	}
    	
    	//Launch Intent Chooser
    	mActivity.startActivity(Intent.createChooser(intent, "Enviar Correo..."));
    }
}
