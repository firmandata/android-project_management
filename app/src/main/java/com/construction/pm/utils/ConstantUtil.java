package com.construction.pm.utils;

public class ConstantUtil {
    public static final String SETTING_USER_SERVER_URL = "http://sim-konstruksi.rafazsa.com";

    public static final int ACTIVITY_REQUEST_NOTIFICATION = 0;
    public static final int NOTIFICATION_ID_NOTIFICATION = 0;
    public static final int NOTIFICATION_ID_REQUEST_PASSWORD = 1;

    public static final String INTENT_RESULT_NOTIFICATION_MODEL = "NOTIFICATION_MODEL";
    public static final String INTENT_RESULT_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";
    public static final String INTENT_RESULT_PROJECT_ACTIVITY_MONITORING_MODEL = "PROJECT_ACTIVITY_MONITORING_MODEL";
    public static final String INTENT_RESULT_PROJECT_ACTIVITY_UPDATE_MODEL = "PROJECT_ACTIVITY_UPDATE_MODEL";
    public static final String INTENT_RESULT_PROJECT_STAGE_ASSIGN_COMMENT_MODEL = "PROJECT_STAGE_ASSIGN_COMMENT_MODEL";
    public static final String INTENT_RESULT_FILE_PATH = "FILE_PATH";

    public static final int INTENT_REQUEST_PERMISSION = 50;

    public static final int INTENT_REQUEST_NOTIFICATION_ACTIVITY = 100;

    public static final int INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_DETAIL = 200;
    public static final int INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_DETAIL_RESULT_UPDATE = 201;
    public static final int INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_DETAIL_RESULT_EDIT = 202;

    public static final int INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_FORM = 300;
    public static final int INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_FORM_RESULT_SAVED = 301;

    public static final int INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_FORM = 400;
    public static final int INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_FORM_RESULT_SAVED = 401;

    public static final int INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_FORM = 500;
    public static final int INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_FORM_RESULT_SAVED = 501;

    public static final int INTENT_REQUEST_INSPECTOR_DETAIL_ACTIVITY = 600;
    public static final int INTENT_REQUEST_INSPECTOR_DETAIL_ACTIVITY_RESULT_CHANGED = 601;

    public static final int INTENT_REQUEST_MANAGER_DETAIL_ACTIVITY = 700;
    public static final int INTENT_REQUEST_MANAGER_DETAIL_ACTIVITY_RESULT_CHANGED = 701;

    public static final int INTENT_REQUEST_CAMERA_ACTIVITY = 800;
    public static final int INTENT_REQUEST_CAMERA_ACTIVITY_RESULT_FILE = 801;

    public static final int INTENT_REQUEST_GALLERY_ACTIVITY = 900;
    public static final int INTENT_REQUEST_GALLERY_ACTIVITY_RESULT_FILE = 901;
}
