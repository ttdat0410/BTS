package vn.vnpt.ansv.bts.ui.monitor;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.objects.MinSensorFullObj;

import static vn.vnpt.ansv.bts.R.id.recyclerView;

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
        holder.txtSensorName.setText(dataSet.get(position).getSensorInfo().getSensorName());
//        holder.cardView.getLayoutParams().height = 300;
//        holder.cardView.getLayoutParams().width = 200;
        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getScreenWidth()/2 - 5);
        holder.cardView.setLayoutParams(lp);
        holder.cardView.requestLayout();
//        holder.deviceAddress.setText("AA");


        /*for (int z = 0; z < listSensorObj.size(); z++) {
                                        int sensorId = listSensorObj.get(z).getSensorInfo().getSensorId();
                                        String sensorName = listSensorObj.get(z).getSensorInfo().getSensorName();
                                        String sensorSerial = listSensorObj.get(z).getSensorInfo().getSensorSerial();
                                        int sensorTypeId = listSensorObj.get(z).getSensorInfo().getSensorTypeId();
                                        String measurementUnit = listSensorObj.get(z).getSensorInfo().getMeasurementUnit();
                                        int warningModeId = listSensorObj.get(z).getSensorInfo().getWarningModeId();
                                        int warningValue1 = listSensorObj.get(z).getSensorInfo().getWarningValue1();
                                        int warningValue2 = listSensorObj.get(z).getSensorInfo().getWarningValue2();
                                        int warningComp = listSensorObj.get(z).getSensorInfo().getWarningComp();

                                        SensorInfoObj sensorInfoObj = new SensorInfoObj(sensorId,
                                                sensorName, sensorSerial, sensorTypeId,
                                                measurementUnit, warningModeId,
                                                warningValue1, warningValue2, warningComp);

                                        List<SensorDataObj> sensorData = listSensorObj.get(z).getSensorData().getList();
                                        String sensorValue = String.valueOf(sensorData.get(0).getValue());
                                        Log.i("0x00", sensorInfoObj.getSensorName() + " " + sensorInfoObj.getMeasurementUnit() + " | " + sensorValue + "\n");
                                    }*/


//        holder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onDeviceItemClick(v, device);
//                }
//            }
//        });
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
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

        @BindView(R.id.txtSensorName)
        TextView txtSensorName;

        @BindView(R.id.card_view)
        CardView cardView;
//
//        @BindView(R.id.device_address)
//        TextView deviceAddress;

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
