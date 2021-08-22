package com.jadblack;

import java.util.Date;
import java.util.Set;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public final class JadBlackComboBox extends Spinner {
	private boolean enabled = true;
	private ArrayAdapter<JadBlackComboBoxItem> adapter;
	private JadBlackComboBoxItemCatalog masterHolder;	
	
	//Item Catalog Class
	public static class JadBlackComboBoxItemCatalog
	{
		public Vector<JadBlackComboBoxItem> items = new Vector<JadBlackComboBoxItem>();
		
		public void clearItems(){
			items = new Vector<JadBlackComboBoxItem>();
		}
				
		//Integer
		public void addItem(int value, String description){
			JadBlackComboBoxItem item = new JadBlackComboBoxItem(
				new Integer(value),
				description,
				JadBlackComboBoxItem.INTEGER_VALUE
			);
			items.add(item);
		}
				
		//String
		public void addItem(String value, String description){
			JadBlackComboBoxItem item = new JadBlackComboBoxItem(
				value,
				description,
				JadBlackComboBoxItem.STRING_VALUE
			);
			items.add(item);
		}
		//Date
		public void addItem(Date value, String description){
			JadBlackComboBoxItem item = new JadBlackComboBoxItem(
				value,
				description,
				JadBlackComboBoxItem.DATE_VALUE
			);
			items.add(item);
		}
		
		public Bundle getBundle(){
			Bundle data = new Bundle();
			String key;
			for( int i = 0; i < items.size(); i++){
				key = Integer.toString(i);
				data.putBundle(key, items.elementAt(i).getBundle());
			}
			return data;
		}
		
		public void setFromBundle(Bundle data){
			if(data == null) return;
			
			Set<String> keys = data.keySet();
			String key;
			JadBlackComboBoxItem item;
			for( int i = 0; i < keys.size(); i++){
				key = String.valueOf(i);
				if(data.containsKey(key)){
					item = new JadBlackComboBoxItem();
					item.setFromBundle(data.getBundle(key));
					items.add(item);
				}
			}
		}
	}
	
	//Item Class
	public static class JadBlackComboBoxItem
	{
		public static final int NO_TYPE_VALUE = 0;
		public static final int STRING_VALUE = 1;
		public static final int SHORT_VALUE = 2;
		public static final int INTEGER_VALUE = 3;		
		public static final int DOUBLE_VALUE = 4;
		public static final int DATE_VALUE = 5;
		
		public Object value;
		public String description;
		public int valueType;
		
		public JadBlackComboBoxItem(){
			value = null;
			description = "";
			valueType = NO_TYPE_VALUE;
		}
		
		public JadBlackComboBoxItem(Object v, String d, int t) {
			value = v;
			description = d;
			valueType = t;
		}
		
		public JadBlackComboBoxItem(JadBlackComboBoxItem o){
			value = o.value;
			description = o.description;
		}			

		public String toString(){
			return description;
		}
		
		public Bundle getBundle(){
			Bundle data = new Bundle();
			switch(valueType){
				case INTEGER_VALUE:
					data.putInt("value", ((Integer)value).intValue());
					break;
				case STRING_VALUE:
					data.putString("value", value.toString());
					break;
				case DATE_VALUE:
					data.putLong("value", ((Date)value).getTime());
					break;
			}
			data.putString("description", description);
			data.putInt("value_type", valueType);
			return data;
		}
		
		public void setFromBundle(Bundle data){
			if( data == null ) return;
			
			valueType = data.getInt("value_type");
			description = data.getString("description");
			
			switch(valueType){
				case INTEGER_VALUE:
					value = new Integer(data.getInt("value"));
					break;
				case STRING_VALUE:
					value = data.getString("value");
					break;
				case DATE_VALUE:
					value = new Date(data.getLong("value"));
					break;
			}
		}
	}
	
	public JadBlackComboBox(Context context){
		super(context);
		initialize();
	}
	
	public JadBlackComboBox(Context context, AttributeSet attrs){
		super(context, attrs);
		initialize();
	}
	
	public JadBlackComboBox(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		initialize();
	}
	
	private void initialize(){
		//Adapter
		adapter = new ArrayAdapter<JadBlackComboBoxItem>(getContext(), R.layout.jadblackcombobox_item_layout);
		adapter.setDropDownViewResource(R.layout.jadblackcombobox_item_layout);
		setAdapter(adapter);
		//Item Catalog
		masterHolder = new JadBlackComboBoxItemCatalog();
	}
	
	public void updateSelf(Bundle stateData){
		//Enabled
		this.enabled = isEnabled();
		if( stateData.containsKey("enabled") ) this.enabled = stateData.getBoolean("enabled");
		
		//Data
		if( stateData.containsKey("data") ){
			masterHolder = new JadBlackComboBoxItemCatalog();
			masterHolder.setFromBundle(stateData.getBundle("data"));
			load();
		}
		handleChange();
	}
	
	public void handleChange(){
		setEnabled(enabled);		
	}
	
	public Bundle getBundle(){
		Bundle data = new Bundle();
		//Enabled
		data.putBoolean("enabled", enabled);
		//Items
		Bundle items = new Bundle();
		String key;
		JadBlackComboBoxItem auxItem;
		for( int i = 0; i < adapter.getCount(); i++){
			key = Integer.toString(i);
			auxItem = adapter.getItem(i);
			items.putBundle(key, auxItem.getBundle());
		}
		data.putBundle("data", items);
		
		return data;
	}
	
	public void load(){
		adapter.clear();
		adapter.notifyDataSetChanged();
		
		for(int i = 0; i < masterHolder.items.size(); i++){
			adapter.add(masterHolder.items.elementAt(i));
		}	
		adapter.notifyDataSetChanged();
		masterHolder.clearItems();
	}
	
	//Get/Set Methods
	//Integer
	public int getintvalue(){
		int result = 0;
		try{
			JadBlackComboBoxItem o = (JadBlackComboBoxItem) getSelectedItem();
			if(o != null) result = ((Integer) o.value).intValue();			
		}catch(Exception e){}
		return result;			
	}
	public void setValue(int searchedValue){
		for(int i = 0; i < adapter.getCount(); i++){
			JadBlackComboBoxItem o = adapter.getItem(i);
			int value = ((Integer) o.value).intValue();
			if( value == searchedValue ){
				setSelection(i);
				break;
			}
		}
	}
		
	//String
	public String getstringvalue(){
		String result = "";
		try{
			JadBlackComboBoxItem o = (JadBlackComboBoxItem) getSelectedItem();
			if(o != null) result = (String) o.value;			
		}catch(Exception e){}
		return result;			
	}
	public void setValue(String searchedValue){
		for(int i = 0; i < adapter.getCount(); i++){
			JadBlackComboBoxItem o = adapter.getItem(i);
			String value = (String) o.value;
			if( value.equalsIgnoreCase(searchedValue) ){
				setSelection(i);
				break;
			}
		}
	}
	
	//DateTime
	public Date getdatevalue(){
		Date result = new Date(0);
		try{
			JadBlackComboBoxItem o = (JadBlackComboBoxItem) getSelectedItem();
			if(o != null) result = (Date) o.value;			
		}catch(Exception e){}		
		return result;			
	}
	public void setValue(Date searchedValue){
		for(int i = 0; i < adapter.getCount(); i++){
			JadBlackComboBoxItem o = adapter.getItem(i);
			Date value = (Date) o.value;
			if(Comparer.process(value, "=", searchedValue)){
				setSelection(i);
				break;
			}
		}
	}
	
	//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	//Saving and Loading State Methods
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
	
	static class SavedState extends BaseSavedState{
		Bundle stateData;
	
		SavedState(Parcelable superState){  super(superState);}
		
		private SavedState(Parcel in){
			super(in);
			stateData = in.readBundle();
		}
	    public void writeToParcel(Parcel out, int flags){
	      super.writeToParcel(out, flags);
	      out.writeBundle(stateData);
	    }
	    
	    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){
	    	public SavedState createFromParcel(Parcel in) { return new SavedState(in);}
	    	public SavedState[] newArray(int size) { return new SavedState[size]; }
	    };
	}
	  
	/*
	//SHORT
	public short getshortvalue(){
		short result = 0;
		try{
			JadBlackComboBoxItem o = (JadBlackComboBoxItem) getSelectedItem();
			//NOTHING SELECTED
			if(o == null) return 0;		
			//RETURN SELECTED VALUE
			result = ((Short)o.value).shortValue();			
		}catch(Exception e){
			
		}
		return result;			
	}
	//INT
	public int getintvalue(){
		int result = 0;
		try{
			JadBlackComboBoxItem o = (JadBlackComboBoxItem) getSelectedItem();
			//NOTHING SELECTED
			if(o == null) return 0;		
			//RETURN SELECTED VALUE
			result = ((Integer)o.value).intValue();			
		}catch(Exception e){
			
		}
		return result;			
	}
	//FLOAT
	public float getfloatvalue(){
		float result = 0;
		try{
			JadBlackComboBoxItem o = (JadBlackComboBoxItem) getSelectedItem();
			//NOTHING SELECTED
			if(o == null) return 0;		
			//RETURN SELECTED VALUE
			result = ((Float)o.value).floatValue();			
		}catch(Exception e){
			
		}
		return result;			
	}
	//DOUBLE
	public double getdoublevalue(){
		double result = 0;
		try{
			JadBlackComboBoxItem o = (JadBlackComboBoxItem) getSelectedItem();
			//NOTHING SELECTED
			if(o == null) return 0;		
			//RETURN SELECTED VALUE
			result = ((Double)o.value).doubleValue();			
		}catch(Exception e){
			
		}
		return result;			
	}
	
	
	//SHORT
	public void setValue(short searchedValue){
		for(int i = 0; i < adapter.getCount(); i++){
			JadBlackComboBoxItem o = adapter.getItem(i);
			short value = ((Short) o.value).shortValue();
			if( value == searchedValue ){
				setSelection(i);
				break;
			}
		}
	}
	//INT
	public void setValue(int searchedValue){
		for(int i = 0; i < adapter.getCount(); i++){
			JadBlackComboBoxItem o = adapter.getItem(i);
			int value = ((Integer) o.value).intValue();
			if( value == searchedValue ){
				setSelection(i);
				break;
			}
		}
	}
	//FLOAT
	public void setValue(float searchedValue){
		for(int i = 0; i < adapter.getCount(); i++){
			JadBlackComboBoxItem o = adapter.getItem(i);
			float value = ((Float) o.value).floatValue();
			if( value == searchedValue ){
				setSelection(i);
				break;
			}
		}
	}
	//DOUBLE
	public void setValue(double searchedValue){
		for(int i = 0; i < adapter.getCount(); i++){
			JadBlackComboBoxItem o = adapter.getItem(i);
			double value = ((Double) o.value).doubleValue();
			if( value == searchedValue ){
				setSelection(i);
				break;
			}
		}
	}
	*/	
}
