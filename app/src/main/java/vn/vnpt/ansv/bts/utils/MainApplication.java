package vn.vnpt.ansv.bts.utils;

import android.app.Application;

import zemin.notification.NotificationDelegater;

/**
 * Created by ANSV on 11/23/2017.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initNotification();
    }

    private void initNotification() {

        NotificationDelegater.initialize(
                this,
                NotificationDelegater.LOCAL |
                        NotificationDelegater.GLOBAL |
                        NotificationDelegater.REMOTE);
    }
}