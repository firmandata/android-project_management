package com.construction.pm.models;

public enum ActivityStatusEnum {
    IN_PROGRESS("INPROGRESS"),
    COMPLETED("COMPLETED"),
    ADDENDUM("ADDENDUM");

    private final String mActivityStatus;

    ActivityStatusEnum(String value) {
        mActivityStatus = value;
    }

    public String getValue() {
        return mActivityStatus;
    }

    public static ActivityStatusEnum fromString(String value) {
        if (value != null) {
            for (ActivityStatusEnum activityStatusEnum : ActivityStatusEnum.values()) {
                if (value.equals(activityStatusEnum.getValue())) {
                    return activityStatusEnum;
                }
            }
        }
        return null;
    }
}
