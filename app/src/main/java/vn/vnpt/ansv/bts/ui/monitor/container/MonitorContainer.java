package vn.vnpt.ansv.bts.ui.monitor.container;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSActivity;
import vn.vnpt.ansv.bts.utils.BTSToast;
import vn.vnpt.ansv.bts.utils.NotificationUtils;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MonitorContainer extends BTSActivity implements MonitorView {

    public static List<MinStationFullObj> listAllStation = null;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Inject
    MonitorPresenter presenter;

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

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

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        presenter.showNotification();
        presenter.showNotification();

        presenter.showNotification();

        presenter.showNotification();

        presenter.showNotification();


        presenter.connectMQTT();
        presenter.subscribeToMQTT("#", new MonitorPresenterImpl.MQTTCallback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void detectNoise() {
                Log.i("0x00", "has data");
                showToast(">>>", SuperToast.Background.RED);

            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.i("0x00", "oNRE");
                // checking for type intent filter
                if (intent.getAction().equals(Utils.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications

                    Log.i("0x00", "REGIS");


                } else if (intent.getAction().equals(Utils.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    showToast(message, SuperToast.Background.RED);

                    Log.i("0x00", "PUSH");

                }
            }
        };

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

        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Utils.REGISTRATION_COMPLETE));
//
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Utils.PUSH_NOTIFICATION));
//
//        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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

