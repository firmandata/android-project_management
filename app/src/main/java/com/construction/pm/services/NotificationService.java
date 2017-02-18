package com.construction.pm.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.construction.pm.R;
import com.construction.pm.activities.BootstrapActivity;
import com.construction.pm.activities.MainActivity;
import com.construction.pm.activities.NotificationActivity;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;

public class NotificationService extends Service implements NotificationWorker.NotificationRoutineListener {

    protected NotificationWorker mNotificationWorker;

    protected Messenger mMessengerClient;
    protected NotificationMessageHandler mNotificationMessageHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("NotificationService", "onCreate");

        mNotificationWorker = new NotificationWorker(this);
        mNotificationWorker.setNotificationHandlerListener(this);
        mNotificationWorker.setDaemon(true);

        mNotificationMessageHandler = new NotificationMessageHandler(this);
        mMessengerClient = new Messenger(mNotificationMessageHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationService", "onStartCommand");

        if (!mNotificationWorker.isAlive())
            mNotificationWorker.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("NotificationService", "onBind");
        return mMessengerClient.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("NotificationService", "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onNotificationRoutineGetNew(NotificationModel[] notificationModels) {
        // -- Prepare NotificationManager --
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // -- Broadcast new NotificationModels message --
        int sentCount = mNotificationMessageHandler.sendNotificationModels(notificationModels);
        if (sentCount > 0) {
            // -- Clear current notification --
            notificationManager.cancel(ConstantUtil.NOTIFICATION_ID_NOTIFICATION);
            return;
        }

        // -- Prepare SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);

        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();
        ProjectMemberModel projectMemberModel = null;
        if (sessionLoginModel != null)
            projectMemberModel = sessionLoginModel.getProjectMemberModel();

        // -- Prepare NotificationPersistent --
        NotificationPersistent notificationPersistent = new NotificationPersistent(this);

        // -- Get unread NotificationModels --
        int unReadNotificationModelCount = 0;
        NotificationModel[] unReadNotificationModels = null;
        if (projectMemberModel != null) {
            try {
                unReadNotificationModelCount = notificationPersistent.getUnreadNotificationModelCount(projectMemberModel.getProjectMemberId());
                unReadNotificationModels = notificationPersistent.getUnreadNotificationModels(projectMemberModel.getProjectMemberId(), 5);
            } catch (PersistenceError persistenceError) {
            }
        }

        // -- Not create notification if no unread NotificationModel --
        if (unReadNotificationModelCount == 0)
            return;
        if (unReadNotificationModels == null)
            return;
        if (unReadNotificationModels.length == 0)
            return;

        // -- Get notification ringtone --
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // -- Build notification --
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setSound(soundUri);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVibrate(new long[]{ 0, 500, 100, 500 });
        notificationBuilder.setContentTitle(ViewUtil.getResourceString(this, R.string.service_notification_title));
        if (unReadNotificationModelCount > 1) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            for (NotificationModel notificationModel : unReadNotificationModels) {
                inboxStyle.addLine(notificationModel.getNotificationMessage());
            }
            inboxStyle.setSummaryText(ViewUtil.getResourceString(this, R.string.service_notification_subtitle, StringUtil.numberFormat(unReadNotificationModelCount)));
            notificationBuilder.setStyle(inboxStyle);
            notificationBuilder.setContentText(ViewUtil.getResourceString(this, R.string.service_notification_subtitle, StringUtil.numberFormat(unReadNotificationModelCount)));
        } else if (unReadNotificationModelCount == 1) {
            notificationBuilder.setContentText(unReadNotificationModels[0].getNotificationMessage());
            notificationBuilder.setSubText(DateTimeUtil.ToDateTimeDisplayString(unReadNotificationModels[0].getNotificationDate()));
        }

        // -- Notification for activity response --
        Intent notificationIntent = null;
        if (unReadNotificationModelCount > 1) {
            // -- Show MainActivity with notification fragment --
            notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra(MainActivity.INTENT_PARAM_SHOW_DEFAULT_FRAGMENT, MainActivity.INTENT_PARAM_SHOW_FRAGMENT_NOTIFICATION);
        } else if (unReadNotificationModelCount == 1) {
            // -- Get NotificationModel in JSON format --
            String notificationModelJson = null;
            try {
                org.json.JSONObject notificationModelJsonObject = unReadNotificationModels[0].build();
                notificationModelJson = notificationModelJsonObject.toString(0);
            } catch (org.json.JSONException ex) {
            }

            // -- Show NotificationActivity --
            notificationIntent = new Intent(this, NotificationActivity.class);
            notificationIntent.putExtra(NotificationActivity.INTENT_PARAM_NOTIFICATION_FROM_NOTIFICATION_SERVICE, true);
            if (notificationModelJson != null)
                notificationIntent.putExtra(NotificationActivity.INTENT_PARAM_NOTIFICATION_MODEL, notificationModelJson);
        }
        if (notificationIntent != null) {
            PendingIntent contentIntent = PendingIntent.getActivity(this, ConstantUtil.ACTIVITY_REQUEST_NOTIFICATION, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(contentIntent);
        }

        // -- Add as notification --
        notificationManager.notify(ConstantUtil.NOTIFICATION_ID_NOTIFICATION, notificationBuilder.build());

        // -- Add unsent NotificationModels in NotificationMessageHandler --
        mNotificationMessageHandler.addUnSentNotificationNew(notificationModels);
    }

    @Override
    public void onNotificationRoutineRequestLogin(SessionLoginModel sessionLoginModel) {
        // -- Prepare NotificationManager --
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // -- Clear current notification --
        notificationManager.cancel(ConstantUtil.NOTIFICATION_ID_NOTIFICATION);

        // -- Broadcast SessionLoginModel for RequestLogin message --
        int sentCount = mNotificationMessageHandler.sendRequestLogin(sessionLoginModel);
        if (sentCount > 0)
            return;

        // -- Get notification ringtone --
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // -- Build notification --
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setSound(soundUri);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVibrate(new long[]{ 0, 500, 100, 500 });
        notificationBuilder.setContentTitle(ViewUtil.getResourceString(this, R.string.app_name));
        notificationBuilder.setContentText(ViewUtil.getResourceString(this, R.string.service_notification_need_login));

        // -- Notification for activity response --
        Intent notificationIntent = new Intent(this, BootstrapActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, ConstantUtil.ACTIVITY_REQUEST_NOTIFICATION, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        // -- Add as notification --
        notificationManager.notify(ConstantUtil.NOTIFICATION_ID_REQUEST_PASSWORD, notificationBuilder.build());
    }

    @Override
    public void onNotificationRoutineStop() {
        // -- Broadcast Stop message --
        mNotificationMessageHandler.sendStop();

        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.i("NotificationService", "onDestroy");

        mNotificationWorker.interrupt();

        super.onDestroy();
    }
}
