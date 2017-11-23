package vn.vnpt.ansv.bts.notification;

/**
 * Created by ANSV on 11/23/2017.
 */

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import vn.vnpt.ansv.bts.ui.monitor.container.MonitorContainer;

import static android.content.Context.NOTIFICATION_SERVICE;
import static vn.vnpt.ansv.bts.ui.monitor.container.MonitorContainer.NOTIFICATION_ID;

public class ReplyReceiver extends BroadcastReceiver {
    public ReplyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {

            CharSequence id = remoteInput.getCharSequence(MonitorContainer.KEY_NOTIFICATION_REPLY);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Request received for ID: " + id);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }
}