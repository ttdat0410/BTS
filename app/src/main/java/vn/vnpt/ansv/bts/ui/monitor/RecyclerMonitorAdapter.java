package vn.vnpt.ansv.bts.ui.monitor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.objects.MinSensorFullObj;

/**
 * Created by ANSV on 11/9/2017.
 */

public class RecyclerMonitorAdapter extends RecyclerView.Adapter<RecyclerMonitorAdapter.DeviceHolder> {

    private List<MinSensorFullObj> dataSet;
    private OnDeviceItemClickListener listener;

    public RecyclerMonitorAdapter(List<MinSensorFullObj> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        } else {
            return dataSet.size();
        }
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_device, parent, false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {


//        final Object device = dataSet.get(position);
        holder.deviceName.setText(dataSet.get(position).getSensorInfo().getSensorName());
        holder.deviceAddress.setText("AA");



//        holder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onDeviceItemClick(v, device);
//                }
//            }
//        });
    }



    public void updateDataSet(List<MinSensorFullObj> devices) {
        this.dataSet = devices;
        notifyDataSetChanged();
    }

    public void setListener(OnDeviceItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnDeviceItemClickListener {
        void onDeviceItemClick(View view, Object device);
    }

    public static class DeviceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_name)
        TextView deviceName;

        @BindView(R.id.device_address)
        TextView deviceAddress;

//        @Bind(R.id.signal_strength)
//        Button signalStrengthIndicator;

        View rootView;

        public DeviceHolder(View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
        }
    }
}
