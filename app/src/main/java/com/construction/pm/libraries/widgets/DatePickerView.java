package com.construction.pm.libraries.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.Calendar;

public class DatePickerView extends RelativeLayout {

    private Context mContext;
    private AppCompatEditText mEditText;
    private FloatingActionButton mButton;

    private DatePickerDialog mDatePickerDialog;
    private Calendar mCalendar;

    private String mDateFormat = "yyyy-MM-dd";
    private String mDisplayDateFormat= "dd-MM-yyyy";

    /* ----------------- */
    /* -- Constructor -- */
    /* ----------------- */

    public DatePickerView(final Context context) {
        super(context);
        initialize(context, 0, -1, 0);
    }

    public DatePickerView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, 0, -1, 0);
    }

    public DatePickerView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, 0, -1, 0);
    }

    public DatePickerView(final Context context, final Calendar calendar) {
        super(context);
        initialize(context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    protected void initialize(Context context, final int defaultYear, final int defaultMonthOfYear, final int defaultDayOfMonth) {
        mContext = context;

        // Set default format setting

        setFormat(DateTimeUtil.DATE_FORMAT);
        setFormatDisplay(DateTimeUtil.DATE_DISPLAY_FORMAT);


        // Build display

        mEditText = new AppCompatEditText(mContext);
        mEditText.setSingleLine(true);
        mEditText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mEditText.setHint(mDisplayDateFormat);

        mButton = new FloatingActionButton(mContext);
        mButton.setImageResource(R.drawable.ic_today_light_24dp);
        mButton.setId(ViewUtil.generateViewId());

        LayoutParams editTextLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        editTextLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        editTextLayout.addRule(RelativeLayout.LEFT_OF, mButton.getId());

        LayoutParams buttonLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        mEditText.setLayoutParams(editTextLayout);
        mButton.setLayoutParams(buttonLayout);

        this.addView(mEditText);
        this.addView(mButton);

        if (!(defaultYear == 0 && defaultMonthOfYear == -1 && defaultDayOfMonth == 0)) {
            setDate(defaultYear, defaultMonthOfYear, defaultDayOfMonth);
        }

        Calendar defaultCalendar = Calendar.getInstance();
        int newDefaultYear = defaultYear;
        if (newDefaultYear == 0)
            newDefaultYear = defaultCalendar.get(Calendar.YEAR);
        int newDefaultMonthOfYear = defaultMonthOfYear;
        if (newDefaultMonthOfYear == -1)
            newDefaultMonthOfYear = defaultCalendar.get(Calendar.MONTH);
        int newDefaultDayOfMonth = defaultDayOfMonth;
        if (newDefaultDayOfMonth == 0)
            newDefaultDayOfMonth = defaultCalendar.get(Calendar.DAY_OF_MONTH);

        if (!isInEditMode()) {
            mDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    setDate(year, monthOfYear, dayOfMonth);
                    mEditText.setError(null);
                }
            }, newDefaultYear, newDefaultMonthOfYear, newDefaultDayOfMonth);
        }

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    setDateFromDisplay(mEditText.getText().toString());
            }
        });
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
    /* -- Set Date -- */
    /* -------------- */

    public void setDate(final Calendar calendar) {
        mCalendar = calendar;
        if (mCalendar != null)
        {
            try {
                mDatePickerDialog.updateDate(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
            } catch (Exception ex) {
                mCalendar = null;
            }
        }

        setDateDisplay();
    }

    public void setDate(final String text) {
        setDate(DateTimeUtil.FromString(text, mDateFormat));
    }

    public void setDate(final int year, final int monthOfYear, final int dayOfMonth) {
        setDate(DateTimeUtil.FromDate(year, monthOfYear, dayOfMonth));
    }

    public void setDateFromDisplay(final String text) {
        setDate(DateTimeUtil.FromString(text, mDisplayDateFormat));
    }

    private void setDateDisplay() {
        mEditText.setText(getDateDisplayString());
    }

    /* -------------- */
    /* -- Get Date -- */
    /* -------------- */

    public String getDateString() {
        return DateTimeUtil.ToStringFormat(mCalendar, mDateFormat);
    }

    public String getDateDisplayString() {
        return DateTimeUtil.ToStringFormat(mCalendar, mDisplayDateFormat);
    }

    /* ------------- */
    /* -- Setting -- */
    /* ------------- */

    public void setFormat(final String format) {
        mDateFormat = format;
    }

    public String getFormat() {
        return mDateFormat;
    }

    public void setFormatDisplay(final String format) {
        mDisplayDateFormat = format;
        if (mEditText != null)
            mEditText.setHint(mDisplayDateFormat);
    }

    public String getFormatDisplay() {
        return mDisplayDateFormat;
    }
}
