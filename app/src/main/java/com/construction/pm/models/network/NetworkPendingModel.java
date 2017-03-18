package com.construction.pm.models.network;

import com.construction.pm.networks.webapi.WebApiResponse;

import java.util.Calendar;

public class NetworkPendingModel {

    public enum ECommandType {
        NOTIFICATION_RECEIVED(0), NOTIFICATION_READ(1),
        PROJECT_ACTIVITY_UPDATE_SAVE(2);

        private final int mCommandType;

        ECommandType(int value) {
            mCommandType = value;
        }

        public int getValue() {
            return mCommandType;
        }

        public static ECommandType fromInt(int value) {
            for (ECommandType eCommandType : ECommandType.values()) {
                if (value == eCommandType.getValue()) {
                    return eCommandType;
                }
            }
            return null;
        }
    }

    protected long mId;
    protected ECommandType mType;
    protected String mCommandKey;
    protected String mCommand;
    protected boolean mSent;
    protected Integer mProjectMemberId;
    protected Calendar mCreatedDate;
    protected Calendar mUpdatedDate;

    protected WebApiResponse mWebApiResponse;

    public NetworkPendingModel() {

    }

    public NetworkPendingModel(final Integer projectMemberId) {
        this();

        mProjectMemberId = projectMemberId;
    }

    public NetworkPendingModel(final Integer projectMemberId, final WebApiResponse webApiResponse, final ECommandType type) {
        this(projectMemberId);

        generateCommandFromWebApiResponse(webApiResponse);
        mType = type;
    }

    public void setId(final long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setType(final ECommandType type) {
        mType = type;
    }

    public ECommandType getType() {
        return mType;
    }

    public void setCommandKey(final String commandKey) {
        mCommandKey = commandKey;
    }

    public String getCommandKey() {
        return mCommandKey;
    }

    public void setCommand(final String command) {
        mCommand = command;
    }

    public String getCommand() {
        return mCommand;
    }

    public void setSent(final boolean sent) {
        mSent = sent;
    }

    public boolean isSent() {
        return mSent;
    }

    public void setProjectMemberId(final Integer projectMemberId) {
        mProjectMemberId = projectMemberId;
    }

    public Integer getProjectMemberId() {
        return mProjectMemberId;
    }

    public void setCreatedDate(final Calendar createdDate) {
        mCreatedDate = createdDate;
    }

    public Calendar getCreatedDate() {
        return mCreatedDate;
    }

    public void setUpdatedDate(final Calendar updatedDate) {
        mUpdatedDate = updatedDate;
    }

    public Calendar getUpdatedDate() {
        return mUpdatedDate;
    }

    protected void generateCommandFromWebApiResponse(final WebApiResponse webApiResponse) {
        mWebApiResponse = webApiResponse;

        if (mWebApiResponse != null) {
            // -- Set command from WebApiResponse --
            try {
                mCommand = mWebApiResponse.buildWebApiRequestCommand().toString(0);
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }
    }

    public WebApiResponse getWebApiResponse() {
        return mWebApiResponse;
    }
}
