package vn.vnpt.ansv.bts.ui.monitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
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
import vn.vnpt.ansv.bts.ui.BTSPreferences;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
import vn.vnpt.ansv.bts.ui.splash.SplashPresenterImpl;
import vn.vnpt.ansv.bts.utils.EStatus;
import vn.vnpt.ansv.bts.utils.TypeCell;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/10/2017.
 */
@SuppressLint("ValidFragment")
public class RecyclerMonitorFragment  extends Fragment implements RecyclerMonitorView {

    private static final String TAG = RecyclerMonitorFragment.class.getSimpleName();

    private static final boolean GRID_LAYOUT = false;
    private RecyclerMonitorAdapter recyclerMonitorAdapter;

    @Inject
    RecyclerMonitorPresenter presenter;

    @Inject
    PreferenceManager preferenceManager;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private SplashPresenterImpl.GetStationCallback callback;

    public static RecyclerMonitorFragment newInstance(int stationId, SplashPresenterImpl.GetStationCallback callback) {
        return new RecyclerMonitorFragment(stationId, callback);
    }

    private int stationId;
    @SuppressLint("ValidFragment")
    public RecyclerMonitorFragment(int stationId, SplashPresenterImpl.GetStationCallback callback) {
        this.callback = callback;
        this.stationId = stationId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        (((BTSApplication) getActivity().getApplicationContext()).getAppComponent()).inject(this);
        presenter.setView(this);
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
//        showUpdate(true);
        mRecyclerView.setHasFixedSize(true);
        presenter.getData(stationId, new RecyclerMonitorPresenterImpl.MonitorCallback() {
            @Override
            public void callback(EStatus eStatus, List<MinSensorFullObj> listSensorObj, String gatewaySerial) {

                if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS && gatewaySerial.length() > 0) {
                    setupRecyclerMonitorAdapter(listSensorObj);
                    callback.callback(EStatus.GET_SENSOR_OBJ_SUCCESS);
                } else if (eStatus == EStatus.NETWORK_FAILURE) {
                    callback.callback(EStatus.NETWORK_FAILURE);
                }
//                showUpdate(false);

            }
        });

        startBackground(15000);
    }

    private Runnable runnableCode = null;
    private Handler handler = new Handler();
    void startDelayed(final int intervalMS, int delayMS) {
        runnableCode = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnableCode, intervalMS);
                presenter.getData(stationId, new RecyclerMonitorPresenterImpl.MonitorCallback() {
                    @Override
                    public void callback(EStatus eStatus, final List<MinSensorFullObj> listSensorObj, String gatewaySerial) {
                        if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS) {

                            if (recyclerMonitorAdapter != null) {

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Collections.sort(listSensorObj, Utils.comparatorWithSensorTypeId);
                                        Collections.sort(listSensorObj, Utils.comparatorWithSensorName);
                                        recyclerMonitorAdapter.updateDataSet(listSensorObj);
                                    }
                                }, 200);
                                callback.callback(EStatus.GET_SENSOR_OBJ_SUCCESS);
                            } else {

                            }

                        } else if (eStatus == EStatus.NETWORK_FAILURE) {
                            callback.callback(EStatus.NETWORK_FAILURE);
                        }
                    }
                });

            }
        };
        handler.postDelayed(runnableCode, delayMS);
        Log.i(TAG, "START BACKGROUND AT: " + (new Date()));
    }

    @Override
    public void startBackground(final int intervalMS) {
        startDelayed(intervalMS, 0);
    }

    @Override
    public void stopBackground() {
        handler.removeCallbacks(runnableCode);
        Log.i(TAG, "STOP BACKGROUND AT: " + (new Date()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBackground();
        BTSPreferences prefs = preferenceManager.getPreferences();
        prefs.apiKey = "";
        prefs.userId = "";
        prefs.roleId = "";
        preferenceManager.setPreferences(prefs);
    }

    /**
     * setupScannerAdapter
     *
     * Sets up ScannerAdapter for the recycler view.
     */
    private void setupRecyclerMonitorAdapter(final List<MinSensorFullObj> listSensorObj) {
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        recyclerMonitorAdapter = new RecyclerMonitorAdapter(listSensorObj, TypeCell.INSIDE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.sort(listSensorObj, Utils.comparatorWithSensorTypeId);
                Collections.sort(listSensorObj, Utils.comparatorWithSensorName);
                recyclerMonitorAdapter.updateDataSet(listSensorObj);
            }
        }, 500);
        mRecyclerView.setAdapter(recyclerMonitorAdapter);
    }

    private TSnackbar mSnackbarUpdate;
    @Override
    public void showUpdate(boolean isUpdate) {
        /*if (getActivity() != null) {
            if (isUpdate) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSnackbarUpdate = TSnackbar.make(getActivity().findViewById(android.R.id.content), "Đang tải...", TSnackbar.LENGTH_INDEFINITE);
                        mSnackbarUpdate.setActionTextColor(Color.WHITE);
                        mSnackbarUpdate.setText("đang cập nhật..");
                        View snackbarView = mSnackbarUpdate.getView();
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                        params.setMargins(params.leftMargin,
                                56 + 16,
                                params.rightMargin,
                                params.bottomMargin);
                        snackbarView.setLayoutParams(params);
                        snackbarView.setBackground(getResources().getDrawable(R.drawable.snackbar_bg));
                        mSnackbarUpdate.show();
                    }
                });

            } else {
                if (mSnackbarUpdate != null && mSnackbarUpdate.isShown()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSnackbarUpdate.dismiss();
                        }
                    });
                }
            }
        }*/
    }

    @Override
    public void showConnectedServer(boolean isConnect) {
        /*if (!isConnect) {
            SnackbarManager.show(
                    com.nispok.snackbar.Snackbar.with(getActivity())
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
                                    getActivity().startActivityForResult(enableNetwork, 0);
                                }
                            })
            );

        } else {
            SnackbarManager.dismiss();
        }*/
    }
}

