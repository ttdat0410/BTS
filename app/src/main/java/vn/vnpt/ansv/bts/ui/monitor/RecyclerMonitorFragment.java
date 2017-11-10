package vn.vnpt.ansv.bts.ui.monitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
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
    private static final int ITEM_COUNT = 7;
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
        Log.i("0x00", stationId + " Station Id");
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        /*final List<Object> items = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; ++i) {
            items.add(new Object());
        }*/
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


    }

    /**
     * setupScannerAdapter
     *
     * Sets up ScannerAdapter for the recycler view.
     */
    private void setupRecyclerMonitorAdapter(List<MinSensorFullObj> listSensorObj) {
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        List<Object> devices = new ArrayList<>();
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

    // RecyclerMonitorView

    @Override
    public void showLoading() {
        Log.i("0x00", "loading...");
    }
}

