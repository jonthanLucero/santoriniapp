
package com.jadblack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JadBlackAutoCompleteField extends LinearLayout {
	public static final String tag = "JadBlackAutoCompleteField";
	
	public String textTyped = "";
	public String selectedValue = "";
	public String selectedDescription = "";
	public int maxPublisedResults = 100;

	//AUTO COMPLETE FORM
	public String label;
	public TextView autoCompleteFieldLabel;
	public AutoCompleteTextView autoCompleteField;
	public AutoCompleteAdapter mAdapter;
	
	//SELECTED VALUE FORM
	public TextView selectedValueLabel;
	public TextWatcher selectedValueLabelListener;
	public Button selectedValueButton;	
	
	//JadBlack Auto Complete Resource
	public static class JadBlackAutoCompleteResource{
		public Vector<String> tempValues;
		public Vector<String> tempDescriptions;
		public String[] values;
		public String[] descriptions;
		public String fileName;		
				
		public JadBlackAutoCompleteResource(){
			tempValues = new Vector<String>();
			tempDescriptions = new Vector<String>();
		}
		
		public void addItem(String value, String description){
			tempValues.add(value);
			tempDescriptions.add(description);
		}
		
		public void load(Context context, String fileName){
			values = new String[tempValues.size()];
			descriptions = new String[tempDescriptions.size()];
			
			tempValues.copyInto(values);
			tempDescriptions.copyInto(descriptions);
			
			tempValues = null;
			tempDescriptions = null;
			
			this.fileName = fileName;			
			toFile(context);
		}
		
		public String getFileName(){
			return (fileName != null) ? fileName : "";
		}
		
		public void setFileName(String f, Context c){
			fileName = f;
			fromFile(c);
		}
		
		public void clear(Context context){
			String filePath = SystemFunctions.getDownloadsDirectory(context) + fileName;
			FileFunctions.filedelete(filePath);
		}
		
		public Bundle getBundle(){
			Bundle data = new Bundle();
			data.putString("file_name", fileName);			
			return data;
		}
		
		public void setFromBundle(Bundle data){
			if(data == null) return;			
			this.fileName = data.getString("file_name");
		}
		
		public String toJSON(){
			JSONObject wrapper = new JSONObject();
			JSONArray jsonValues = new JSONArray();
			JSONArray jsonDescriptions = new JSONArray();
			for( int i = 0; i < values.length; i ++){
				jsonValues.put(values[i]);
				jsonDescriptions.put(descriptions[i]);
			}
			try{
				wrapper.put("values", jsonValues);
				wrapper.put("descriptions", jsonDescriptions);
			}catch (JSONException e){
				return null;
			}		
			return wrapper.toString();
		}
		
		public boolean fromJSON(String content){
			try{
				JSONObject jWrapper = new JSONObject(content);
				JSONArray jValues = jWrapper.getJSONArray("values");
				JSONArray jDescriptions = jWrapper.getJSONArray("descriptions");				
				values = new String[jValues.length()];
				descriptions = new String[jDescriptions.length()];
				for(int i = 0; i < jValues.length(); i++){
					values[i] = jValues.getString(i);
					descriptions[i] = jDescriptions.getString(i);
				}
				return true;
			}catch(Exception e){
				return false;
			}
		}
		
		public boolean toFile(Context context){			
			String filePath = SystemFunctions.getDownloadsDirectory(context) + fileName;
			String content = toJSON();
			try{
				File f = new File(filePath);
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(content.getBytes());
				fos.close();
				
				values = null;
				descriptions = null;

				Log.d(tag, "File saved to " + filePath);
				return true;
			}catch(Exception e){
				e.printStackTrace();

				Log.d(tag, "Write error: " + e);
				return false;
			}			
		}
		
		public boolean fromFile(Context context){
			try{
				String filePath = SystemFunctions.getDownloadsDirectory(context) + fileName;
				File f = new File(filePath);
				FileInputStream fis = new FileInputStream(f);
				byte[] buffer = new byte[(int) f.length()];
				fis.read(buffer);
				fis.close();				
				String content = new String(buffer);

				Log.d(tag, "File loaded from " + filePath);
				return fromJSON(content);
			}catch(Exception e){
				e.printStackTrace();

				Log.d(tag, "Read error: " + e);
				return false;
			}			
		}
	}
	
	//Constructors
	public JadBlackAutoCompleteField(Context context){ super(context); initialize(); }
	public JadBlackAutoCompleteField(Context context, AttributeSet attrs){ super(context, attrs); initialize(); }
	
	public void initialize(){
		if( isInEditMode() ){
			addView(new EditText(getContext()));
			return;
		}
		//Inflate Layout
		inflate(getContext() , R.layout.jadblackautocompletefield_layout, this);
		//Get Controls References	
		getAutoCompleteForm();
		getSelectedValueForm();
		//Hide Selected Value Wrapper
		selectedValueWrapperToggleVisibility(View.GONE);		
	}
	
	public void setLabel(String l){
		label = l;
		autoCompleteFieldLabel.setText(label);
	}
	
	//Controls References
	public void getAutoCompleteForm(){
		autoCompleteFieldLabel = (TextView) findViewById(R.id.autoCompleteFieldLabel);
		autoCompleteField = (AutoCompleteTextView) findViewById(R.id.autoCompleteField);
		autoCompleteField.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable e) {
			}

			public void beforeTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
				textTyped = c.toString();
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
		});		
	}
	public void setListener(TextWatcher listener){
		selectedValueLabelListener = listener;	
	}
	
	public void getSelectedValueForm(){
		selectedValueLabel = (TextView) findViewById(R.id.selectedValueLabel);
		selectedValueButton = (Button) findViewById(R.id.selectedValueButton);
		selectedValueButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				selectedValueLabel.addTextChangedListener(selectedValueLabelListener);
				unSetValue();
				selectedValueLabel.removeTextChangedListener(selectedValueLabelListener);
			}			
		});
	}
	
	//Wrappers Visibility Toggle Methods
	public void autoCompleteFieldWrapperToggleVisibility(int visibility){
		LinearLayout autoCompleteFieldWrapper = (LinearLayout) findViewById(R.id.autoCompleteFieldWrapper);
		autoCompleteFieldWrapper.setVisibility(visibility);
	}	
	public void selectedValueWrapperToggleVisibility(int visibility){
		LinearLayout selectedValueWrapper = (LinearLayout) findViewById(R.id.selectedValueWrapper);
		selectedValueWrapper.setVisibility(visibility);
	}
	
	//Auto Complete Load From Resource Method
	public void loadFromResource(JadBlackAutoCompleteResource resource){
		resource.fromFile(getContext());
		String[] values = resource.values;
		String[] descriptions = resource.descriptions;
		String fileName = resource.fileName;
		
		mAdapter = new AutoCompleteAdapter(getContext(), fileName);
		mAdapter.setValuesAndDescriptions(values, descriptions);
		autoCompleteField.setAdapter(mAdapter);
		autoCompleteField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				selectedDescription = (String) listView.getItemAtPosition(position);
				selectedValue = mAdapter.getValueFromDescription(selectedDescription);
				selectedValueLabel.addTextChangedListener(selectedValueLabelListener);
				setValue();
				selectedValueLabel.removeTextChangedListener(selectedValueLabelListener);
			}
		});
	}
	
	public String getValue(){
		return selectedValue;
	}
	
	//Set Value
	public void setValue(String value){
		if( !selectedValue.equals(value) ){
			selectedValue = value;
			if(selectedDescription.equals("")){
				selectedDescription = mAdapter.getDescriptionFromValue(selectedValue);
			}
			setValue();
		}
	}
	
	public void setValue(){
		autoCompleteFieldWrapperToggleVisibility(View.GONE);
		selectedValueWrapperToggleVisibility(View.VISIBLE);
		selectedValueLabel.setText(selectedDescription);
	}
	
	//Unset Value
	public void unSetValue(){
		selectedValue = "";
		selectedDescription = "";		
		
		selectedValueLabel.setText("");
		selectedValueWrapperToggleVisibility(View.GONE);
		
		autoCompleteFieldWrapperToggleVisibility(View.VISIBLE);		
		autoCompleteField.setText(textTyped);

		try {
			autoCompleteField.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Bundle getBundle(){
		Bundle data = new Bundle();		
		data.putString("typed_text", textTyped);
		data.putString("selected_value", selectedValue);
		data.putString("selected_description", selectedDescription);
		data.putBundle("data", mAdapter.getBundle());
		return data;
	}
	
	public synchronized void updateSelf(Bundle stateData){		
		if(stateData.containsKey("typed_text")) textTyped = stateData.getString("typed_text");
		if(stateData.containsKey("selected_value")) selectedValue = stateData.getString("selected_value");
		if(stateData.containsKey("selected_description")) selectedDescription = stateData.getString("selected_description");
		if( stateData.containsKey("data") ){
			JadBlackAutoCompleteResource resource = new JadBlackAutoCompleteResource();
			resource.setFromBundle(stateData.getBundle("data"));
			loadFromResource(resource);
		}
		handleChange();
	}

	public void setResource(JadBlackAutoCompleteResource resource) {
		Bundle data = new Bundle();
		data.putBundle("data", resource.getBundle());
		updateSelf(data);
	}
	
	public void handleChange(){
		if(!selectedValue.equals("")){
			setValue();
		}else{
			unSetValue();
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
	
	//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	//Array Adapter
	@SuppressLint("DefaultLocale")
	private final class AutoCompleteAdapter extends ArrayAdapter<String>{
		public String[] values;
		public String[] descriptions;
		public String fileName; 
		public AutoCompleteFilter mFilter;

		public AutoCompleteAdapter(Context context, String fileName){
			super(context, R.layout.jadblackautocompletefield_list_item_layout, R.id.item);
			this.fileName = fileName;
		}
		
		public String getValueFromDescription(String description){
			for(int i = 0; i < descriptions.length; i++){
				if( descriptions[i].equals(description) ){
					return values[i];
				}
			}
			return "";
		}
		
		public String getDescriptionFromValue(String value){
			for(int i = 0; i < values.length; i++){
				if( values[i].equals(value) ){
					return descriptions[i];
				}
			}
			return "";
		}
		
		public void setValuesAndDescriptions(String[] v, String[] d){
			values = v;
			descriptions = d;
			populate();
		}
		
		public void populate(){
			if( values != null && descriptions != null ){
				for(int i = 0; i < descriptions.length; i++ )
					add(descriptions[i]);
			}
			notifyDataSetChanged();
		}
		
		public Bundle getBundle(){
			Bundle data = new Bundle();		
			data.putString("file_name", fileName);
			return data;
		}
		
		public Filter getFilter(){
			if( mFilter == null)
				mFilter = new AutoCompleteFilter();
			return mFilter;
		}
				
		private final class AutoCompleteFilter extends Filter{			
			protected FilterResults performFiltering(CharSequence filterWords) {
				FilterResults results = new FilterResults();
				String[] newValues = null;
				Vector<String> filteredValues = new Vector<String>();
				
				if(filterWords == null || filterWords.length() == 0){
					newValues = descriptions;
			    }
			    else{
				    boolean itemContainsAllKeys;
				    String[] filterWordsArray = new String(filterWords.toString()).split(" ");
				    for(int i = 0; i < descriptions.length; i++){
				    	String o = descriptions[i];
				    	itemContainsAllKeys = true;
				    	for(int j = 0; j < filterWordsArray.length; j++){
				    		if(!o.toString().toLowerCase().contains(filterWordsArray[j].toLowerCase())){
				    			itemContainsAllKeys = false;
				    			break;
				    		}
				    	}
				    	if(itemContainsAllKeys){
				    		filteredValues.add(o);
				    		if(filteredValues.size() >= maxPublisedResults)
				    			break;
				    	}
				    }
			    }
				if( filteredValues.size() > 0){
					newValues = new String[filteredValues.size()];
					filteredValues.copyInto(newValues);
				}
				
				results.values = newValues;
			    results.count = newValues.length;
			    return results;
			}
		
			protected void publishResults(CharSequence filterWords, FilterResults filterResults){
			    String[] newValues = (String[]) filterResults.values;
			    clear();
			    if( newValues != null){
				    for(int i = 0; i < newValues.length; i++){
				    	add(newValues[i]);
				    	if( i >= maxPublisedResults){
				    		break;
				    	}
				    }
			    }
			}			
		}
	}
}
