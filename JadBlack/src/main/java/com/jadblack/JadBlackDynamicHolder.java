package com.jadblack;

import java.util.Date;
import java.util.Set;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JadBlackDynamicHolder extends LinearLayout{
	public final static String tag = "JadBlackDynamicHolder";
	
	//Margins
	public static final int LEFT_MARGIN = 2;
	public static final int TOP_MARGIN = 2;
	public static final int RIGHT_MARGIN = 2;
	public static final int BOTTOM_MARGIN = 2;
	
	//Input Types
	public static final int LABEL = 1;
	public static final int EDITBOX = 2;
	public static final int COMBOBOX = 3;
	public static final int DATEBOX = 4;
	public static final int BUTTON = 5;
	
	//Item Catalog Variable
	public JadBlackDynamicHolderItemCatalog children;
	
	//Date Field Callbacks
	public static View.OnClickListener dateListener = null; 
	
	//Supers
	public JadBlackDynamicHolder(Context context){ super(context); initialize(); }
	public JadBlackDynamicHolder(Context context, AttributeSet attrs){ super(context, attrs); initialize(); }
	
	public void initialize(){
		children = new JadBlackDynamicHolderItemCatalog();
	}
	
	public void setDateListener(View.OnClickListener listener){
		dateListener = listener;
	}
	
	public void setFromBundle(Bundle data){
		if(data == null) return;
		children = new JadBlackDynamicHolderItemCatalog();
		children.setFromBundle(data);	
	}
	
	public void load(){
		removeAllViews();
		
		Log.d(tag, "Children to deploy: " + children.items.size());
		for(int i = 0; i < children.items.size(); i++){
			deployChild(children.items.elementAt(i));
			
		}
	}
	
	public void deployChild(JadBlackDynamicHolderItem item){
		Object[] o;
		switch(item.fieldType){
			case LABEL:
				o = getLabelControl(item, getContext());
				addView((TextView)o[0], (LayoutParams)o[1]);
				break;
				
			case EDITBOX:
				o = getEditBoxControl(item, getContext());
				addView((LinearLayout)o[0], (LayoutParams)o[1]);
				break;
				
			case COMBOBOX:
				o = getComboBoxControl(item, getContext());
				addView((LinearLayout)o[0], (LayoutParams)o[1]);
				break;
				
			case DATEBOX:
				o = getDateFieldControl(item, getContext());
				addView((LinearLayout)o[0], (LayoutParams)o[1]);
				break;
				
			case BUTTON:
				o = getButtonControl(item, getContext());
				addView((JadBlackButton)o[0]);
				break;
		}
	}
	
	//Gather Values
	public void gatherValues(){
		JadBlackDynamicHolderItem child;
		for(int i = 0; i < children.items.size(); i++){
			child = children.items.elementAt(i);
			
			switch(child.fieldType){
				
				case EDITBOX:
					EditText et = (EditText) findViewById(child.fieldId);
					if(et != null){
						String value = et.getText().toString();
						children.items.elementAt(i).inputValue = value;
						switch(child.valueType){
							case JadBlackDynamicHolderItem.STRING:
								children.items.elementAt(i).value = value;
								break;
							case JadBlackDynamicHolderItem.SHORT: 
								children.items.elementAt(i).value = new Double(com.jadblack.NumericFunctions.toShort(value));
								break;
							case JadBlackDynamicHolderItem.INT: 
								children.items.elementAt(i).value = new Double(com.jadblack.NumericFunctions.toInteger(value));
								break;
							case JadBlackDynamicHolderItem.LONG:
								children.items.elementAt(i).value = new Double(com.jadblack.NumericFunctions.toLong(value));
								break;
							case JadBlackDynamicHolderItem.FLOAT:
								children.items.elementAt(i).value = new Double(com.jadblack.NumericFunctions.toFloat(value));
								break;
							case JadBlackDynamicHolderItem.DOUBLE:
								children.items.elementAt(i).value = new Double(com.jadblack.NumericFunctions.toDouble(value));
								break;
						}
					}
					break;
					
				case COMBOBOX:
					JadBlackComboBox cb = (JadBlackComboBox) findViewById(child.fieldId);
					if(cb != null){
						switch(child.valueType){
							case JadBlackDynamicHolderItem.STRING:
								children.items.elementAt(i).inputValue = cb.getstringvalue();
								children.items.elementAt(i).value = cb.getstringvalue();
								break;
							case JadBlackDynamicHolderItem.INT:
								children.items.elementAt(i).inputValue = StringFunctions.toString(cb.getintvalue());
								children.items.elementAt(i).value = new Double(cb.getintvalue());
								break;
							case JadBlackDynamicHolderItem.DATE:
								children.items.elementAt(i).inputValue = StringFunctions.toString(cb.getdatevalue().getTime());
								children.items.elementAt(i).value = cb.getdatevalue();
								break;
						}
					}
					break;
				
				case DATEBOX:
					JadBlackDateField df = (JadBlackDateField) findViewById(child.fieldId);
					if(df != null){
						children.items.elementAt(i).inputValue = StringFunctions.toString(df.getDate().getTime());
						children.items.elementAt(i).value = df.getDate();					
					}
					break;
			}
		}
	}
		
	public Bundle getBundle(){
		gatherValues();
		Bundle data = new Bundle();
		data.putBundle("data", children.getBundle());
		return data;
	}
	
	public void updateSelf(Bundle stateData){
		if(stateData == null) return;
		if( stateData.containsKey("data") ){
			setFromBundle(stateData.getBundle("data"));
			load();
		}
	}
	
	//Get Controls Methods
	public static void addMandatoryImageView(LinearLayout wrapper){
		ImageView iv = new ImageView(wrapper.getContext());
		iv.setImageResource(com.jadblack.R.drawable.star);
		iv.setScaleType(ScaleType.FIT_START);
		LayoutParams ivLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ivLayout.weight = 0;
		ivLayout.setMargins(0, 5, 0, 0);

		wrapper.addView(iv, ivLayout);
	}
		
	public static Object[] getLabelControl(JadBlackDynamicHolderItem item, Context mContext){
		//Label
		TextView labelField = new TextView(mContext);
		labelField.setId(item.fieldId);
		labelField.setText(item.label);
		labelField.setTextColor(Color.BLACK);
		labelField.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		//Bold
		if(item.boldLabel) labelField.setTypeface(Typeface.DEFAULT, Typeface.BOLD);				
		//Align
		labelField.setGravity(item.align);
		//Layout
		LayoutParams labelLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		labelLayout.setMargins(LEFT_MARGIN, TOP_MARGIN * 2, RIGHT_MARGIN, BOTTOM_MARGIN * 2);
		
		//Return Object
		Object[] o = new Object[2];
		o[0] = labelField;
		o[1] = labelLayout;		
		return o;
	}
	
	public static Object[] getEditBoxControl(JadBlackDynamicHolderItem item, Context mContext){
		//Field deploy
		final EditText field = new EditText(mContext);
		field.setId(item.fieldId);
		
		//Input Type
		switch(item.valueType){
			case JadBlackDynamicHolderItem.SHORT:	
			case JadBlackDynamicHolderItem.INT:				
			case JadBlackDynamicHolderItem.LONG:
				field.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
				field.setSelectAllOnFocus(true);
				field.setGravity(Gravity.CENTER);
				break;
				
			case JadBlackDynamicHolderItem.FLOAT:	
			case JadBlackDynamicHolderItem.DOUBLE:
				field.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				field.setSelectAllOnFocus(true);
				field.setGravity(Gravity.CENTER);
				break;
		}
		//Length
		if(item.valueLength > 0){
			InputFilter[] filters = new InputFilter[1];
			filters[0] = new InputFilter.LengthFilter(item.valueLength);
			field.setFilters(filters);
		}
		//Read Only
		if( item.readOnly ) field.setEnabled(false);
		//Set Value
		field.setText(item.getStringRepresentation());
		//Layout
		LinearLayout.LayoutParams fieldLayout = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		fieldLayout.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		
		//Label deploy
		TextView labelField = new TextView(mContext);
		labelField.setText(item.label);
		labelField.setTextColor(Color.BLACK);
		labelField.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		//Bold
		if(item.boldLabel) labelField.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		//Layout
		LayoutParams labelLayout = new LayoutParams(0, LayoutParams.WRAP_CONTENT);		
		labelLayout.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;		
		//Focus request
		labelField.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {		
				field.requestFocus();
			}
		});
		
		//Correct Weight according to Value Type
		if(item.valueType == JadBlackDynamicHolderItem.STRING){
			labelLayout.weight = 1;
			fieldLayout.weight = 2;
		}else{
			labelLayout.weight = 2;
			fieldLayout.weight = 1;
		}
		
		//Wrapper
		LinearLayout wrapper = new LinearLayout(mContext);
		wrapper.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams wrapperLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		wrapperLayout.gravity = Gravity.CENTER_VERTICAL;
		wrapperLayout.setMargins(LEFT_MARGIN, TOP_MARGIN, RIGHT_MARGIN, BOTTOM_MARGIN);
		wrapper.addView(labelField, labelLayout);
		wrapper.addView(field, fieldLayout);		
		
		//Mandatory
		if(item.mandatory) addMandatoryImageView(wrapper);
		
		//Return Object
		Object[] o = new Object[2];
		o[0] = wrapper;
		o[1] = wrapperLayout;
		return o;
	}
	
	public static Object[] getComboBoxControl(JadBlackDynamicHolderItem item, Context mContext){
		//Label
		TextView labelField = new TextView(mContext);
		labelField.setText(item.label);
		labelField.setTextColor(Color.BLACK);
		labelField.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		//Bold
		if(item.boldLabel) labelField.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		//Layout
		LayoutParams labelLayout = new LayoutParams(0, LayoutParams.WRAP_CONTENT);		
		
		//Combo Box
		JadBlackComboBox field = new JadBlackComboBox(mContext);
		field.setId(item.fieldId);
		//Choices
		Bundle stateData = new Bundle();
		stateData.putBundle("data", item.choices.getBundle());
		field.updateSelf(stateData);
		//Value
		field.setValue(item.getStringValue());
		//Read Only
		if(item.readOnly) field.setEnabled(false);
		//Layout
		LayoutParams fieldLayout = new LayoutParams(0, LayoutParams.WRAP_CONTENT);		
		
		//Set Weight
		labelLayout.weight = 1;
		fieldLayout.weight = 1;
		
		//Wrapper
		LinearLayout wrapper = new LinearLayout(mContext);
		wrapper.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams wrapperLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		wrapperLayout.setMargins(LEFT_MARGIN, TOP_MARGIN, RIGHT_MARGIN, BOTTOM_MARGIN);
		wrapper.addView(labelField, labelLayout);
		wrapper.addView(field, fieldLayout);
		
		//Mandatory
		if(item.mandatory) addMandatoryImageView(wrapper);
				
		//Return Object
		Object[] o = new Object[2];
		o[0] = wrapper;
		o[1] = wrapperLayout;
		return o;
	}
	
	public static Object[] getDateFieldControl(JadBlackDynamicHolderItem item, Context mContext){
		//Label
		TextView labelField = new TextView(mContext);
		labelField.setText(item.label);
		labelField.setTextColor(Color.BLACK);
		labelField.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		//Bold
		if(item.boldLabel) labelField.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		//Layout
		LayoutParams labelLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		labelLayout.setMargins(5, 0, 5, 0);
		labelLayout.weight = 0;
		
		//Date Box
		JadBlackDateField field = new JadBlackDateField(mContext);
		field.setId(item.fieldId);
		//Value
		try{
			field.setDate(item.getDateValue());
		}catch(Exception e){
			field.setDate(new Date());
		}
		//Read Only
		if(item.readOnly) field.setEnabled(false);
		//Listener
		if(dateListener != null) field.setOnClickListener(dateListener);
		//Layout
		LayoutParams fieldLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);			
		fieldLayout.weight = 10;
		fieldLayout.setMargins(5, 0, 5, 0);	
		
		//Wrapper
		LinearLayout wrapper = new LinearLayout(mContext);
		wrapper.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams wrapperLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		wrapper.addView(labelField, labelLayout);
		wrapper.addView(field, fieldLayout);
		
		//Mandatory
		if(item.mandatory) addMandatoryImageView(wrapper);
		
		//Return Object
		Object[] o = new Object[2];
		o[0] = wrapper;
		o[1] = wrapperLayout;
		return o;
	}
	
	public static Object[] getButtonControl(JadBlackDynamicHolderItem item, Context mContext){
		//Button
		JadBlackButton field = new JadBlackButton(mContext);
		field.setText(item.label);
		field.setId(item.fieldId);
		
		//Return Object
		Object[] o= new Object[1];
		o[0] = field;
		return o;
	}
	
	//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	//Item Catalog Class
	public static class JadBlackDynamicHolderItemCatalog{
		public Vector<JadBlackDynamicHolderItem> items;
		public JadBlackDynamicHolderItem item;
		public int currentId = 30000;
		
		public JadBlackDynamicHolderItemCatalog(){
			items = new Vector<JadBlackDynamicHolderItem>();
		}
		
		public void clearItems(){
			items = new Vector<JadBlackDynamicHolderItem>();
		}
		
		
		public JadBlackDynamicHolderItem getCurrentItem(){
			return item;
		}
		
		public void createItem(){
			item = new JadBlackDynamicHolderItem();
		}
		
		public void addItem(){
			item.fieldId = getNewFieldId();
			items.add(item);
		}
		
		public int getNewFieldId(){
			currentId += 1;
			return currentId;
		}
		
		public int getSize(){
			return items.size();
		}
		
		public String getId(int index){
			String id = "";
			try{
				id = items.elementAt(index).id;
			}catch(Exception e){}
			
			return id;
		}
		
		public void setFromBundle(Bundle data){
			if(data == null) return;
			
			Set<String> keys = data.keySet();
			String key;
			for( int i = 0; i < keys.size(); i++){
				key = String.valueOf(i);
				if(data.containsKey(key)){
					createItem();
					getCurrentItem().setFromBundle(data.getBundle(key));
					addItem();
				}
			}
		}
		
		public Bundle getBundle(){
			Bundle data = new Bundle();
			for(int i = 0; i < items.size(); i++){
				data.putBundle(String.valueOf(i), items.elementAt(i).getBundle());
			}
			return data;
		}
		
		//Is Field Empty Method
		public boolean isFieldEmpty(String id){
			JadBlackDynamicHolderItem item = _getItemById(id);
			if(item != null){
				return item.inputValue.length() == 0;
			}
			return false;
		}
		
		//Getters
		public String getStringValue(String id){
			JadBlackDynamicHolderItem item = _getItemById(id);
			if(item != null){
				return item.getStringValue();
			}
			return "";
		}
		public int getIntValue(String id){
			JadBlackDynamicHolderItem item = _getItemById(id);
			if(item != null){
				return item.getIntValue();
			}
			return 0;
		}
		public float getFloatValue(String id){
			JadBlackDynamicHolderItem item = _getItemById(id);
			if(item != null){
				return item.getFloatValue();
			}
			return 0;
		}
		public double getDoubleValue(String id){
			JadBlackDynamicHolderItem item = _getItemById(id);
			if(item != null){
				return item.getDoubleValue();
			}
			return 0;
		}
		public Date getDateValue(String id){
			JadBlackDynamicHolderItem item = _getItemById(id);
			if(item != null){
				return item.getDateValue();
			}
			return new Date(0);
		}
		
		private JadBlackDynamicHolderItem _getItemById(String id){
			JadBlackDynamicHolderItem aux = new JadBlackDynamicHolderItem();
			for(int i = 0; i < items.size(); i++){
				aux = items.elementAt(i);
				if(id.equalsIgnoreCase(aux.id))
					return aux;
			}
			return null;
		}
	}
	
	//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	//Item Class
	public static class JadBlackDynamicHolderItem{
		public static final int STRING = 1;
		public static final int SHORT = 2;
		public static final int INT = 3;
		public static final int LONG = 4;
		public static final int FLOAT = 5;
		public static final int DOUBLE = 6;
		public static final int DATE = 7;
		
		public String id;
		public String label;
		public boolean boldLabel;
		public String inputValue;
		public Object value;
		public int valueType;
		public int valueLength;
		public int fieldType;
		public int fieldId;
		public boolean readOnly;
		public boolean mandatory;
		public int align;
		public JadBlackComboBox.JadBlackComboBoxItemCatalog choices;
		
		public JadBlackDynamicHolderItem(){
			id = "";
			label = "";
			boldLabel = false;
			inputValue = "";
			value = null;
			valueType = 0;
			valueLength = 0;
			fieldType = 0;
			fieldId = 0;
			readOnly = false;
			mandatory = false;
			align = Gravity.LEFT;
			choices = new JadBlackComboBox.JadBlackComboBoxItemCatalog();
		}
				
		public Bundle getBundle(){
			Bundle data = new Bundle();
			data.putString("id", id);
			data.putString("label", label);
			data.putBoolean("bold_label", boldLabel);
			data.putString("input_value", inputValue);
			
			switch(valueType){
				case STRING: data.putString("value", (String)value); break;
				case SHORT: data.putShort("value", ((Double)value).shortValue()); break;
				case INT: data.putInt("value", ((Double)value).intValue()); break;
				case LONG: data.putLong("value", ((Double)value).longValue()); break;
				case FLOAT: data.putFloat("value", ((Double) value).floatValue()); break;
				case DOUBLE: data.putDouble("value", ((Double) value).doubleValue()); break;
				case DATE: data.putLong("value", ((Date)value).getTime()); break;
			}
			
			data.putInt("value_type", valueType);
			data.putInt("value_length", valueLength);
			data.putInt("field_type", fieldType);
			data.putInt("field_id", fieldId);
			data.putBoolean("read_only", readOnly);
			data.putBoolean("mandatory", mandatory);
			data.putInt("align", align);
			data.putBundle("choices", choices.getBundle());			
			return data;
		}
		
		public void setFromBundle(Bundle data){
			id = data.getString("id");
			label = data.getString("label");
			boldLabel = data.getBoolean("bold_label");
			inputValue = data.getString("input_value");
			valueType = data.getInt("value_type");
			valueLength = data.getInt("value_length");
			fieldType = data.getInt("field_type");
			fieldId = data.getInt("field_id");
			readOnly = data.getBoolean("read_only");
			mandatory = data.getBoolean("mandatory");
			align = data.getInt("align");
			
			switch(valueType){
				case STRING: value = data.getString("value"); break;
				case SHORT: value = new Double(data.getShort("value")); break;
				case INT: value = new Double(data.getInt("value")); break;
				case LONG: value = new Double(data.getLong("value")); break;
				case FLOAT: value = new Double(data.getFloat("value")); break;
				case DOUBLE: value = new Double(data.getDouble("value")); break;
				case DATE: value = new Date(data.getLong("value")); break;
			}
			
			choices.setFromBundle(data.getBundle("choices"));
		}
		
		//Setters
		public void setId(String id){
			this.id = id;
		}		
		public void setLabel(String label){
			this.label = label;
		}
		public void setValueType(String type){
			if(type.equalsIgnoreCase("STRING")) valueType = STRING;
			if(type.equalsIgnoreCase("SHORT")) valueType = SHORT;
			if(type.equalsIgnoreCase("INT")) valueType = INT;
			if(type.equalsIgnoreCase("LONG")) valueType = LONG;
			if(type.equalsIgnoreCase("FLOAT")) valueType = FLOAT;
			if(type.equalsIgnoreCase("DOUBLE")) valueType = DOUBLE;	
			if(type.equalsIgnoreCase("DATE")) valueType = DATE;
		}
		public void setValue(String value){
			this.value = value;				
		}	
		public void setValue(int value){
			this.value = new Double(value);
		}
		public void setValue(long value){
			this.value = new Double(value);
		}
		public void setValue(float value){
			this.value = new Double(value);
		}
		public void setValue(double value){
			this.value = new Double(value);
		}
		public void setValue(Date value){
			this.value = value;
		}
		public void setValueLength(int length){
			valueLength = length;
		}
		public void setFieldType(String type){
			if(type.equalsIgnoreCase("LABEL")) fieldType = JadBlackDynamicHolder.LABEL;
			if(type.equalsIgnoreCase("EDITBOX")) fieldType = JadBlackDynamicHolder.EDITBOX;
			if(type.equalsIgnoreCase("COMBOBOX")) fieldType = JadBlackDynamicHolder.COMBOBOX;
			if(type.equalsIgnoreCase("DATEBOX")) fieldType = JadBlackDynamicHolder.DATEBOX;
			if(type.equalsIgnoreCase("BUTTON")) fieldType = JadBlackDynamicHolder.BUTTON;
		}
		public void setReadOnly(boolean value){
			readOnly = value;
		}
		public void setMandatory(boolean value){
			mandatory = value;
		}
		public void setBold(boolean value){
			boldLabel = value;
		}
		public void setAlign(String value){
			if(value.equalsIgnoreCase("LEFT")) align = Gravity.LEFT;
			if(value.equalsIgnoreCase("CENTER")) align = Gravity.CENTER;
			if(value.equalsIgnoreCase("RIGHT")) align = Gravity.RIGHT;
		}
		
		public void addChoice(String value, String description){
			choices.addItem(value, description);
		}
		
		//Getters
		public String getStringRepresentation(){
			String s = "";
			switch(valueType){
				case STRING:  s = getStringValue(); break;
				case SHORT: s = com.jadblack.StringFunctions.toString(getShortValue()); break;
				case INT: s = com.jadblack.StringFunctions.toString(getIntValue()); break;
				case LONG: s = com.jadblack.StringFunctions.toString(getLongValue()); break;
				case FLOAT: s = com.jadblack.StringFunctions.toString(getFloatValue()); break;
				case DOUBLE: s = com.jadblack.StringFunctions.toString(getDoubleValue()); break;
				case DATE: s = com.jadblack.StringFunctions.toString(getDateValue().getTime()); break;
			}
			
			double val = 0;
			try{ val = Double.valueOf(s); }catch(Exception e){}
			
			if(valueType != STRING && val == 0) s = "";			
			return s;
		}
		public String getStringValue(){
			String result = "";
			try{ result = (String) value; }catch(Exception e){}
			return result;
		}
		public short getShortValue(){
			short result = 0;
			try{ result = ((Double) value).shortValue(); }catch(Exception e){}
			return result;
		}
		public int getIntValue(){
			int result = 0;
			try{ result = ((Double) value).intValue(); }catch(Exception e){}
			return result;
		}
		public long getLongValue(){
			long result = 0;
			try{ result = ((Double) value).longValue(); }catch(Exception e){}
			return result;
		}
		public float getFloatValue(){
			float result = 0;
			try{ result = ((Double) value).floatValue(); }catch(Exception e){}
			return result;
		}
		public double getDoubleValue(){
			double result = 0;
			try{ result = ((Double) value).doubleValue(); }catch(Exception e){}
			return result;
		}
		public Date getDateValue(){
			Date result = new Date(0);
			try{ result = (Date) value; }catch(Exception e){}
			return result;
		}		
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
