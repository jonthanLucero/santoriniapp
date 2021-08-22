package com.jadblack.Ranger;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.jadblack.R;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDateTime;

public class RangerLocal extends HorizontalScrollView implements View.OnClickListener {

    public final static String TAG = RangerLocal.class.getSimpleName();

    /**
     * Constants
     */
    //Layouts
    private final int WIDGET_LAYOUT_RES_ID = R.layout.ranger_layout;
    private final int DAY_VIEW_LAYOUT_RES_ID = R.layout.day_layout;
    //Resource ids
    public static final int DAYS_CONTAINER_RES_ID = R.id.days_container;
    public static final int DAY_OF_WEEK_RES_ID = R.id.day_of_week;
    public static final int DAY_NUMBER_RES_ID = R.id.day_number;
    public static final int MONTH_NAME_RES_ID = R.id.month_short_name;
    public static final int YEAR_ID = R.id.year_hidden;
    //Delay
    public static final int DELAY_SELECTION = 300;
    public static final int NO_DELAY_SELECTION = 0;


    /**
     * Variables
     */
    //State
    Context mContext;
    LocalDateTime mStartDate;
    LocalDateTime mEndDate;
    int mSelectedDay;
    int mSelectedMonth;
    int mSelectedYear;

    //Colors
    int mDayTextColor;
    int mSelectedDayTextColor;
    int mDaysContainerBackgroundColor;
    int mSelectedDayBackgroundColor;

    //Titles
    boolean mAlwaysDisplayMonth;
    boolean mDisplayDayOfWeek;

    //Listener
    DayViewOnClickListener mListener;
    public void setDayViewOnClickListener(DayViewOnClickListener listener) {
        mListener = listener;
    }
    public interface DayViewOnClickListener {
        public void onDaySelected(int day, int month, int year);
    }

    //Day View
    DayView mSelectedDayView;


    /**
     * Controls
     */
    Space mLeftSpace;
    LinearLayout mDaysContainer;
    Space mRightSpace;


    /**
     * Constructors
     */
    public RangerLocal(Context context) {
        super(context);
        init(context, null);
    }

    public RangerLocal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangerLocal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /**
     * Initialization
     */
    public void init(Context context, AttributeSet attributeSet) {
        mContext = context;

        //Init JodaTime
        JodaTimeAndroid.init(context);

        //Init Start and End date with current month
        final LocalDateTime currentDateTime = new LocalDateTime();
        setStartDateWithParts(currentDateTime.getYear(), currentDateTime.getMonthOfYear(), currentDateTime.dayOfMonth().withMinimumValue().getDayOfMonth());
        setEndDateWithParts(currentDateTime.plusMonths(1).getYear(), currentDateTime.plusMonths(1).getMonthOfYear(), currentDateTime.plusMonths(1).dayOfMonth().withMaximumValue().getDayOfMonth());

        //Inflate view
        View view = LayoutInflater.from(mContext).inflate(WIDGET_LAYOUT_RES_ID, this, true);

        //Get controls
        mDaysContainer = (LinearLayout) view.findViewById(DAYS_CONTAINER_RES_ID);

        //Get custom attributes
        mDisplayDayOfWeek = true;
        if(attributeSet != null) {
            TypedArray a = mContext.getTheme().obtainStyledAttributes(attributeSet, R.styleable.Ranger, 0, 0);

            try {

                //Colors
                mDayTextColor = a.getColor(R.styleable.Ranger_dayTextColor, getColor(R.color.default_day_text_color));
                mSelectedDayTextColor = a.getColor(R.styleable.Ranger_selectedDayTextColor, getColor(R.color.default_selected_day_text_color));

                mDaysContainerBackgroundColor = a.getColor(R.styleable.Ranger_daysContainerBackgroundColor, getColor(R.color.default_days_container_background_color));
                mSelectedDayBackgroundColor = a.getColor(R.styleable.Ranger_selectedDayBackgroundColor, getColor(R.color.default_selected_day_background_color));

                //Labels
                mAlwaysDisplayMonth = a.getBoolean(R.styleable.Ranger_alwaysDisplayMonth, false);
                mDisplayDayOfWeek = a.getBoolean(R.styleable.Ranger_displayDayOfWeek, true);

            } finally {
                a.recycle();
            }
        }

        //Setup styling
        //Days Container
        mDaysContainer.setBackgroundColor(mDaysContainerBackgroundColor);

        //Render control
        render();

        //Set Selection. Default is today.
        setSelectedDay(currentDateTime.getDayOfMonth(), currentDateTime.getMonthOfYear(), currentDateTime.getYear(), false, DELAY_SELECTION);
    }


    /***
     * State modification
     */
    public void setStartAndEndDateWithParts(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        setStartDateWithParts(startYear, startMonth, startDay);
        setEndDateWithParts(endYear, endMonth, endDay);

        render();
    }

