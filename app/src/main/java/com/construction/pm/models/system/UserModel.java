package com.construction.pm.models.system;

import org.json.JSONException;

public class UserModel {
    protected int mId;
    protected String mUsername;
    protected String mEmail;

    public UserModel() {

    }

    public void setId(final int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setUsername(final String username) {
        mUsername = username;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setEmail(final String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    public static UserModel build(final org.json.JSONObject jsonObject) throws JSONException {
        UserModel userModel = new UserModel();

        if (!jsonObject.isNull("id"))
            userModel.setId(jsonObject.getInt("id"));
        if (!jsonObject.isNull("username"))
            userModel.setUsername(jsonObject.getString("username"));
        if (!jsonObject.isNull("email"))
            userModel.setEmail(jsonObject.getString("email"));

        return userModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        jsonObject.put("id", getId());
        jsonObject.put("username", getUsername());
        jsonObject.put("email", getEmail());

        return jsonObject;
    }
}
