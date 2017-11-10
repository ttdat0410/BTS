package vn.vnpt.ansv.bts.ui.monitor;

import android.annotation.SuppressLint;
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
                    public void callback(EStatus eStatus, List<MinSensorFullObj> listSensorObj) {

                        if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS) {
                            recyclerMonitorAdapter.updateDataSet(listSensorObj);
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
    public void onResume() {
        super.onResume();
        Log.i("0x00", "RecyclerMonitorFragment REUSME" + stationId);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("0x00", "RecyclerMonitorFragment PAUSE" + stationId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBackground();
        Log.i("0x00", "RecyclerMonitorFragment onDestroy" + stationId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("0x00", " RecyclerMonitorFragment onDestroyView" + stationId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("0x00", "RecyclerMonitorFragment onStart" + stationId);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("0x00", "RecyclerMonitorFragment onStop" + stationId);
    }

    /**
     * setupScannerAdapter
     *
     * Sets up ScannerAdapter for the recycler view.
     */
    private void setupRecyclerMonitorAdapter(List<MinSensorFullObj> listSensorObj) {
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        recyclerMonitorAdapter = new RecyclerMonitorAdapter(listSensorObj);
        recyclerMonitorAdapter.updateDataSet(listSensorObj);
//        scannerAdapter.setListener(listener);
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

    @Override
    public void showLoading() {
        Log.i("0x00", "loading...");
    }
}

