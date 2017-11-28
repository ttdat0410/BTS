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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.github.johnpersano.supertoasts.SuperToast;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.objects.MinSensorFullObj;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSActivity;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorAdapter;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorPresenter;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorPresenterImpl;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorView;
import vn.vnpt.ansv.bts.utils.BTSToast;
import vn.vnpt.ansv.bts.utils.EStatus;
import vn.vnpt.ansv.bts.utils.TypeCell;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MonitorContainer extends BTSActivity implements MonitorView, RecyclerMonitorView {

    public static List<MinStationFullObj> listAllStation = null;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private int stationId = -1;
    @Inject
    MonitorPresenter presenter;

    @Inject
    RecyclerMonitorPresenter recyclerMonitorPresenter;

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @BindView(R.id.recyclerViewOutside)
    RecyclerView mRecyclerViewOutside;

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
        recyclerMonitorPresenter.setView(this);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
//            setSupportActionBar(toolbar);
            toolbar.setVisibility(View.GONE);
        }
        // MQTT -----------------
        presenter.connectMQTT();
        presenter.subscribeToTopic(Utils.defaultTopic, new MonitorPresenterImpl.MQTTCallback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void detectNoise(String message) {
                presenter.showNotification(message);
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Utils.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    showToast(message, SuperToast.Background.PURPLE);
                }
            }
        };
        // -----------------------
        setLayoutManager();
        // get item
        stationId = listAllStation.get(0).getStationInfo().getStationId();
        recyclerMonitorPresenter.getData(stationId, new RecyclerMonitorPresenterImpl.MonitorCallback() {
            @Override
            public void callback(EStatus eStatus, List<MinSensorFullObj> listSensorObj, String gatewaySerial) {
                if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS && gatewaySerial.length() > 0) {
                    setupRecyclerViewAdapter(listSensorObj);
                } else if (eStatus == EStatus.NETWORK_FAILURE) {
                }
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

    private void setLayoutManager() {
        mRecyclerViewOutside.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerViewOutside.setHasFixedSize(true);
        startBackground(15000);
    }

    /**
     * setupScannerAdapter
     *
     * Sets up ScannerAdapter for the recycler view.
     */
    private RecyclerMonitorAdapter recyclerMonitorAdapter;
    private void setupRecyclerViewAdapter(final List<MinSensorFullObj> listSensorObj) {
        mRecyclerViewOutside.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        recyclerMonitorAdapter = new RecyclerMonitorAdapter(listSensorObj, TypeCell.OUTSIDE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.sort(listSensorObj, Utils.comparatorWithSensorTypeId);
                Collections.sort(listSensorObj, Utils.comparatorWithSensorName);
                recyclerMonitorAdapter.updateDataSet(listSensorObj);
            }
        }, 500);
        mRecyclerViewOutside.setAdapter(recyclerMonitorAdapter);
    }

    private Runnable runnableCode = null;
    private Handler handler = new Handler();
    void startDelayed(final int intervalMS, int delayMS) {

        runnableCode = new Runnable() {
            @Override
            public void run() {
                Log.i("0x00", "running... ");
                handler.postDelayed(runnableCode, intervalMS);
                recyclerMonitorPresenter.getData(stationId, new RecyclerMonitorPresenterImpl.MonitorCallback() {
                    @Override
                    public void callback(EStatus eStatus, final List<MinSensorFullObj> listSensorObj, String gatewaySerial) {
                        if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Collections.sort(listSensorObj, Utils.comparatorWithSensorTypeId);
                                    Collections.sort(listSensorObj, Utils.comparatorWithSensorName);
                                    recyclerMonitorAdapter.updateDataSet(listSensorObj);
                                }
                            }, 200);

                        } else if (eStatus == EStatus.NETWORK_FAILURE) {
                        }
                    }
                });

            }
        };
        handler.postDelayed(runnableCode, delayMS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Utils.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listAllStation = null;
        presenter.unsubscribeToToptic(Utils.defaultTopic);
        stopBackground();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {
            case 0:
                break;

            default:
                super.onActivityResult(requestCode, responseCode, intent);
        }
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

    @Override
    public void startBackground(int intervalMS) {
        startDelayed(intervalMS, 0);
    }

    @Override
    public void stopBackground() {
        handler.removeCallbacks(runnableCode);
        Log.i("0x00", "MONITOR ALL STOP BACKGROUND AT: " + (new Date()));
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
}

