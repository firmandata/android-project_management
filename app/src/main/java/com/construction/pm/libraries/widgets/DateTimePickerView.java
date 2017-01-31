package com.construction.pm.libraries.widgets;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.construction.pm.utils.DateTimeUtil;

import java.util.Calendar;

public class DateTimePickerView extends LinearLayout {
    protected Context mContext;

    protected DatePickerView mDatePickerView;
    protected TimePickerView mTimePickerView;

    public DateTimePickerView(final Context context) {
        super(context);

        mContext = context;

        setOrientation(LinearLayout.VERTICAL);

        initialize();
    }

    protected void initialize() {
        mDatePickerView = new DatePickerView(mContext);
        mDatePickerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mDatePickerView);

        mTimePickerView = new TimePickerView(mContext);
        mTimePickerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mTimePickerView);
    }

    public DatePickerView getDatePickerView() {
        return mDatePickerView;
    }

    public TimePickerView getTimePickerView() {
        return mTimePickerView;
    }

    public void setCalendar(final Calendar calendar) {
        mDatePickerView.setDate(calendar);
        mTimePickerView.setTime(calendar);
    }

    public Calendar getCalendar() {
        String dateString = mDatePickerView.getDateString();
        String timeString = mTimePickerView.getTimeString();

        return DateTimeUtil.FromDateTimeString(dateString + " " + timeString);
    }
}
