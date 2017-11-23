package vn.vnpt.ansv.bts.ui.monitor.container;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.johnpersano.supertoasts.SuperToast;

import java.util.List;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.notification.DetailActivity;
import vn.vnpt.ansv.bts.notification.NotificationUtils;
import vn.vnpt.ansv.bts.notification.ReplyReceiver;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSActivity;
import vn.vnpt.ansv.bts.utils.BTSToast;

import android.app.NotificationManager;
import android.app.PendingIntent;

import android.support.v4.app.RemoteInput;


/**
 * Created by ANSV on 11/9/2017.
 */

public class MonitorContainer extends BTSActivity implements MonitorView {

    public static List<MinStationFullObj> listAllStation = null;

    private NotificationUtils notificationUtils;

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    @Inject
    MonitorPresenter presenter;

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    public static void launch(Context context, List<MinStationFullObj> listStation) {
        listAllStation = listStation;
        Intent intent = new Intent(context, MonitorContainer.class);
        context.startActivity(intent);
    }

    public static int NOTIFICATION_ID = 1;
    public static final String KEY_NOTIFICATION_REPLY = "KEY_NOTIFICATION_REPLY";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        setTitle("");
        (((BTSApplication)getApplication()).getAppComponent()).inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        presenter.connectMQTT();
        presenter.subscribeToMQTT("#", new MonitorPresenterImpl.MQTTCallback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void detectNoise() {
                Log.i("0x00", "has data");
                showToast(">>>", SuperToast.Background.RED);

                Intent detailsIntent = new Intent(MonitorContainer.this, DetailActivity.class);
                detailsIntent.putExtra("EXTRA_DETAILS_ID", 42);
                PendingIntent detailsPendingIntent = PendingIntent.getActivity(
                        MonitorContainer.this,
                        0,
                        detailsIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                // Define PendingIntent for Reply action
                PendingIntent replyPendingIntent = null;
                // Call Activity on platforms that don't support DirectReply natively
                if (Build.VERSION.SDK_INT < 24) {
                    replyPendingIntent = detailsPendingIntent;
                } else { // Call BroadcastReceiver on platforms supporting DirectReply
                    replyPendingIntent = PendingIntent.getBroadcast(
                            MonitorContainer.this,
                            0,
                            new Intent(MonitorContainer.this, ReplyReceiver.class),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
                }

                // Create RemoteInput and attach it to Notification Action
                RemoteInput remoteInput = new RemoteInput.Builder(KEY_NOTIFICATION_REPLY)
                        .setLabel("Reply")
                        .build();
                NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_save, "Provide ID", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

                // NotificationCompat Builder takes care of backwards compatibility and
                // provides clean API to create rich notifications
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MonitorContainer.this)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Something important happened")
                        .setContentText("See the details")
                        .setAutoCancel(true)
                        .setContentIntent(detailsPendingIntent)
                        .addAction(replyAction)
                        .addAction(android.R.drawable.ic_menu_compass, "Details", detailsPendingIntent)
                        .addAction(android.R.drawable.ic_menu_directions, "Show Map", detailsPendingIntent);

                // Obtain NotificationManager system service in order to show the notification
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, mBuilder.build());



            }
        });

        new Thread(new Runnable() {
            public void run() {
                mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return presenter.setFragment(listAllStation, position);
                    }
                    @Override
                    public int getCount() {
                        return presenter.getCount(listAllStation);
                    }
                    @Override
                    public CharSequence getPageTitle(int position) {
                        return presenter.getPageTitle(listAllStation, position);
                    }
                });

                mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
                    @Override
                    public HeaderDesign getHeaderDesign(int page) {
                        return presenter.getHeaderDesign(listAllStation, page);

                    }
                });

                mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
                mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
                mViewPager.getPagerTitleStrip().setTextColor(getResources().getColor(R.color.sl_white));
                mViewPager.getPagerTitleStrip().setTextSize(30);
            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // add listener
//        mDelegater.addListener(mNotificationListener);

        // update notification count
//        mTvTotalCount.setText(String.valueOf(mDelegater.getNotificationCount()));
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private ProgressDialog dialog;
    @Override
    public void showLoading() {
        dialog = new ProgressDialog(MonitorContainer.this);
        dialog.setCancelable(false);
        dialog.setMax(100);
        dialog.setMessage(getResources().getString(R.string.dialog_verify_get_sensor_list));
        dialog.setTitle("");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }


    @Override
    public void hideLoading() {
        if (dialog.isShowing()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.hide();
                }
            }, 500);
        }
    }

    private AlertDialog.Builder builder = null;
    @Override
    public AlertDialog.Builder showAlert() {
        builder = new AlertDialog.Builder(MonitorContainer.this);
        builder.setTitle("Thông báo");
        builder.setMessage("Mất kết nối server.");
        builder.setPositiveButton(getResources().getString(R.string.dialog_try_again_button), null);
        builder.setCancelable(false);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showLoading();
                dialog.dismiss();
                MonitorPresenterImpl.isShowAlert = true;
            }
        });
        return builder;
    }

    @Override
    public void showToast(String content, int color) {
        if (dialog.isShowing()) {
            new BTSToast(this).showToast(content, color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listAllStation = null;
    }
}

