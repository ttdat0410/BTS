package vn.vnpt.ansv.bts.ui.monitor.container;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.List;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSActivity;
import vn.vnpt.ansv.bts.utils.BTSToast;
import zemin.notification.NotificationBuilder;
import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationEntry;
import zemin.notification.NotificationGlobal;
import zemin.notification.NotificationListener;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationRemote;
import zemin.notification.NotificationView;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MonitorContainer extends BTSActivity implements MonitorView {

    public static List<MinStationFullObj> listAllStation = null;

    @Inject
    MonitorPresenter presenter;

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @BindView(R.id.nv)
    NotificationView nv;


    private NotificationDelegater mDelegater;
    private NotificationRemote mRemote;
    private NotificationLocal mLocal;
    private NotificationGlobal mGlobal;

    public static void launch(Context context, List<MinStationFullObj> listStation) {
        listAllStation = listStation;
        Intent intent = new Intent(context, MonitorContainer.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        setTitle("");
        (((BTSApplication)getApplication()).getAppComponent()).inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        mDelegater = NotificationDelegater.getInstance();
//        mRemote = mDelegater.remote();
        mLocal = mDelegater.local();
//        mGlobal = mDelegater.global();
//        // enable global view && board
//        mGlobal.setViewEnabled(true);
//        mGlobal.setBoardEnabled(true);
        mLocal.setView(nv);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        presenter.connectMQTT();
        presenter.subscribeToMQTT("AD54B847", new MonitorPresenterImpl.MQTTCallback() {
            @Override
            public void detectNoise() {
                Log.i("0x00", "has data");
                String title = getNextTitle();
                String text = getNextText();

                NotificationBuilder.V2 builder = NotificationBuilder.remote()
                        .setSmallIconResource(R.mipmap.ic_sound_active)
                        .setTicker(title + ": " + text)
                        .setTitle(title)
                        .addAction(zemin.notification.R.drawable.clear_button, "yes", null, null)
                        .setText(text);

                mDelegater.send(builder.getNotification());
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
        mDelegater.addListener(mNotificationListener);

        // update notification count
//        mTvTotalCount.setText(String.valueOf(mDelegater.getNotificationCount()));
    }

    @Override
    protected void onPause() {
        super.onPause();

        // remove listener
        mDelegater.removeListener(mNotificationListener);
    }

    private final NotificationListener mNotificationListener = new NotificationListener() {
        @Override
        public void onArrival(NotificationEntry entry) {
            updateNotificationCount(entry);
        }

        @Override
        public void onCancel(NotificationEntry entry) {
            updateNotificationCount(entry);
        }

        @Override
        public void onUpdate(NotificationEntry entry) {
        }
    };

    private void updateNotificationCount(NotificationEntry entry) {
        if (entry.isSentToRemote()) {
//            mTvRemoteCount.setText(String.valueOf(mRemote.getNotificationCount()));
        }
        if (entry.isSentToLocalView()) {
//            mTvLocalCount.setText(String.valueOf(mLocal.getNotificationCount()));
        }
        if (entry.isSentToGlobalView()) {
//            mTvGlobalCount.setText(String.valueOf(mGlobal.getNotificationCount()));
        }
//        mTvTotalCount.setText(String.valueOf(mDelegater.getNotificationCount()));
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

    ///
    private int mTitleIdx;
    private int mTextIdx;


    private static final String[] mTitleSet = new String[] {
            "Guess Who",
            "Meet Android",
            "I'm a Notification",
    };

    private static final String[] mTextSet = new String[] {
            "hello world",
            "welcome to the android world",
            "welcome to code samples for zemin-notification. " +
                    "Here you can browse sample code and learn how to send, show and cancel a notification.",
            "zemin-notification library is available on GitHub under the Apache License v2.0. " +
                    "You are free to make use of it.",
    };

    private String getNextTitle() {
        if (mTitleIdx == mTitleSet.length) {
            mTitleIdx = 0;
        }
        return mTitleSet[mTitleIdx++];
    }

    private String getNextText() {
        if (mTextIdx == mTextSet.length) {
            mTextIdx = 0;
        }
        return mTextSet[mTextIdx++];
    }
}

