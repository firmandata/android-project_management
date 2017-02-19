package com.construction.pm.models;

public enum StatusTaskEnum {
    IN_PROGRESS("INPROGRESS"),
    COMING_DUE("COMINGDUE"),
    SHOULD_HAVE_STARTED("SHOULDHAVESTARTED"),
    LATE("LATE"),
    COMPLETED("COMPLETED");

    private final String mStatusTask;

    StatusTaskEnum(String value) {
        mStatusTask = value;
    }

    public String getValue() {
        return mStatusTask;
    }

    public static StatusTaskEnum fromString(String value) {
        if (value != null) {
            for (StatusTaskEnum eStatusTask : StatusTaskEnum.values()) {
                if (value.equals(eStatusTask.getValue())) {
                    return eStatusTask;
                }
            }
        }
        return null;
    }
}
