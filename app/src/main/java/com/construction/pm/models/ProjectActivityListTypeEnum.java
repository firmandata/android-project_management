package com.construction.pm.models;

public enum ProjectActivityListTypeEnum {
    INSPECTOR("INSPECTOR"),
    MANAGER("MANAGER");

    private final String mProjectActivityListType;

    ProjectActivityListTypeEnum(String value) {
        mProjectActivityListType = value;
    }

    public String getValue() {
        return mProjectActivityListType;
    }

    public static ProjectActivityListTypeEnum fromString(String value) {
        if (value != null) {
            for (ProjectActivityListTypeEnum projectActivityListTypeEnum : ProjectActivityListTypeEnum.values()) {
                if (value.equals(projectActivityListTypeEnum.getValue())) {
                    return projectActivityListTypeEnum;
                }
            }
        }
        return null;
    }
}
