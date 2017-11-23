package vn.vnpt.ansv.bts.notification;

/**
 * Created by ANSV on 11/23/2017.
 */

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.ui.monitor.container.MonitorContainer;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Log.d("PLAYGROUND", "Details ID: " + getIntent().getIntExtra("EXTRA_DETAILS_ID", -1));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(MonitorContainer.NOTIFICATION_ID);
    }
}