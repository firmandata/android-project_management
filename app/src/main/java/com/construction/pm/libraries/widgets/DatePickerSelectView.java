package com.construction.pm.libraries.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerSelectView extends RelativeLayout {

    protected Context mContext;

    protected Calendar mCalendar;

    protected AppCompatSpinner mSpDayOfMonth;
    protected SpinnerDayOfMonthAdapter mSpDayOfMonthAdapter;
    protected AppCompatSpinner mSpMonthOfYear;
    protected SpinnerMonthOfYearAdapter mSpMonthOfYearAdapter;
    protected AppCompatSpinner mSpYear;
    protected SpinnerYearAdapter mSpYearAdapter;

    /* ----------------- */
    /* -- Constructor -- */
    /* ----------------- */

    public DatePickerSelectView(Context context) {
        super(context);

        mContext = context;

        initializeView();
    }

    protected void initializeView() {
        mSpDayOfMonthAdapter = new SpinnerDayOfMonthAdapter(mContext, android.R.layout.simple_spinner_dropdown_item);
        mSpDayOfMonthAdapter.setInitialValue();
        for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
            mSpDayOfMonthAdapter.add(dayOfMonth);
        }
        mSpDayOfMonth = new AppCompatSpinner(mContext);
        mSpDayOfMonth.setId(ViewUtil.generateViewId());
        mSpDayOfMonth.setAdapter(mSpDayOfMonthAdapter);
        mSpDayOfMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                generateCalendar();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpMonthOfYearAdapter = new SpinnerMonthOfYearAdapter(mContext, android.R.layout.simple_spinner_dropdown_item);
        mSpMonthOfYearAdapter.setInitialValue();
        for (int monthOfYear = 0; monthOfYear <= 11; monthOfYear++) {
            mSpMonthOfYearAdapter.add(monthOfYear);
        }
        mSpMonthOfYear = new AppCompatSpinner(mContext);
        mSpMonthOfYear.setId(ViewUtil.generateViewId());
        mSpMonthOfYear.setAdapter(mSpMonthOfYearAdapter);
        mSpMonthOfYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                generateCalendar();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpYearAdapter = new SpinnerYearAdapter(mContext, android.R.layout.simple_spinner_dropdown_item);
        mSpYearAdapter.setInitialValue();
        Calendar nowCalendar = Calendar.getInstance();
        int yearBegin = nowCalendar.get(Calendar.YEAR) - 10;
        int yearEnd = nowCalendar.get(Calendar.YEAR) - 100;
        for (int year = yearBegin; year >= yearEnd; year--) {
            mSpYearAdapter.add(year);
        }
        mSpYear = new AppCompatSpinner(mContext);
        mSpYear.setId(ViewUtil.generateViewId());
        mSpYear.setAdapter(mSpYearAdapter);
        mSpYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                generateCalendar();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            LayoutParams spDayOfMonthLayout = new LayoutParams(ViewUtil.getPxFromDp(mContext, 75), LayoutParams.WRAP_CONTENT);
            spDayOfMonthLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            spDayOfMonthLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mSpDayOfMonth.setLayoutParams(spDayOfMonthLayout);
        addView(mSpDayOfMonth);

            LayoutParams spMonthOfYearLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            spMonthOfYearLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            spMonthOfYearLayout.addRule(RelativeLayout.RIGHT_OF, mSpDayOfMonth.getId());
            spMonthOfYearLayout.addRule(RelativeLayout.LEFT_OF, mSpYear.getId());
            spMonthOfYearLayout.setMargins(ViewUtil.getPxFromDp(mContext, 5), 0, 0, 0);
        mSpMonthOfYear.setLayoutParams(spMonthOfYearLayout);
        addView(mSpMonthOfYear);

            LayoutParams spYearLayout = new LayoutParams(ViewUtil.getPxFromDp(mContext, 95), LayoutParams.WRAP_CONTENT);
            spYearLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            spYearLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            spYearLayout.setMargins(ViewUtil.getPxFromDp(mContext, 5), 0, 0, 0);
        mSpYear.setLayoutParams(spYearLayout);
        addView(mSpYear);
    }

    /* ----------- */
    /* -- Value -- */
    /* ----------- */

    public void setDate(final Calendar calendar) {
        mCalendar = calendar;

        if (mCalendar != null) {
            int dayOfMonthPosition = mSpDayOfMonthAdapter.getPositionByItem(mCalendar.get(Calendar.DAY_OF_MONTH));
            mSpDayOfMonth.setSelection(dayOfMonthPosition);

            int monthOfYearPosition = mSpMonthOfYearAdapter.getPositionByItem(mCalendar.get(Calendar.MONTH));
            mSpMonthOfYear.setSelection(monthOfYearPosition);

            int yearPosition = mSpYearAdapter.getPositionByItem(mCalendar.get(Calendar.YEAR));
            mSpYear.setSelection(yearPosition);
        } else {
            mSpDayOfMonth.setSelection(0);
            mSpMonthOfYear.setSelection(0);
            mSpYear.setSelection(0);
        }
    }

    public void setDate(final int year, final int monthOfYear, final int dayOfMonth) {
        setDate(DateTimeUtil.FromDate(year, monthOfYear, dayOfMonth));
    }

    protected void generateCalendar() {
        Integer dayOfMonthValue = -1;
        int dayOfMonthItemPosition = mSpDayOfMonth.getSelectedItemPosition();
        if (dayOfMonthItemPosition > -1) {
            int dayOfMonthAdapterCount = mSpDayOfMonthAdapter.getCount();
            if (dayOfMonthAdapterCount > dayOfMonthItemPosition) {
                dayOfMonthValue = mSpDayOfMonthAdapter.getItem(dayOfMonthItemPosition);
            }
        }
        int dayOfMonth = dayOfMonthValue.intValue();

        Integer monthOfYearValue = -1;
        int monthOfYearItemPosition = mSpMonthOfYear.getSelectedItemPosition();
        if (monthOfYearItemPosition > -1) {
            int monthOfYearAdapterCount = mSpMonthOfYearAdapter.getCount();
            if (monthOfYearAdapterCount > monthOfYearItemPosition) {
                monthOfYearValue = mSpMonthOfYearAdapter.getItem(monthOfYearItemPosition);
            }
        }
        int monthOfYear = monthOfYearValue.intValue();

        Integer yearValue = -1;
        int yearItemPosition = mSpYear.getSelectedItemPosition();
        if (yearItemPosition > -1) {
            int yearAdapterCount = mSpYearAdapter.getCount();
            if (yearAdapterCount > yearItemPosition) {
                yearValue = mSpYearAdapter.getItem(yearItemPosition);
            }
        }
        int year = yearValue.intValue();

        if (dayOfMonth > -1 && monthOfYear > -1 && year > -1)
            mCalendar = DateTimeUtil.FromDate(yearValue.intValue(), monthOfYearValue.intValue(), dayOfMonthValue.intValue());
        else
            mCalendar = null;
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    /* ------------- */
    /* -- Display -- */
    /* ------------- */

    public void setError(final String error) {

    }

    public class SpinnerDayOfMonthAdapter extends ArrayAdapter<Integer> {

        protected Context mContext;

        public SpinnerDayOfMonthAdapter(Context context, int resource) {
            super(context, resource);

            mContext = context;
        }

        public int getPositionByItem(Integer value) {
            int position = -1;
            if (value != null) {
                for (int itemPosition = 0; itemPosition < this.getCount(); itemPosition++) {
                    Integer selfValue = getItem(itemPosition);
                    if (selfValue != null) {
                        if (value.intValue() == selfValue.intValue()) {
                            position = itemPosition;
                            break;
                        }
                    }
                }
            }
            return position;
        }

        public void setInitialValue() {
            add(-1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getDropDownView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            AppCompatTextView tvItem = new AppCompatTextView(mContext);

            Integer itemValue = getItem(position);
            if (itemValue != null) {
                String itemDisplayValue;
                int value = itemValue.intValue();
                if (value < 0)
                    itemDisplayValue = ViewUtil.getResourceString(mContext, R.string.date_picker_select_day);
                else
                    itemDisplayValue = String.format("%02d", value);
                tvItem.setText(itemDisplayValue);
            }
            tvItem.setTextSize(16);
            tvItem.setSingleLine(true);
            tvItem.setTextColor(mContext.getResources().getColor(R.color.colorTextPrimaryDark));
            ViewUtil.setTextViewCenterAlignment(tvItem);
            tvItem.setPadding(ViewUtil.getPxFromDp(mContext, 7), ViewUtil.getPxFromDp(mContext, 5), ViewUtil.getPxFromDp(mContext, 7), ViewUtil.getPxFromDp(mContext, 5));

            return tvItem;
        }
    }

    public class SpinnerMonthOfYearAdapter extends ArrayAdapter<Integer> {

        protected Context mContext;

        public SpinnerMonthOfYearAdapter(Context context, int resource) {
            super(context, resource);

            mContext = context;
        }

        public int getPositionByItem(Integer value) {
            int position = -1;
            if (value != null) {
                for (int itemPosition = 0; itemPosition < this.getCount(); itemPosition++) {
                    Integer selfValue = getItem(itemPosition);
                    if (selfValue != null) {
                        if (value.intValue() == selfValue.intValue()) {
                            position = itemPosition;
                            break;
                        }
                    }
                }
            }
            return position;
        }

        public void setInitialValue() {
            add(-1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getDropDownView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            AppCompatTextView tvItem = new AppCompatTextView(mContext);

            Integer itemValue = getItem(position);
            if (itemValue != null) {
                String itemDisplayValue;
                int value = itemValue.intValue();
                if (value < 0) {
                    itemDisplayValue = ViewUtil.getResourceString(mContext, R.string.date_picker_select_month);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
                    calendar.set(Calendar.MONTH, value);

                    itemDisplayValue = simpleDateFormat.format(calendar.getTime());
                }
                tvItem.setText(itemDisplayValue);
            }
            tvItem.setTextSize(16);
            tvItem.setSingleLine(true);
            tvItem.setTextColor(mContext.getResources().getColor(R.color.colorTextPrimaryDark));
            tvItem.setPadding(ViewUtil.getPxFromDp(mContext, 7), ViewUtil.getPxFromDp(mContext, 5), ViewUtil.getPxFromDp(mContext, 7), ViewUtil.getPxFromDp(mContext, 5));

            return tvItem;
        }
    }

    public class SpinnerYearAdapter extends ArrayAdapter<Integer> {

        protected Context mContext;

        public SpinnerYearAdapter(Context context, int resource) {
            super(context, resource);

            mContext = context;
        }

        public int getPositionByItem(Integer value) {
            int position = -1;
            if (value != null) {
                for (int itemPosition = 0; itemPosition < this.getCount(); itemPosition++) {
                    Integer selfValue = getItem(itemPosition);
                    if (selfValue != null) {
                        if (value.intValue() == selfValue.intValue()) {
                            position = itemPosition;
                            break;
                        }
                    }
                }
            }
            return position;
        }

        public void setInitialValue() {
            add(-1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getDropDownView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            AppCompatTextView tvItem = new AppCompatTextView(mContext);
            Integer itemValue = getItem(position);
            if (itemValue != null) {
                String itemDisplayValue;
                int value = itemValue.intValue();
                if (value < 0)
                    itemDisplayValue = ViewUtil.getResourceString(mContext, R.string.date_picker_select_year);
                else
                    itemDisplayValue = String.valueOf(value);
                tvItem.setText(itemDisplayValue);
            }
            tvItem.setTextSize(16);
            tvItem.setSingleLine(true);
            tvItem.setTextColor(mContext.getResources().getColor(R.color.colorTextPrimaryDark));
            ViewUtil.setTextViewCenterAlignment(tvItem);
            tvItem.setPadding(ViewUtil.getPxFromDp(mContext, 7), ViewUtil.getPxFromDp(mContext, 5), ViewUtil.getPxFromDp(mContext, 7), ViewUtil.getPxFromDp(mContext, 5));

            return tvItem;
        }
    }
}
