package com.jadblack.MultimediaViewPager;

import java.util.ArrayList;
import java.util.Set;

import android.os.Bundle;

public class MultimediaItemManager {
	
	public ArrayList<MultimediaItem> items = new ArrayList<MultimediaItem>();
	public MultimediaItem currentItem;
	
	public MultimediaItem getCurrentItem(){
		return currentItem;
	}
	
	public void createItem(){
		currentItem = new MultimediaItem();
	}
	
	public void addItem(){
		items.add(currentItem);
	}
	
	public void clearItems(){
		items = new ArrayList<MultimediaItem>();
	}
	
	public int size(){
		return items.size();
	}
	
	public MultimediaItem get(int index){
		try{
			return items.get(index);
		}catch(Exception e){
			
		}
		return null;
	}
	
	public Bundle getBundle(){
		String key;
		
		Bundle data = new Bundle();
		for(int i=0; i<items.size(); i++){
			key = Integer.toString(i);
			data.putBundle(key, items.get(i).getBundle());			
		}
		return data;
	}
	
	public void setFromBundle(Bundle data){
		String key;
		MultimediaItem item;
		
		if(data == null) return;
		
		Set<String> keys = data.keySet();
		for(int i = 0; i < keys.size(); i++){
			key = String.valueOf(i);
			if(data.containsKey(key)){
				item = new MultimediaItem();
				item.setFromBundle(data.getBundle(key));
				items.add(item);				
			}
		}
	}
	
	public static class MultimediaItem{
		public String id;
		public String title;
		public String description;
		public String url;
		public String path;
		int type;
		float size;
		public String notes;
		
		
		public class Type{
			public static final int BASE = 0;
			public static final int PICTURE = 1;
			public static final int YOUTUBE = 2;
			public static final int PDF = 3;
			protected static final int WORD = 4;
			protected static final int EXCEL = 5;
		}
		
		public class Parms{
			public static final String ID = "id";
			public static final String NAME = "name";
			public static final String DESCRIPTION = "description";
			public static final String URL = "url";
			public static final String PATH = "path";
			public static final String TYPE = "type";
			public static final String SIZE = "size";
			public static final String NOTES = "notes";
		}
		
		
		public MultimediaItem() {
			type = Type.BASE;
		}
		
		public void setYoutube(){
			type = Type.YOUTUBE;
		}
		
		public void setPicture(){
			type = Type.PICTURE;
		}
		
		public void setPdf(){
			type = Type.PDF;
		}
		
		public void setWord(){
			type = Type.WORD;
		}
		
		public void setExcel(){
			type = Type.EXCEL;
		}
	
		public Bundle getBundle() {
			Bundle data = new Bundle();
			data.putString(Parms.ID, id);
			data.putString(Parms.NAME, title);
			data.putString(Parms.DESCRIPTION, description);
			data.putString(Parms.URL, url);
			data.putString(Parms.PATH, path);
			data.putInt(Parms.TYPE, type);
			data.putFloat(Parms.SIZE, size);
			data.putString(Parms.NOTES, notes);
			
			return data;
		}
		
		public void setFromBundle(Bundle data){
			id = data.getString(Parms.ID);
			title = data.getString(Parms.NAME);
			description = data.getString(Parms.DESCRIPTION);
			url = data.getString(Parms.URL);
			path = data.getString(Parms.PATH);
			type = data.getInt(Parms.TYPE);
			size = data.getFloat(Parms.SIZE);
			notes = data.getString(Parms.NOTES);
		}
	}
}
