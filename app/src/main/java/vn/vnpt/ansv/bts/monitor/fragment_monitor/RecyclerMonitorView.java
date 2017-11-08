package vn.vnpt.ansv.bts.monitor.fragment_monitor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
/**
 * Created by ANSV on 11/7/2017.
 */

public class RecyclerMonitorView extends Fragment {
    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 7;
    private RecyclerMonitorAdapter recyclerMonitorAdapter;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public static RecyclerMonitorView newInstance() {
        return new RecyclerMonitorView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final List<Object> items = new ArrayList<>();

        for (int i = 0; i < ITEM_COUNT; ++i) {
            items.add(new Object());
        }

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);
        setupRecyclerMonitorAdapter(items);
    }

    /**
     * setupScannerAdapter
     *
     * Sets up ScannerAdapter for the recycler view.
     */
    private void setupRecyclerMonitorAdapter(List<Object> items) {
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        List<Object> devices = new ArrayList<>();
        recyclerMonitorAdapter = new RecyclerMonitorAdapter(items);
        recyclerMonitorAdapter.updateDataSet(items);
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
}
