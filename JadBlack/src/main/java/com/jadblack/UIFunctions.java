package com.jadblack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.widget.Toast;

public class UIFunctions {
	//CONFIRM DIALOG
	static boolean confirmChoice = false;	
	public static boolean confirmDialog(final String message, final Activity callerActivity, final Handler availableScreenHandler){
		confirmChoice = false;
		try{		
			availableScreenHandler.post( new Runnable(){
				public void run(){ 
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(callerActivity)
						.setCancelable(false)
						.setMessage(message)
						.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int id){
								confirmChoice = true;
								synchronized(callerActivity){
									callerActivity.notifyAll();
								}
								dialog.dismiss();
							}
						})
						.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int id){
								confirmChoice = false;
								synchronized(callerActivity){
									callerActivity.notifyAll();
								}
								dialog.dismiss();
							}
						});
					alertDialog.show();
				}});
		
			synchronized(callerActivity){
				try {
					//WAIT ONE MINUTE
					callerActivity.wait(60 * 1000);					
				}catch(InterruptedException e){								
				}
			}
		}catch(Exception e){
			
		}		
		return confirmChoice;
	}
	
	//MESSAGE
	public static void message(final Context context, Handler screenHandler, final String message){
		screenHandler.post( new Runnable(){ 
			public void run(){
				AlertDialog.Builder d = new AlertDialog.Builder(context);
				d.setCancelable(false);
				d.setMessage(message);
				d.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						dialog.cancel();
					}
				});
				d.show();
			}
		});				    
	}	
	//MESSAGE TOAST
	public static void message(final Context context, Handler screenHandler, final String message, boolean waitForUser){
		if(waitForUser){
			message(context, screenHandler, message);
		}
		else{
			screenHandler.post( new Runnable(){ 
				public void run(){
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, message, duration);
					toast.show();
				}
			});
		}
	}
	
	//getColor
	public static int getColor(String sColor){
		int color;
		try{
			color = Color.parseColor(sColor);
		}catch(Exception e){
			color = Color.BLACK;
		}
		return color;
	}
}