    private void setStartDateWithParts(int year, int month, int day) {
        mStartDate = new LocalDateTime(year, month, day, 0, 0, 0);
    }

    private void setEndDateWithParts(int year, int month, int day) {
        mEndDate = new LocalDateTime(year, month, day, 0, 0, 0);
    }

    public void setSelectedDay(final int day, final int month, final int year, final boolean notifyListeners, long delay) {
        //Post delayed 300 ms at most because of redraw
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Deselect day selected
                if(mSelectedDay > 0 && mSelectedMonth > 0)
                    unSelectDay(mSelectedDay, mSelectedMonth);

                //Set selected day
                mSelectedDay = day;
                mSelectedMonth = month;
                mSelectedYear = year;
                selectDay(mSelectedDay, mSelectedMonth);

                //Scroll to DayView
                scrollToDayView(mSelectedDayView);

                //Call listener
                if(notifyListeners && mListener != null)
                    mListener.onDaySelected(mSelectedDay, mSelectedMonth, mSelectedYear);
            }
        }, delay);
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    public int getSelectedYear() {
        return mSelectedYear;
    }


    /**
     * Ui
     */
    private int getColor(int colorResId) {
        return getResources().getColor(colorResId);
    }

    private void render() {
        //Get inflater for view
        LayoutInflater inflater = LayoutInflater.from(mContext);

        //Add left padding
        mLeftSpace = new Space(mContext);
        mDaysContainer.addView(mLeftSpace);

        //Cycle from start day
        LocalDateTime startDate = mStartDate;
        LocalDateTime endDate = mEndDate;

        while (startDate.isBefore(endDate.plusDays(1))) {

            //Inflate view
            LinearLayout view = (LinearLayout) inflater.inflate(DAY_VIEW_LAYOUT_RES_ID, mDaysContainer, false);

            //new DayView
            DayView dayView = new DayView(view);

            //Set texts and listener
            dayView.setDayOfWeek(startDate.dayOfWeek().getAsShortText().substring(0,3));
            if(!mDisplayDayOfWeek)
                dayView.hideDayOfWeek();

            dayView.setDay(startDate.getDayOfMonth());
            dayView.setMonth(startDate.getMonthOfYear());
            dayView.setMonthShortName(startDate.monthOfYear().getAsShortText().substring(0,3));
            dayView.setYear(startDate.getYear());

            //Hide month if range in same month
            //if (!mAlwaysDisplayMonth && startDate.getMonthOfYear() == endDate.getMonthOfYear())
            //    dayView.hideMonthShortName();

            //Set style
            dayView.setTextColor(mDayTextColor);

            //Set listener
            dayView.setOnClickListener(this);

            //Add to container
            mDaysContainer.addView(dayView.getView());

            //Next day
            startDate = startDate.plusDays(1);
        }

        //Add right padding
        mRightSpace = new Space(mContext);
        mDaysContainer.addView(mRightSpace);
    }

    private void unSelectDay(int day, int month) {
        for (int i = 1; i < mDaysContainer.getChildCount() - 1; i++) {
            DayView dayView = new DayView(mDaysContainer.getChildAt(i));
            if(dayView.getDay() == day && dayView.getMonth() == month) {
                dayView.setTextColor(mDayTextColor);
                dayView.setBackgroundColor(0);
                return;
            }
        }
    }

    private void selectDay(int day, int month) {
        for (int i = 1; i < mDaysContainer.getChildCount() - 1; i++) {
            DayView dayView = new DayView(mDaysContainer.getChildAt(i));
            if(dayView.getDay() == day  && dayView.getMonth() == month) {

                dayView.setTextColor(mSelectedDayTextColor);
                dayView.setBackgroundColor(mSelectedDayBackgroundColor);

                mSelectedDayView = dayView;

                return;
            }
        }
    }

    public void scrollToDayView(DayView dayView) {
        int x = dayView.getView().getLeft();
        int y = dayView.getView().getTop();
        smoothScrollTo(x - mLeftSpace.getLayoutParams().width, y);
    }


    /**
     * On DayView click listener
     */
    @Override
    public void onClick(View view) {
        //Get day view
        DayView dayView = new DayView(view);

        //Get selected day and set selection
        int selectedDay = dayView.getDay();
        int selectedMonth = dayView.getMonth();
        int selectedYear = dayView.getYear();
        setSelectedDay(selectedDay, selectedMonth, selectedYear, true, NO_DELAY_SELECTION);
    }


    /**
     * Custom implementation for left and right spaces
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(changed) {
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

            LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) mLeftSpace.getLayoutParams();
            leftParams.width = getWidth() / 2 - padding;
            mLeftSpace.setLayoutParams(leftParams);

            LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) mRightSpace.getLayoutParams();
            rightParams.width = getWidth() / 2 - padding;
            mRightSpace.setLayoutParams(rightParams);
        }
    }


    /**
     * Configuration change handling
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.setSelectedDay(mSelectedDay);
        savedState.setSelectedMonth(mSelectedMonth);
        savedState.setSelectedYear(mSelectedYear);
        savedState.setStartDateString(mStartDate.toString());
        savedState.setEndDateString(mEndDate.toString());

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        mSelectedDay = savedState.getSelectedDay();
        mSelectedMonth = savedState.getSelectedMonth();
        mSelectedYear = savedState.getSelectedYear();
        mStartDate = LocalDateTime.parse(savedState.getStartDateString());
        mEndDate = LocalDateTime.parse(savedState.getEndDateDateString());

        render();

        setSelectedDay(mSelectedDay, mSelectedMonth, mSelectedYear, false, DELAY_SELECTION);
    }

    protected static class SavedState extends BaseSavedState {
        int mSelectedDay;
        int mSelectedMonth;
        int mSelectedYear;

        String mStartDateString;
        String mEndDateString;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel in) {
            super(in);
            mSelectedDay = in.readInt();
            mSelectedMonth = in.readInt();
            mSelectedYear = in.readInt();
            mStartDateString = in.readString();
            mEndDateString = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mSelectedDay);
            out.writeInt(mSelectedMonth);
            out.writeInt(mSelectedYear);
            out.writeString(mStartDateString);
            out.writeString(mEndDateString);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public void setSelectedDay(int selectedDay) {
            mSelectedDay = selectedDay;
        }

        public void setSelectedMonth(int selectedMonth) {
            mSelectedMonth = selectedMonth;
        }

        public void setSelectedYear(int selectedYear) {
            mSelectedYear = selectedYear;
        }

        public void setStartDateString(String startDateString) {
            mStartDateString = startDateString;
        }

        public void setEndDateString(String endDateString) {
            mEndDateString = endDateString;
        }

        public int getSelectedDay() {
            return mSelectedDay;
        }

        public int getSelectedMonth() {
            return mSelectedMonth;
        }

        public int getSelectedYear() {
            return mSelectedYear;
        }

        public String getStartDateString() {
            return mStartDateString;
        }

        public String getEndDateDateString() {
            return mEndDateString;
        }
    }


    /**
     * DateView class
     */
    public static class DayView {

        int mDay;
        int mMonth;
        int mYear;

        LinearLayout mView;
        TextView mDayOfWeek;
        TextView mDayNumber;
        TextView mMonthShortName;
        TextView mYearControl;

        public DayView(View view) {
            mView = (LinearLayout) view;

            mDayOfWeek = (TextView) mView.findViewById(DAY_OF_WEEK_RES_ID);
            mDayNumber = (TextView) mView.findViewById(DAY_NUMBER_RES_ID);
            mMonthShortName = (TextView) mView.findViewById(MONTH_NAME_RES_ID);
            mYearControl = (TextView) mView.findViewById(YEAR_ID);
        }

        public int getDay() {
            return Integer.parseInt(mDayNumber.getText().toString());
        }

        public int getMonth() {
            return Integer.parseInt(mMonthShortName.getTag().toString());
        }

        public int getYear(){
            return Integer.parseInt(mYearControl.getText().toString());
        }

        public void setDay(int day) {
            mDay = day;
            setDayNumber(String.format("%02d", day));
        }

        public void setMonth(int month) {
            mMonth = month;
            setMonthNumberTag(String.format("%02d", month));
        }

        public void setYear(int year){
            mYear = year;
            setYearControl(String.format("%4d", year));
        }

        public void setDayOfWeek(String dayOfWeek) {
            mDayOfWeek.setText(dayOfWeek);
        }

        public void setDayNumber(String dayNumber) {
            mDayNumber.setText(dayNumber);
        }

        public void setMonthNumberTag(String monthNumber) {
            mMonthShortName.setTag(monthNumber);
        }

        public void setMonthShortName(String monthShortName) {
            mMonthShortName.setText(monthShortName);
        }

        public void setYearControl(String yearControlText) {
            mYearControl.setText(yearControlText);
        }

        public void setBackgroundColor(int color) {
            mView.setBackgroundColor(color);
        }

        public void setTextColor(int color) {
            mDayOfWeek.setTextColor(color);
            mDayNumber.setTextColor(color);
            mMonthShortName.setTextColor(color);
        }

        public void setOnClickListener(OnClickListener listener) {
            mView.setOnClickListener(listener);
        }

        public View getView() {
            return mView;
        }

        public void hideDayOfWeek() {
            mDayOfWeek.setVisibility(View.GONE);
        }

        public void hideMonthShortName() {
            mMonthShortName.setVisibility(View.GONE);
        }

    }

}