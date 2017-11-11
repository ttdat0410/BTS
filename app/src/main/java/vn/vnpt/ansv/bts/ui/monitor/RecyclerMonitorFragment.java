package vn.vnpt.ansv.bts.ui.monitor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
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
import vn.vnpt.ansv.bts.utils.EStatus;

/**
 * Created by ANSV on 11/10/2017.
 */
@SuppressLint("ValidFragment")
public class RecyclerMonitorFragment  extends Fragment implements RecyclerMonitorView {
    private static final boolean GRID_LAYOUT = false;
    private RecyclerMonitorAdapter recyclerMonitorAdapter;

    @Inject
    RecyclerMonitorPresenter presenter;

    @Inject
    PreferenceManager preferenceManager;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public static RecyclerMonitorFragment newInstance(int stationId) {
        return new RecyclerMonitorFragment(stationId);
    }

    private int stationId;
    @SuppressLint("ValidFragment")
    public RecyclerMonitorFragment(int stationId) {
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
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        presenter.getData(stationId, new RecyclerMonitorPresenterImpl.MonitorCallback() {
            @Override
            public void callback(EStatus eStatus, List<MinSensorFullObj> listSensorObj) {

                if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS) {
                    setupRecyclerMonitorAdapter(listSensorObj);
                }
            }
        });
        runBackground(15000);
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
                    public void callback(EStatus eStatus, final List<MinSensorFullObj> listSensorObj) {

                        if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerMonitorAdapter.updateDataSet(listSensorObj);
                                }
                            }, 200);
                        }
                    }
                });
                Log.i("0x00", "RUNNING.... " + (new Date()));
            }
        };
        handler.postDelayed(runnableCode, delayMS);
    }

    void runBackground(final int intervalMS) {
        startDelayed(intervalMS, 0);
    }

    void stopBackground() {
        handler.removeCallbacks(runnableCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBackground();
        BTSPreferences prefs = preferenceManager.getPreferences();
        prefs.apiKey = "";
        prefs.userId = "";
        preferenceManager.setPreferences(prefs);
    }

    /**
     * setupScannerAdapter
     *
     * Sets up ScannerAdapter for the recycler view.
     */
    private void setupRecyclerMonitorAdapter(final List<MinSensorFullObj> listSensorObj) {
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        recyclerMonitorAdapter = new RecyclerMonitorAdapter(listSensorObj);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerMonitorAdapter.updateDataSet(listSensorObj);
            }
        }, 500);
        mRecyclerView.setAdapter(recyclerMonitorAdapter);
    }

//    private RecyclerMonitorAdapter.OnDeviceItemClickListener listener = new RecyclerMonitorAdapter.OnDeviceItemClickListener() {
//        @Override
//        public void onDeviceItemClick(View view, Objects device) {
//            Intent intent = new Intent(ScannerActivity.this, DemosSelectionActivity.class);
//            intent.putExtra(ThunderBoardConstants.EXTRA_DEVICE_NAME, device.getName());
//            intent.putExtra(ThunderBoardConstants.EXTRA_DEVICE_ADDRESS, device.getAddress());
//            startActivity(intent);
//        }
//    };

}

