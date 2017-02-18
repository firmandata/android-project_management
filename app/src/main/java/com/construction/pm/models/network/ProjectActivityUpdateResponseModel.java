package com.construction.pm.models.network;

import com.construction.pm.models.ProjectActivityUpdateModel;

public class ProjectActivityUpdateResponseModel extends SimpleResponseModel {

    protected ProjectActivityUpdateModel mProjectActivityUpdateModel;

    public ProjectActivityUpdateResponseModel() {
        super();
    }

    public ProjectActivityUpdateResponseModel(final Integer code, final String message, final ProjectActivityUpdateModel projectActivityUpdateModel) {
        super(code, message);
        mProjectActivityUpdateModel = projectActivityUpdateModel;
    }

    public void setProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        mProjectActivityUpdateModel = projectActivityUpdateModel;
    }

    public ProjectActivityUpdateModel getProjectActivityUpdateModel() {
        return mProjectActivityUpdateModel;
    }
}
