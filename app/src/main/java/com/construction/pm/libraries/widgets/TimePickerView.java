package com.construction.pm.libraries.widgets;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.construction.pm.R;
import com.construction.pm.utils.ButtonUtil;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.Calendar;

public class TimePickerView extends RelativeLayout {

    private Context mContext;
    private AppCompatEditText mEditText;
    private AppCompatButton mButton;

    private TimePickerDialog mTimePickerDialog;
    private Calendar mCalendar;

    private String mTimeFormat = "HH:mm:ss";
    private String mDisplayTimeFormat= "HH:mm:ss";

    /* ----------------- */
    /* -- Constructor -- */
    /* ----------------- */

    public TimePickerView(final Context context) {
        this(context, 0, -1, 0);
    }

    public TimePickerView(final Context context, final int defaultHour, final int defaultMinute, final int defaultSecond) {
        super(context);
        mContext = context;

        // Set default format setting

        setFormat(DateTimeUtil.TIME_FORMAT);
        setFormatDisplay(DateTimeUtil.TIME_DISPLAY_FORMAT);


        // Build display

        mEditText = new AppCompatEditText(mContext);
        mEditText.setSingleLine(true);
        mEditText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        mButton = new AppCompatButton(mContext);
        mButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_timer_light_24dp));
        mButton.setId(ViewUtil.generateViewId());
        ButtonUtil.setButtonInfo(mContext, mButton);

        LayoutParams editTextLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        editTextLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        editTextLayout.addRule(RelativeLayout.CENTER_VERTICAL);
        editTextLayout.addRule(RelativeLayout.LEFT_OF, mButton.getId());

        int buttonWidth = ViewUtil.getPxFromDp(mContext, 28);
        LayoutParams buttonLayout = new LayoutParams(buttonWidth, buttonWidth);
        buttonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonLayout.addRule(RelativeLayout.CENTER_VERTICAL);

        mEditText.setLayoutParams(editTextLayout);
        mButton.setLayoutParams(buttonLayout);

        this.addView(mEditText);
        this.addView(mButton);

        if (!(defaultHour == 0 && defaultMinute == 0 && defaultSecond == 0)) {
            setTime(defaultHour, defaultMinute, defaultSecond);
        }

        Calendar defaultCalendar = Calendar.getInstance();
        int newDefaultHour = defaultHour;
        if (newDefaultHour == 0)
            newDefaultHour = defaultCalendar.get(Calendar.HOUR_OF_DAY);
        int newDefaultMinute = defaultMinute;
        if (newDefaultMinute == 0)
            newDefaultMinute = defaultCalendar.get(Calendar.MINUTE);
        int newDefaultSecond = defaultSecond;
        if (newDefaultSecond == 0)
            newDefaultSecond = defaultCalendar.get(Calendar.SECOND);

        mTimePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                setTime(i, i1, 0);
            }
        }, newDefaultHour, newDefaultMinute, true);

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog.show();
            }
        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false)
                    setTimeFromDisplay(mEditText.getText().toString());
            }
        });
    }

    public TimePickerView(final Context context, final Calendar calendar) {
        this(context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /* ------------ */
    /* -- Helper -- */
    /* ------------ */

    public Calendar getCalendar() {
        return mCalendar;
    }

    public AppCompatEditText getEditText() {
        return mEditText;
    }

    /* -------------- */
    /* -- Set Time -- */
    /* -------------- */

    public void setTime(final Calendar calendar) {
        mCalendar = calendar;
        if (mCalendar != null)
        {
            try {
                mTimePickerDialog.updateTime(mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
            } catch (Exception ex) {
                mCalendar = null;
            }
        }

        setTimeDisplay();
    }

    public void setTime(final String text) {
        setTime(DateTimeUtil.FromString(text, mTimeFormat));
    }

    public void setTime(final int hour, final int minute, final int second) {
        setTime(DateTimeUtil.FromTime(hour, minute, second));
    }

    public void setTimeFromDisplay(final String text) {
        setTime(DateTimeUtil.FromString(text, mDisplayTimeFormat));
    }

    private void setTimeDisplay() {
        mEditText.setText(getTimeDisplayString());
    }

    /* -------------- */
    /* -- Get Time -- */
    /* -------------- */

    public String getTimeString() {
        return DateTimeUtil.ToStringFormat(mCalendar, mTimeFormat);
    }

    public String getTimeDisplayString() {
        return DateTimeUtil.ToStringFormat(mCalendar, mDisplayTimeFormat);
    }

    /* ------------- */
    /* -- Setting -- */
    /* ------------- */

    public void setFormat(final String format) {
        mTimeFormat = format;
    }

    public String getFormat() {
        return mTimeFormat;
    }

    public void setFormatDisplay(final String format) {
        mDisplayTimeFormat = format;
    }

    public String getFormatDisplay() {
        return mDisplayTimeFormat;
    }
}
