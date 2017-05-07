package com.construction.pm.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityDashboardModel;
import com.construction.pm.models.StatusTaskEnum;
import com.construction.pm.models.network.ProjectActivityDashboardResponseModel;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.project_activity.ProjectActivityDashboardView;

public class HomeView {
    protected Context mContext;

    protected RelativeLayout mHomeView;
    protected LinearLayout mProjectActivityDashboard;

    protected HomeListener mHomeListener;

    public HomeView(final Context context) {
        mContext = context;
    }

    public HomeView(final Context context, final RelativeLayout homeView) {
        this(context);

        initializeView(homeView);
    }

    public static HomeView buildHomeView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new HomeView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static HomeView buildHomeView(final Context context, final ViewGroup viewGroup) {
        return buildHomeView(context, R.layout.home_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout homeView) {
        mHomeView = homeView;

        mProjectActivityDashboard = (LinearLayout) mHomeView.findViewById(R.id.projectActivityDashboard);
    }

    public RelativeLayout getView() {
        return mHomeView;
    }

    public void setProjectActivityDashboardResponseModel(final ProjectActivityDashboardResponseModel projectActivityDashboardResponseModel) {
        mProjectActivityDashboard.removeAllViews();

        ProjectActivityDashboardModel[] inspectorProjectActivityDashboardModels = new ProjectActivityDashboardModel[5];
        inspectorProjectActivityDashboardModels[0] = new ProjectActivityDashboardModel();
        inspectorProjectActivityDashboardModels[0].setProjectMemberId(4);
        inspectorProjectActivityDashboardModels[0].setStatusTask(StatusTaskEnum.IN_PROGRESS);
        inspectorProjectActivityDashboardModels[0].setTotalTask(1);
        inspectorProjectActivityDashboardModels[1] = new ProjectActivityDashboardModel();
        inspectorProjectActivityDashboardModels[1].setProjectMemberId(4);
        inspectorProjectActivityDashboardModels[1].setStatusTask(StatusTaskEnum.COMING_DUE);
        inspectorProjectActivityDashboardModels[1].setTotalTask(2);
        inspectorProjectActivityDashboardModels[2] = new ProjectActivityDashboardModel();
        inspectorProjectActivityDashboardModels[2].setProjectMemberId(4);
        inspectorProjectActivityDashboardModels[2].setStatusTask(StatusTaskEnum.SHOULD_HAVE_STARTED);
        inspectorProjectActivityDashboardModels[2].setTotalTask(3);
        inspectorProjectActivityDashboardModels[3] = new ProjectActivityDashboardModel();
        inspectorProjectActivityDashboardModels[3].setProjectMemberId(4);
        inspectorProjectActivityDashboardModels[3].setStatusTask(StatusTaskEnum.LATE);
        inspectorProjectActivityDashboardModels[3].setTotalTask(4);
        inspectorProjectActivityDashboardModels[4] = new ProjectActivityDashboardModel();
        inspectorProjectActivityDashboardModels[4].setProjectMemberId(4);
        inspectorProjectActivityDashboardModels[4].setStatusTask(StatusTaskEnum.COMPLETED);
        inspectorProjectActivityDashboardModels[4].setTotalTask(5);
        //ProjectActivityDashboardModel[] projectActivityMonitoringDashboardModels = projectActivityDashboardResponseModel.getProjectActivityMonitoringDashboardModels();
        if (inspectorProjectActivityDashboardModels != null) {
            if (inspectorProjectActivityDashboardModels.length > 0) {
                ProjectActivityDashboardView projectActivityDashboardView = ProjectActivityDashboardView.buildProjectActivityDashboardView(mContext, null);
                projectActivityDashboardView.setProjectActivityDashboardItemListener(new ProjectActivityDashboardView.ProjectActivityDashboardItemListener() {
                    @Override
                    public void onProjectActivityDashboardItemClick(ProjectActivityDashboardModel projectActivityDashboardModel) {
                        if (mHomeListener != null)
                            mHomeListener.onDashboardInspectorItemClick(projectActivityDashboardModel);
                    }
                });
                projectActivityDashboardView.setLabel(ViewUtil.getResourceString(mContext, R.string.project_activity_dashboard_list_title_inspector));
                projectActivityDashboardView.setProjectActivityDashboardModel(inspectorProjectActivityDashboardModels);
                mProjectActivityDashboard.addView(projectActivityDashboardView.getView());
            }
        }

        ProjectActivityDashboardModel[] managerProjectActivityDashboardModels = new ProjectActivityDashboardModel[5];
        managerProjectActivityDashboardModels[0] = new ProjectActivityDashboardModel();
        managerProjectActivityDashboardModels[0].setProjectMemberId(4);
        managerProjectActivityDashboardModels[0].setStatusTask(StatusTaskEnum.IN_PROGRESS);
        managerProjectActivityDashboardModels[0].setTotalTask(1);
        managerProjectActivityDashboardModels[1] = new ProjectActivityDashboardModel();
        managerProjectActivityDashboardModels[1].setProjectMemberId(4);
        managerProjectActivityDashboardModels[1].setStatusTask(StatusTaskEnum.COMING_DUE);
        managerProjectActivityDashboardModels[1].setTotalTask(2);
        managerProjectActivityDashboardModels[2] = new ProjectActivityDashboardModel();
        managerProjectActivityDashboardModels[2].setProjectMemberId(4);
        managerProjectActivityDashboardModels[2].setStatusTask(StatusTaskEnum.SHOULD_HAVE_STARTED);
        managerProjectActivityDashboardModels[2].setTotalTask(3);
        managerProjectActivityDashboardModels[3] = new ProjectActivityDashboardModel();
        managerProjectActivityDashboardModels[3].setProjectMemberId(4);
        managerProjectActivityDashboardModels[3].setStatusTask(StatusTaskEnum.LATE);
        managerProjectActivityDashboardModels[3].setTotalTask(4);
        managerProjectActivityDashboardModels[4] = new ProjectActivityDashboardModel();
        managerProjectActivityDashboardModels[4].setProjectMemberId(4);
        managerProjectActivityDashboardModels[4].setStatusTask(StatusTaskEnum.COMPLETED);
        managerProjectActivityDashboardModels[4].setTotalTask(5);
        //ProjectActivityDashboardModel[] projectActivityDashboardModels = projectActivityDashboardResponseModel.getProjectActivityDashboardModels();
        if (managerProjectActivityDashboardModels != null) {
            if (managerProjectActivityDashboardModels.length > 0) {
                ProjectActivityDashboardView projectActivityDashboardView = ProjectActivityDashboardView.buildProjectActivityDashboardView(mContext, null);
                projectActivityDashboardView.setProjectActivityDashboardItemListener(new ProjectActivityDashboardView.ProjectActivityDashboardItemListener() {
                    @Override
                    public void onProjectActivityDashboardItemClick(ProjectActivityDashboardModel projectActivityDashboardModel) {
                        if (mHomeListener != null)
                            mHomeListener.onDashboardManagerItemClick(projectActivityDashboardModel);
                    }
                });
                projectActivityDashboardView.setLabel(ViewUtil.getResourceString(mContext, R.string.project_activity_dashboard_list_title_manager));
                projectActivityDashboardView.setProjectActivityDashboardModel(managerProjectActivityDashboardModels);
                mProjectActivityDashboard.addView(projectActivityDashboardView.getView());
            }
        }
    }

    public void setHomeListener(final HomeListener homeListener) {
        mHomeListener = homeListener;
    }

    public interface HomeListener {
        void onDashboardInspectorItemClick(ProjectActivityDashboardModel projectActivityDashboardModel);
        void onDashboardManagerItemClick(ProjectActivityDashboardModel projectActivityDashboardModel);
    }
}
