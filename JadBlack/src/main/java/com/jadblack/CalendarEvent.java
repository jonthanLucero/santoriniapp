
/*
 * Implementation following John Malizia guidelines on 
 * http://pastebin.com/jpnqTp1n (2011)
 */
package com.jadblack;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

@SuppressWarnings("unused")
public class CalendarEvent{
	//UID
    private long _UID;
    //SUBJECT
    private String _subject;
    //LOCATION
    private String _location;
    //START DATE
    private Date _startDate;
    //END DATE
    private Date _endDate;
    //NOTE
    private String _note;
    //ALARM TIME    
    private int _alarmTime;
    //ALARM REQUIRED
    private boolean _alarmRequired;
    
    //CONFIGURATION VARIABLES
    private Uri remindersUri;
    private Uri eventsUri;
    private String eventsTable;
    private String contentProvider;
    
    //EVENT COLUMNS
    public static class EventColumns{
        public static final String ID = "calendar_id";
        public static final String TITLE = "title";
        public static final String DESC = "description";
        public static final String LOCATION = "eventLocation";
        public static final String START = "dtstart";
        public static final String END = "dtend";
        public static final String ALLDAY = "allDay";
        public static final String STATUS = "eventStatus";
        public static final String VIS = "visibility";
        public static final String TRANS = "transparency";
        public static final String ALARM = "hasAlarm";
    }
    
    public CalendarEvent(){
        this._UID = 0;
        this._subject = "";
        this._location = "";
        this._startDate = new Date();
        this._endDate = new Date();
        this._note = "";
        this._alarmTime = 0;
        this._alarmRequired = false;
        
        initCalendarConfig();
    }
    
    //INIT URIS 
    public void initCalendarConfig(){
		int sdk;
		//GET SDK VERSION
		try{
			sdk = new Integer(Build.VERSION.SDK_INT).intValue();
		}
		catch(Exception e){
			sdk = 9;
		}
		//EVALUATE SDK FOR AVAILABLE EVENT TABLE AND CONTENT PROVIDER
		if(sdk >= 8){
			//2.2 OR HIGHER
			eventsTable = "view_events";
			contentProvider = "com.android.calendar";
		}
		else{
			//ANYTHING ONLER
			eventsTable = "Events";
			contentProvider = "calendar";
		}
		//URIS
		remindersUri = Uri.parse(String.format("content://%s/reminders",contentProvider));
		eventsUri = Uri.parse(String.format("content://%s/events",contentProvider));		
    }
    
    //UID
    public void setUID(long UID){
        this._UID = UID;
    }
    public long getUID(){
        return this._UID;
    }
    //SUBJECT
    public void setSubject(String subject){
        this._subject = subject;
    }
    public String getSubject(){
        return this._subject;
    }
    //LOCATION
    public void setLocation(String location){
        this._location = location;
    }
    public String getLocation(){
        return this._location;
    }
    //START DATE
    public void setStartDate(Date startDate){
        this._startDate = startDate;
    }
    public Date getStartDate(){
        return this._startDate;
    }
    //END DATE
    public void setEndDate(Date endDate){
        this._endDate = endDate;
    }
    public Date getEndDate(){
        return this._endDate;
    }
    //NOTE
    public void setNote(String note){
        this._note = note;
    }
    public String getNote(){
        return this._note;
    }
    //ALARM MINUTES
    public void setAlarmMinutes(int alarmTime){
        this._alarmTime = alarmTime;
        this._alarmRequired = true;
    }
    public int getAlarmMinutes(){
        return this._alarmTime;
    }
    
    //VERIFY DATA METHOD
    private boolean verifyData(){
        //VERIFY SUBJECT
        if( Comparer.process(this._subject, "=", "") ){
            ErrorHandler.getInstance().setErrorMessage("Calendar Event Subject can not be empty");
            return false;
        }
        //VERIFY START AND END DATE
        if( DateFunctions.datediff(this._endDate, this._startDate) <= 0 ){
            ErrorHandler.getInstance().setErrorMessage("Calendar Event End Date less than Start Date");
            return false;
        }
        //VERIFY ALARM
        if( this._alarmRequired ){
            if( this._alarmTime < 0 ){
                ErrorHandler.getInstance().setErrorMessage("Calendar Event Alarm Minutes can no be negative");
                return false;
            }
        }
        //IF NO ERRORS, RETURN TRUE
        return true;
    }
    
    //INSERT METHOD
    public boolean insert(Context context){
    	//RETURN VALUE
    	boolean insertOK = false;
		//FLUSH ERRORS
		ErrorHandler.getInstance().clean();
		//VERIFY DATA
		if( this.verifyData() == false ) return false;
		
		try{
			//BUILT IN CALENDAR ( CALENDAR ID = 1 )
			int calendarId = 1;
			ContentValues event = new ContentValues();
			event.put(EventColumns.ID, calendarId);
			event.put(EventColumns.TITLE, _subject);
			event.put(EventColumns.DESC, _note);
			event.put(EventColumns.START, _startDate.getTime());
			event.put(EventColumns.END, _endDate.getTime());
			event.put(EventColumns.LOCATION, _location);
			event.put(EventColumns.ALLDAY, "0");
			event.put(EventColumns.STATUS, "1"); //CONFIRMED
			event.put(EventColumns.VIS, "0");
			event.put(EventColumns.TRANS, "0");
			event.put(EventColumns.ALARM, "1");
            
			_UID = Long.parseLong(context.getContentResolver().insert(eventsUri, event).getLastPathSegment());
			
			insertOK = true;
			
			if( _alarmRequired ){
				ContentValues values = new ContentValues();
				values.put("event_id", _UID);
				values.put("method", 1);
				values.put("minutes", _alarmTime);
				context.getContentResolver().insert(remindersUri, values);
			}			
		}catch(Exception e){
			ErrorHandler.getInstance().setErrorMessage("Error during Calendar Event insert: " + e);
			insertOK = false;
		}
		return insertOK;
    }
}
