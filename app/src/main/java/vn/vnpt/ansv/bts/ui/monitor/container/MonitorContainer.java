package vn.vnpt.ansv.bts.ui.monitor.container;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
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
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.johnpersano.supertoasts.SuperToast;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

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
//                    showToast(message, SuperToast.Background.PURPLE);
                }
            }
        };
        // -----------------------
        setLayoutManager();
        // get item
        if (listAllStation.size() > 0) {
            showUpdate(true);
            stationId = listAllStation.get(0).getStationInfo().getStationId();
            recyclerMonitorPresenter.getData(stationId, new RecyclerMonitorPresenterImpl.MonitorCallback() {
                @Override
                public void callback(EStatus eStatus, List<MinSensorFullObj> listSensorObj, String gatewaySerial) {
                    if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS && gatewaySerial.length() > 0) {
                        setupRecyclerViewAdapter(listSensorObj);
                    } else if (eStatus == EStatus.NETWORK_FAILURE) {

                    }
                    showUpdate(false);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSnackbarUpdate = TSnackbar.make(findViewById(android.R.id.content), "Đang tải...", TSnackbar.LENGTH_INDEFINITE);
                    mSnackbarUpdate.setActionTextColor(Color.WHITE);
                    mSnackbarUpdate.setText("STATION_ID == NULL");
                    View snackbarView = mSnackbarUpdate.getView();
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                    params.setMargins(params.leftMargin,
                            56+16,
                            params.rightMargin,
                            params.bottomMargin);
                    snackbarView.setLayoutParams(params);
                    snackbarView.setBackground(getResources().getDrawable(R.drawable.snackbar_bg));
                    mSnackbarUpdate.show();
                }
            });showConnectedServer(true);
        }

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
//        mRecyclerViewOutside.addItemDecoration(new MaterialViewPagerHeaderDecorator());
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
        SnackbarManager.dismiss();
        if (mSnackbarUpdate != null && mSnackbarUpdate.isShown()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSnackbarUpdate.dismiss();
                }
            });
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listAllStation = null;
        presenter.unsubscribeToToptic(Utils.defaultTopic);
        stopBackground();
//        hideLoading();
        finish();
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

    /*private ProgressDialog dialog;
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
        if (dialog.isShowing() || dialog != null) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.hide();
                    dialog.dismiss();
                }
            }, 500);
        }
    }*/

    @Override
    public void startBackground(int intervalMS) {
        startDelayed(intervalMS, 0);
    }

    @Override
    public void stopBackground() {
        handler.removeCallbacks(runnableCode);
        Log.i("0x00", "MONITOR ALL STOP BACKGROUND AT: " + (new Date()));
    }

    /*private AlertDialog.Builder builder = null;
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
//                showLoading();
                dialog.dismiss();
//                MonitorPresenterImpl.isShowAlert = true;
            }
        });
        return builder;
    }*/

    /*@Override
    public void showToast(String content, int color) {
        if (dialog.isShowing()) {
            new BTSToast(this).showToast(content, color);
        }
    }*/

    private TSnackbar mSnackbarUpdate;
    @Override
    public void showUpdate(boolean isUpdate) {
        if (isUpdate) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSnackbarUpdate = TSnackbar.make(findViewById(android.R.id.content), "Đang tải...", TSnackbar.LENGTH_INDEFINITE);
                    mSnackbarUpdate.setActionTextColor(Color.WHITE);
                    mSnackbarUpdate.setText("đang cập nhật..");
                    View snackbarView = mSnackbarUpdate.getView();
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                    params.setMargins(params.leftMargin,
                            56+16,
                            params.rightMargin,
                            params.bottomMargin);
                    snackbarView.setLayoutParams(params);
                    snackbarView.setBackground(getResources().getDrawable(R.drawable.snackbar_bg));
                    mSnackbarUpdate.show();
                }
            });

        } else {
            if (mSnackbarUpdate != null && mSnackbarUpdate.isShown()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSnackbarUpdate.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void showSnackBar(EStatus msServerState, String status) {
        Snackbar snackbar = Snackbar
                .make(mViewPager, status, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        if (msServerState == EStatus.GET_STATIONS_SUCCESS) {
            snackbarView.setBackgroundColor(getResources().getColor(R.color.sl_terbium_green));
            this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
//                    updateRecycleView();
                }
            });
        } else {
            snackbarView.setBackgroundColor(getResources().getColor(R.color.sl_red_orange));
        }
        snackbar.show();
    }

    @Override
    public void showConnectedServer(boolean isConnect) {
        if (!isConnect) {
            SnackbarManager.show(
                    com.nispok.snackbar.Snackbar.with(MonitorContainer.this)
                            .position(com.nispok.snackbar.Snackbar.SnackbarPosition.BOTTOM)
                            .text("Mất kết nối server...")
                            .textColor(Color.parseColor("#AFFFFFFF"))
                            .color(Color.parseColor("#333333"))
                            .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                            .actionLabel("KIỂM TRA MẠNG")
                            .actionColor(Color.parseColor("#ffe65100"))
                            .actionListener(new ActionClickListener() {
                                @Override
                                public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                                    Intent enableNetwork = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                                    MonitorContainer.this.startActivityForResult(enableNetwork, 0);
                                }
                            })
            );

        } else {
            SnackbarManager.dismiss();
        }
    }
}

