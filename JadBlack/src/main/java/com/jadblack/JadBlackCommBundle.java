
package com.jadblack;

import android.os.Bundle;

public final class JadBlackCommBundle {
	public String handler;
	public Bundle data;
	
	public static final class Builder{
		public String bHandler;
		public Bundle bData = new Bundle();
		
		public Builder setHandler(String handler){
			bHandler = handler;
			return this;
		}

		public Builder addData(String type, CharSequence value){
			bData.putCharSequence(type, value);
			return this;
		}

		public Builder addData(String type, int value){
			bData.putInt(type, value);
			return this;			
		}
		
		public Builder addData(String type, double value){
			bData.putDouble(type, value);
			return this;			
		}
		
		public Builder addData(String type, String value){
			bData.putString(type, value);
			return this;
		}
		
		public Builder addData(String type, boolean value){
			bData.putBoolean(type, value);
			return this;
		}
		
		public Builder addData(String type, Bundle value){
			bData.putBundle(type, value);
			return this;
		}
		
		public JadBlackCommBundle build(String handler){
			setHandler(handler);
			
			JadBlackCommBundle o = new JadBlackCommBundle();
			o.handler = bHandler;
			o.data = bData;
			return o;
		}
	}
}
